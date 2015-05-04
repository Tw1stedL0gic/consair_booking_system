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

%%--------------------------------------------------------------%%

%% @doc The start function, used to start a loop which listens to a
%% certain port number.
-spec start() -> ok.

start ()->
    start(?PORTNO).

%% @doc Auxilary start function 
-spec start(Pno) -> ok when
      Pno::integer().

start(Pno) ->
    spawn(?MODULE, open_port, [Pno]).

%%--------------------------------------------------------------%%

%% @doc open_port
-spec open_port(Port) -> ok.

open_port(Port) ->
    %% Start listening to port
    case gen_tcp:listen(Port, [binary,            %% Accept data in binary
			       {packet,0},        %% I don't know what this is
			       {active,false}])   %% Server is not active (I don't know what that means)
    of 
	{ok, Socket} -> %% Port opened successfully
	    %% Start listen loop
	    listen_loop(Socket);
 	_           -> %% Any other result
	    %% Stop listening to port
	    stop
    end.

%% Need implement some sort of closing of the port

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

getPassengerlist(AL,FN) ->
    getFromDatabase. %% [1,2,3,4,5,6,7]. %% get from database

createMessage(4,[H | T]) ->
    foldl(fun(Elem,<<Acc/binary>>) -> <<Acc/binary,Elem:8>> end,<<(length([H|T])*8+1):32/unsigned,4:8/unsigned>>, [H|T])>>.

%%
%%message_handlerOLD(Message) 
%%    case Message of
%%	1  ->
%%	    
%%
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
%%
%%
%%
%%
%%			{1, Package, PID} -> %receive Id=1 with PID_1
%%				case validate({1,Package}) of
%%					false ->
%%						print_Bad_Argument, loop(Input);
%%					true -> gen_tcp::send(LSock, get({PassengerList, PID}), loop(Input))
%%				end;
%%			{2, Package, PID} -> % receive XXXXXXXX, do YYYYYYYYYY
%%				case validate({2,Package}) of
%%					false ->
%%						print_Bad_Argument;
%%					true -> PID ! {PassengerList, Package}
%%				end;
%%				loop(Input);
%%			{3, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%%				case validate({3,Package}) of
%%					false ->
%%						print_Bad_Argument, loop(Input);
%%%%					true -> gen_tcp::send(LSock, get({BookSeat, PID}), loop(Input))
%%				end;
%%			{4, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%%				case validate({4,Package}) of
%%					false ->
%%						print_Bad_Argument;
%%					true -> PID ! {BookSeat, Package}
%%				end			
%%				loop(Input);
%%			{5, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%%				case validate({5,Package}) of
%%					false ->
%%						print_Bad_Argument, loop(Input);
%%					true -> login({Login, PID}), loop(Input)
%%				end;
%%			{6, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%%				case validate({6,Package}) of
%%					false ->
%%						print_Bad_Argument;
%%					true -> PID ! {Login, Package}
%%				end			
%%				loop(Input);
%%%%%%%%%%			{7, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY		 
%%				case validate({7,Package}) of
%%					false ->
%%						print_Bad_Argument;
%%					true -> disconnect({Disconnect, PID})
%%				end
%%				loop(Input);
%%			{8, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%%				case validate({8,Package}) of
%%					false ->
%%						print_Bad_Argument;
%%					true -> heartbeat({Heartbeat, PID})
%%				end
%%				loop(Input);
%%			{9, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%%				case validate({9,Package}) of
%%					false ->
%%						print_Bad_Argument, loop(Input);
%%					true -> gen_tcp::send(LSock, get({PassengerInfo, PID}), loop(Input))
%%				end;
%%			{10, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
%%				case validate({10,Package}) of
%%					false ->
%%						print_Bad_Argument;
%%					true -> PID ! {PassengerInfo, Package}
%%				end		
%%				gen_tcp::close(LSock),
%%				loop(Listen);
%%	end.
		
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
    
