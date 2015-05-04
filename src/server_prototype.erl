-module(server_prototype).
-export([start_send/2, start_recv/1, connect_send/3, connect_recv/2]).


-define(CONNECTIONOPTIONS, [binary, {packet, 4}, {active, false}]).
-define(CONNECTIONOPTIONS_AUX, [binary, {packet, 0}, {active, false}]).


%% HOW TO USE: use start_send or start_recv on "server" with appropriate arguments
%% Use use connect_recv or connect_send respectively on "client" to contact the server.
%% Port: usually 33333
%% Message: a bitstring. can be a normal string, but should be within << >> if something else. Example: <<1,1,1,1,1>>
%% IP: IP of server. If using the same computer write "localhost" (including the quotation marks).


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
    {ok, LSock} = gen_tcp:listen(Port, ?CONNECTIONOPTIONS),
    {ok, Sock} = gen_tcp:accept(LSock),
    Sock.

connect_send(IP, Port, Message) ->
    Sock = connect(IP, Port),
    gen_tcp:send(Sock, Message).

connect_recv(IP, Port) ->
    Sock = connect(IP, Port),  
    {ok, Package} = gen_tcp:recv(Sock, 0),
    Package.

connect(IP, Port) ->
    {ok, Sock} = gen_tcp:connect(IP, Port, ?CONNECTIONOPTIONS_AUX),
    Sock.
