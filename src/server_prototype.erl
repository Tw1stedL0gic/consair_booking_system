-module(server_prototype).
-export([start_send/2, start_recv/1, connect_send/3, connect_recv/2, connect/2, listener/3, talker/3, start_listening/1, start_talking/3]).


-define(CONNECTIONOPTIONS, [binary, {packet, 4}, {active, false}]).
-define(CONNECTIONOPTIONS_AUX, [binary, {packet, 0}, {active, false}]).


%% HOW TO USE: use start_send or start_recv on "server" with appropriate arguments
%% Use use connect_recv or connect_send respectively on "client" to contact the server.
%% Port: usually 33333
%% Message: a bitstring. can be a normal string, but should be within << >> if something else. Example: <<1,1,1,1,1>>
%% IP: IP of server. If using the same computer write "localhost" (including the quotation marks).






start_listening(Port) ->
    %% open port
    {ok, LSock} = gen_tcp:listen(Port, ?CONNECTIONOPTIONS),
    listener_spawner(LSock, 1).

listener_spawner(_, 0) ->
    io:fwrite("Close message recieved, closing~n");

listener_spawner(LSock, N) ->
    receive
	close ->
	    listener_spawner(LSock, 0);
	terminated -> 
	    io:fwrite("Connection terminated.~n"),
	    listener_spawner(LSock, N-1)
	after 100 ->
	    %% connect to other device
	    case gen_tcp:accept(LSock, 100) of
		{ok, Package} ->
		    %% spawn process to handle this connection
		    NewPID = spawn(server_prototype, listener, [Sock, N, self()]),
		    io:fwrite("Number of connections: ~p, new process: ~p~n", [N, NewPID]),
		    listener_spawner(LSock, N+1);
		{error, timeout} ->
		    listener_spawner(LSock, N)
	    end
    end.
    
listener(Sock, N, ParentPID) ->    
    io:fwrite("C~p: Awaiting message...~n", [N]),
    case gen_tcp:recv(Sock, 0, 30000) of
	{error, timeout} -> 
	    io:fwrite("..."),
	    listener(Sock, N, ParentPID);
	{error, closed} ->
	    io:fwrite("C~p: Connection terminated.~n"),
	    ParentPID ! terminated;
	{error, Error} ->
	    io:fwrite("C~p: Error: ~p~n", [N, {error, Error}]),
	    {error, Error};
	{ok, Package} ->
	    io:fwrite("C~p: Message recieved: ~p~n", [N, Package]),
	    listener(Sock, N, ParentPID)
    end.
    
start_talking(IP, Port, Message) ->
    talker_spawner(IP, Port, Message).

talker_spawner(_, _, []) ->
    io:fwrite("Final message sent.~n");

talker_spawner(IP, Port, [Message | MessageTail]) ->
    NewPID = spawn(server_prototype, talker, [IP, Port, Message]),
    io:fwrite("Spawned ~p with message ~p~n", [NewPID, Message]),
    talker_spawner(IP, Port, MessageTail).

talker(IP, Port, Message) ->
    Sock = connect(IP, Port),
    timer:sleep(random:uniform(100)*1000),
    gen_tcp:send(Sock, Message),
    io:fwrite("~p sent!~n", [Message]).

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
    {ok, Sock} = gen_tcp:connect(IP, Port, ?CONNECTIONOPTIONS),
    Sock.
