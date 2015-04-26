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
		
		
