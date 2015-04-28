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
    spawn(?MODULE, loop0, [Pno]).

%%--------------------------------------------------------------%%

open_port(Port) ->
    %% Starts listening to Port. 
    {ok, listen_socket} = gen_tcp:listen(Port, [binary,              %% Accept data in binary
						{packet,0},          %% I don't know what this is
						{active,false}]),    %% Server is not active (I don't know what that means).
    %% Start listening loop
    listen_loop(listen_socket).
    %% Pseudo: Should have some code here to listen for a message
    %% to shut port. 

listen_loop(listen_socket) ->
    {ok, socket} = gen_tcp:accept(listen_socket),                    %% waits for connection to be established and saves it in socket.
    %% pseudo: spawn new process for this connection
    listen_loop(listen_socket);


%% Here is my suggestion for how the connection loop should work.
%% Open_port will open the port and then start the listening loop
%% which will wait for connections and when it's made a connection
%% it will spawn a process which will deal with the connection.
%%--------------------------------------------------------------%%

%% @doc loop
-spec loop0(Port) -> ok.

loop0(Port) ->
    case gen_tcp:listen(Port, [binary,{packet,0},{active,false}]) of 
	{ok, LSock} ->
	    loop(LSock);
 	_           ->
	    stop
    end.

%% @doc loop
-spec loop(Listen) -> ok.

loop(Listen) ->
    case gen_tcp:accept(Listen) of 
	{ok,S} ->
	    %%gen_tcp:send(S, io_lib:format("~p~n",[{date(),time()}])),
	    

	    gen_tcp:close(S),
	    loop(Listen);
	_      ->
	    loop(Listen)
    end.

%% COMMENT: Can loop0 and loop both be called loop? To fit aux functions
%% code standard. Better yet: maybe there is a better name for the 
%% function. Maybe listen_loop or something. - Carl
%%we are fixing this now.

%%--------------------------------------------------------------%%


%% @doc TODO: add documentation
-spec loop(Input) -> ok when 
      Input::[integer()|string()].

	  
%%Kan vara många fel här, under grov bearbetning. Ska bytas ut mot funktionen loop ovan
	

loop(Listen) ->
	case gen_tcp::accept(Listen) of
		{ok, LSock}->
			{1, Package, PID} -> %receive Id=1 with PID_1
				case validate({1,Package}) of
					false ->
						print_Bad_Argument, loop(Input);
					true -> gen_tcp::send(LSock, get({PassengerList, PID}), loop(Input))
				end;
			{2, Package, PID} -> % receive XXXXXXXX, do YYYYYYYYYY
				case validate({2,Package}) of
					false ->
						print_Bad_Argument;
					true -> PID ! {PassengerList, Package}
				end;
				loop(Input);
			{3, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
				case validate({3,Package}) of
					false ->
						print_Bad_Argument, loop(Input);
					true -> gen_tcp::send(LSock, get({BookSeat, PID}), loop(Input))
				end;
			{4, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
				case validate({4,Package}) of
					false ->
						print_Bad_Argument;
					true -> PID ! {BookSeat, Package}
				end			
				loop(Input);
			{5, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
				case validate({5,Package}) of
					false ->
						print_Bad_Argument, loop(Input);
					true -> login({Login, PID}), loop(Input)
				end;
			{6, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
				case validate({6,Package}) of
					false ->
						print_Bad_Argument;
					true -> PID ! {Login, Package}
				end			
				loop(Input);
			{7, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY		 
				case validate({7,Package}) of
					false ->
						print_Bad_Argument;
					true -> disconnect({Disconnect, PID})
				end
				loop(Input);
			{8, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
				case validate({8,Package}) of
					false ->
						print_Bad_Argument;
					true -> heartbeat({Heartbeat, PID})
				end
				loop(Input);
			{9, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
				case validate({9,Package}) of
					false ->
						print_Bad_Argument, loop(Input);
					true -> gen_tcp::send(LSock, get({PassengerInfo, PID}), loop(Input))
				end;
			{10, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
				case validate({10,Package}) of
					false ->
						print_Bad_Argument;
					true -> PID ! {PassengerInfo, Package}
				end		
				gen_tcp::close(LSock),
				loop(Listen);
	end.
		
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
