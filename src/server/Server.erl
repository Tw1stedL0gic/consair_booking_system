%% ------------------------
%% @title Name Of This File
%% @version 1.0.0
%% {@date}
%% @author Sir Derpatron the V 
%% @doc Description of this file and a quick rundown of what the
%% user will use it for. 
%% @end
%% ------------------------

%%Handles the server io for the booking. This is the abstraction layer for java / erlang%%

-module('Server').
-export([start/0, loop0/1]).
-define(PORTNO, 33333).
-define(CONNECTIONOPTIONS, [binary, {packet, 4}, {active, false}]).
-define(ALLOWEDTIMEOUTS, 10). %% Amount of minutes allowed before conenction is terminated

%%--------------------------------------------------------------%%

%% @doc The start function, used to start a loop which listens to a
%% certain port number.
-spec start() -> ok.

start ()->
    start(?PORTNO).

%% @doc Auxilary start function 
%% Port is the port to use
%% LSock is the listening sock. 

-spec start(Port) -> ok when
      Port::integer().

start(Port) ->
    %% Open port
    {ok, LSock} = gen_tcp:listen(Port, ?CONNECTIONOPTIONS),
    listener_spawner(LSock, 0).

%%--------------------------------------------------------------%%

%% @doc a spawner for listening processes. will switch between
%% listening for messages from processes and listening to
%% attempts to create connections. 
%% LSock is the listening sock
%% N is the number of active connections

%% Calling this function with N = 0 will close the server. 

connector_spawner(_, -1) ->
    io:fwrite("Exit message received, closing~n");

connector_spawner(LSock, N) ->
    %% receive message from other processes for 100ms
    receive
	exit ->        %% if received exit -> exit the loop (connection processes are still alive)
	    io:fwrite("Number of connections: ~p  Exit called~n", [N]),
	    connector_spawner(LSock, -1);
	terminated ->  %% if received terminated -> reduce amount of connections
	    io:fwrite("Number of Connections: ~p  Process terminated~n", [N-1, NewPID]),
	    connector_spawner(LSock, N-1)
    after 100 ->
	    %% try connecting to other device for 100ms
	    case gen_tcp:accept(LSock, 100) of
		{ok, Sock} ->
		    %% spawn process to handle this connection
		    NewPID = spawn(server_prototype, connector, [Sock, N+1, 0, self()]),
		    io:fwrite("Number of Connections: ~p  New process: ~p~n", [N+1, NewPID]),
		    connector_spawner(LSock, N+1);
		{error, timeout} ->
		    connector_spawner(LSock, N);
		{error, Error} ->
		    io:fwrite("Error: ~p", [{error, Error}])
	    end
    end.

%% @doc Connector, which listens to the connection

%% Connection timed out (10 timeouts).
connector(Sock, ID, 10, ParentPID) ->
    %% send message to client that it is timed out
    %% revert all database changes
    io:fwrite("C~p: ~p timeouts reached, connection terminated~n", [ID, ?ALLOWEDTIMEOUTS]).

connector(Sock, ID, Timeouts, ParentPID) ->
    %% announce established connection to client and terminal
    gen_tcp:send(Sock, <<ID>>),
    io:fwrite("C~p: Connection established", [ID]),
    
    case gen_tcp:recv(Sock, 0, 60000) of
	{error, timeout} -> %% in case of timeout, reloop.
	    %% iterate a counter, which will warn client when timeouting too much
	    %% after 10 minutes it will break the connection.
	    io:fwrite("C~p: Timeout ~n, ~n tries remaining", [Timeouts, ?ALLOWEDTIMEOUTS-Timeouts]), 
	    connector(Sock, ID, Timeouts+1, ParentPID);
	{error, closed} -> %% in case of connection closed, tell parent
	    io:fwrite("C~p: Connection closed, terminating.~n", [ID]),
	    ParentPID ! Terminated;
	{error, Error} -> %% In case of error, print error and announce termination to parent
	    io:fwrite("C~p: {error, ~p}", [ID, Error]),
	    ParentPID ! Terminated;	
	{ok, <<"exit">>} -> %% In case of message "exit", tell parent to exit
	    io:fwrite("C~p: Exit message received", [ID]),
	    ParentPID ! exit;	    
	{ok, Package} -> %% In case of random package, print and send back "Thanks"
	    io:fwrite("C~p: Message received: ~p~n", [ID, Package]),
	    gen_tcp:send(Sock, <<1, "Thanks">>),
	    listener(Sock, ID, 0, ParentPID)
    end.
    

