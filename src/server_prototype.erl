-module(server_prototype).
-export([start_send/2, start_recv/1]).


start_send(Port, Message) ->
    Sock = start(Port),
    timer:sleep(1000),
    gen_tcp:send(Sock, Message),
    gen_tcp:close(Sock).

start_recv(Port) ->
    Sock = start(Port),
    {ok, Packet} = gen_tcp:recv(Sock, 0),
    gen_tcp:close(Sock),
    Packet.

start(Port) ->
    {ok, LSock} = gen_tcp:listen(Port, [binary, {packet, 0}, {active, false}]), 
    {ok, Sock} = gen_tcp:accept(LSock),
    Sock.
