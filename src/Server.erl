
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
	    gen_tcp:send(S, io_lib:format("~p~n",[{date(),time()}])),
	    gen_tcp:close(S),
	    loop(Listen);
	_      ->
	    loop(Listen)
    end.

%% COMMENT: Can loop0 and loop both be called loop? To fit aux functions
%% code standard. Better yet: maybe there is a better name for the 
%% function. Maybe listen_loop or something. - Carl

%%--------------------------------------------------------------%%