%% Here is my suggestion for how the connection loop should work.
%% Open_port will open the port and then start the listening loop
%% which will wait for connections and when it's made a connection
%% it will spawn a process which will deal with the connection.
%%--------------------------------------------------------------%%

%% @doc loop
%% -----------
%%-spec loop0(Port) -> ok.

open_port(Port) ->
    %% Start listening to port
    case gen_tcp:listen(Port, [binary,            %% Accept data in binary
			       {packet,5},        %% I don't know what this is
			       {active,false}])   %% Server is not active (I don't know what that means)
    of 
	{ok, Socket} -> %% Port opened successfully
	    %% Start listen loop
	    listen_loop(Socket);
 	_           -> %% Any other result
	    %% Stop listening to port
	    stop
    end.

%% @doc loop
%%-spec loop(Listen) -> ok.

%%--------------------------------------------------------------%%

%% @doc listen_loop
-spec listen_loop(Socket) -> ok.

listen_loop(Socket) ->
    case gen_tcp:accept(Socket) of 
	{ok,S} ->
	    %% Spawn process to handle this connection
	    spawn(?MODULE, loop, [S]),   
	    gen_tcp:close(S),
	    loop(Socket);
	_      ->
	    loop(Socket)
    end.

%%--------------------------------------------------------------%%

connection_handler(S) ->
    %% handling connection with socket S

    case gen_tcp:recv(S, 4) of 
	{error, closed} ->
	    %% error because port was closed
	    tbi;
	{error, Reason} ->
	    %% handle error
	    tbi;
	{ok, Package} ->
	    %% convert Package into a format that 
	    %% message handler can read, also make
	    %% sure that package is correct
	    <<MessLeng:32/unsigned>> = Package,
	    case gen_tcp:recv(S, MessLeng) of
		{error, closed} ->
		    %% error because port was closed
		    tbi;
		{error, Reason} ->
		    %% handle error
		    tbi;
		{ok, <<ID:8/unsigned, Mess/binary>>} ->
		    %% convert Package into a format that 
		    %% message handler can read, also make
		    %% sure that package is correct
		    case message_handler(ID, Mess) of
			{ok, SendData} ->
			    gen_tcp:send(S,SendData);
			{err, Reason}  -> tbi
		    end
			 
	    end
end.

%%--------------------------------------------------------------%%

%% @doc TODO: add documentation
-spec loop(Input) -> ok when 
      Input::[integer()|string()].

	  
%%Kan vara många fel här, under grov bearbetning. Ska bytas ut mot funktionen loop ovan
	
%%	    %% 1: Hämta passagerare (front-end to back-end)
%%	    %% 2: Passagerarlista (back-end to front-end)
%%	    %% 3: Boka plats
%%	    %% 4: Response to #3 (lyckat / misslyckat)
%%	    %% 5: Login
%%	    %% 6: Response #5
%%	    %% 7: Disconnect / Terminera Anslutning
%%	    %% 8: Heartbeat
%%	    %% 9: Get passenger info (front-end -> back-end)
%%	    %% 10: Response #9
message_handler(1,Mess) ->
    tbi;
message_handler(2,Mess) ->
    tbi;
message_handler(3,Mess) -> %% Get passengerlist
    <<AL:16/unsigned, FN/binary>> = Mess,
    {ok, createMessage(4,getPassengerlist(AL,FN))};
message_handler(4,Mess) ->
    {err, unsupported};
message_handler(5,Mess) ->
    tbi;
message_handler(6,Mess) ->
    tbi;
message_handler(7,Mess) ->
    tbi;
message_handler(8,Mess) ->
    tbi;
message_handler(9,Mess) ->
    tbi;
message_handler(10,Mess) ->
    tbi;

%% loop(Listen) ->
%%     case gen_tcp:accept(Listen) of
%% 	{ok, LSock}->
%% 	    {1, Package, PID} -> %receive Id=1 with PID_1
%% 			   case validate({1,Package}) of
%% 			       false ->
%% 				   print_Bad_Argument, loop(Input);
%% 			       true -> gen_tcp::send(LSock, get({PassengerList, PID}), loop(Input))
%% 						end;
%% 			{2, Package, PID} -> % receive XXXXXXXX, do YYYYYYYYYY
%% 				case validate({2,Package}) of
%% 					false ->
%% 						print_Bad_Argument;
%% 					true -> PID ! {PassengerList, Package}
%% 				end;
%% 				loop(Input);
%% 			{3, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%% 				case validate({3,Package}) of
%% 					false ->
%% 						print_Bad_Argument, loop(Input);
%% 					true -> gen_tcp::send(LSock, get({BookSeat, PID}), loop(Input))
%% 				end;
%% 			{4, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%% 				case validate({4,Package}) of
%% 					false ->
%% 						print_Bad_Argument;
%% 					true -> PID ! {BookSeat, Package}
%% 				end			
%% 				loop(Input);
%% 			{5, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%% 				case validate({5,Package}) of
%% 					false ->
%% 						print_Bad_Argument, loop(Input);
%% 					true -> login({Login, PID}), loop(Input)
%% 				end;
%% 			{6, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%% 				case validate({6,Package}) of
%% 					false ->
%% 						print_Bad_Argument;
%% 					true -> PID ! {Login, Package}
%% 				end			
%% 				loop(Input);
%% 			{7, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY		 
%% 				case validate({7,Package}) of
%% 					false ->
%% 						print_Bad_Argument;
%% 					true -> disconnect({Disconnect, PID})
%% 				end
%% 				loop(Input);
%% 			{8, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%% 				case validate({8,Package}) of
%% 					false ->
%% 						print_Bad_Argument;
%% 					true -> heartbeat({Heartbeat, PID})
%% 				end
%% 				loop(Input);
%% 			{9, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%% 				case validate({9,Package}) of
%% 					false ->
%% 						print_Bad_Argument, loop(Input);
%% 					true -> gen_tcp::send(LSock, get({PassengerInfo, PID}), loop(Input))
%% 				end;
%% 			{10, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%% 				case validate({10,Package}) of
%% 					false ->
%% 						print_Bad_Argument;
%% 					true -> PID ! {PassengerInfo, Package}
%% 				end		
%% 				gen_tcp::close(LSock),
%% 				loop(Listen);
%% 	end.
		
%% COMMENT: Remember that an atom is as large as an integer in Erlang, 
%% so to make it clearer we might want to change the numbers 1-10 to 
%% atoms. Atoms take up space in the atom table, but a constant 10
%% atoms will not take up too much space. 
%%
%% This is because I was not there to write this, but why does
%% each case need the validate({X, Package}) part? Surely, if the
%% input manages to pass the pattern matching then we shouldn't need
%% to validate. (Also I can't find what the validate() function does. 
%% Have we written it ourselves?
%%
%% We should also add comments to each case so we know what each 
%% case is handling. 
%% - Carl

		
%%--------------------------------------------------------------%%

%% @doc this function will decipher the message sent
%% It will take the message and the size. 
%% The idea is that it will be used for the 
%% first 4 meta-info-bytes as well as the rest
%% of the message. 

message_translator(Message, size) ->
    %% translate the bytes into a format that erlang will understand

hardcoded_message_translator(Message, 4) ->
    5;
hardcoded_message_translator(Message, 5) ->
    
