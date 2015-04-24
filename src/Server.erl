
%%Handels the server io for the booking. This is the abstration layer for java / erlang%%

-module('Server').
-export([start/0, loop0/1]).
-define(PORTNO, 33333).

start ()->
    start(?PORTNO).
start(Pno) ->
    spawn(?MODULE, loop0, [Pno]).

loop0(Port)->
    case gen_tcp:listen(Port, [binary,{packet,0},{activ,false}]) of 
	{ok, LSock}->
	    loop(LSock);
	_ ->
	    stop
    end.

loop(Listen) ->
    case gen_tcp:accept(Listen) of 
	{ok,S} ->
	    gen_tcp:send(S, io_lib:format("~p~n",[{date(),time()}])),
	    gen_tcp:close(S),
	    loop(Listen);
	_ ->
	    loop(Listen)
    end.
