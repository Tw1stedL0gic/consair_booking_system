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

%% here is a change

-module(server).
-export([start/0, start/1, connector_spawner/2, connector/5]).
-include("server_utils.hrl").
-define(Version_number, 0.8).

%%--------------------------------------------------------------%%

%% @doc The start function, used to start a loop which listens to a
%% certain port number.
-spec start() -> ok.

start() ->
    start(?PORT).

%% @doc Auxilary start function 
%% Port is the port to use
%% LSock is the listening sock. 

-spec start(Port) -> ok when
      Port::integer().

start(Port) ->
    %% Open port
    case gen_tcp:listen(Port, ?CONNECTIONOPTIONS) of 
	{ok, LSock} ->
	    %% spawn new process and let this one die 
	    %% spawn(?MODULE, connector_spawner, [LSock, 0]);
	    %% continue in same process
	    io:fwrite("=============================~n"),
	    io:fwrite("Server Initiated! Version ~p~n", [?Version_number]),
	    io:fwrite("=============================~n"),
	    case lists:keyfind(addr, 1, element(2, lists:keyfind("wlan0", 1, element(2, inet:getifaddrs())))) of
		false ->
		    ok;
		W_IP ->
		    io:fwrite("Wireless IP Address: ~p~n", [element(2, W_IP)])
	    end,
	    case lists:keyfind(addr, 1, element(2, lists:keyfind("eth0", 1, element(2, inet:getifaddrs())))) of
		false ->
		    ok;
		E_IP ->
		    io:fwrite("Ethernet IP Address: ~p~n", [element(2, E_IP)])
	    end,
	    connector_spawner(LSock, 0);
	{error, eaddrinuse} ->
	    io:fwrite("Port busy~n"),
	    {error, eaddrinuse};
	_ ->
	    {error, could_not_listen}		
    end.

%%--------------------------------------------------------------%%

%% @doc a spawner for listening processes. will switch between
%% listening for messages from processes and listening to
%% attempts to create connections. 
%% LSock is the listening sock
%% N is the number of active connections

%% Calling this function with N = 0 will close the server. 

connector_spawner(LSock, N) ->
    %% receive message from other processes for 100ms
    receive
	exit ->        %% if received exit -> exit the loop (connection processes are still alive)
	    io:fwrite("*CS* ~p: Connections: ~p | Exit called~n", [self(), N]),
	    gen_tcp:close(LSock);
	disconnect ->  %% if received terminated -> reduce amount of connections
	    io:fwrite("*CS* ~p: Connections: ~p | Process terminated~n", [self(), N-1]),
	    connector_spawner(LSock, N-1);
	reload_code ->
	    code:load_file(server),
	    code:load_file(server_utils),
	    code:load_file(package_handler),
	    code:load_file(booking_agent)
    after 100 ->
	    %% try connecting to other device for 100ms
	    case gen_tcp:accept(LSock, 100) of
		{ok, Sock} ->
		    %% spawn process to handle this connection
		    NewPID = spawn(?MODULE, connector, [Sock, N+1, 0, null, self()]),
		    io:fwrite("*CS* ~p: Connections: ~p | New connection established: ~p~n", [self(), N+1, NewPID]),
		    connector_spawner(LSock, N+1);
		{error, timeout} ->
		    connector_spawner(LSock, N);
		{error, Error} ->
		    io:fwrite("Error: ~p~n", [{error, Error}])
	    end
    end.


%% @doc Connector, which communicates with the client. 
%% it waits 60 seconds for a message, then issues a 
%% timeout. After the defined allowed timeouts it will
%% terminate the communication. 
%% In the case of a message it will handle it accordingl

%% Connection timed out (10 timeouts).
connector(_, ID, ?ALLOWEDTIMEOUTS, User, Parent_PID) ->
    io:fwrite("C~p ~p (~p): ~p timeouts reached, connection terminated~n", [ID, self(), User, ?ALLOWEDTIMEOUTS]),
    Parent_PID ! disconnect;
    %% send message to client that it is timed out
    %% revert all database changes

connector(Sock, ID, Timeouts, User, Parent_PID) ->
    %% announce established connection to client and terminal
    case gen_tcp:recv(Sock, 0, 60000) of 
	{error, timeout} -> %% in case of timeout, reloop.
	    %% iterate a counter, which will warn client when timeouting too much
	    %% after 10 minutes it will break the connection.
	    io:fwrite("C~p ~p (~p): Timeout ~p, ~p tries remaining~n", [ID, self(), User, Timeouts+1, ?ALLOWEDTIMEOUTS-Timeouts]), 
	    connector(Sock, ID, Timeouts+1, User, Parent_PID);
	{error, closed} -> %% in case of connection closed, tell parent. RECODE THIS TO IMPLEMENT HEARTBEAT
	    io:fwrite("C~p ~p (~p): Connection unexpectantly closed, logging out user.~n", [ID, self(), User]),
	    package_handler:logout(User),
	    Parent_PID ! disconnect;
	{error, Error} -> %% In case of error, print error and announce termination to parent
	    io:fwrite("C~p ~p (~p): {error, ~p}~n", [ID, self(), User, Error]),
	    Parent_PID ! disconnect;	
	{ok, Package} -> %% In case of package handle and responde
	    io:fwrite("C~p ~p (~p): Message received: ~p~n", [ID, self(), User, Package]),

	    %% Timestamp calculation
	    {Timestamp, Handled_package} = package_handler:handle_package(Package, User),
	    {Mega_S, S, Micro_S} = now(),
	    Time_taken = ((((Mega_S * 1000) + S) * 1000) + (Micro_S rem 1000)) - Timestamp,
	    io:fwrite("C~p ~p (~p): Time to handle package: ~p~n", [ID, self(), User, Time_taken]),

	    %% case to handle package
	    case Handled_package of
		{ok, exit} ->
       		    io:fwrite("C~p ~p (~p): Exit request~n", [ID, self(), User]),    
		    Parent_PID ! exit;
		{ok, reload_code} ->
       		    io:fwrite("C~p ~p (~p): Code reload request~n", [ID, self(), User]),    
		    Parent_PID ! reload_code;
		{ok, {admin, Response}} ->
		    io:fwrite("C~p ~p (~p): Message send: ~p~n", [ID, self(), User, Response]),    
		    gen_tcp:send(Sock, Response),
		    io:fwrite("C~p ~p (~p): Logged in as Admin~n", [ID, self(), User]),
		    connector(Sock, ID, 0, admin, Parent_PID);
		{ok, {New_user, Response}} ->
		    io:fwrite("C~p ~p (~p): Message send: ~p~n", [ID, self(), User, Response]),    
		    gen_tcp:send(Sock, Response),
		    io:fwrite("C~p ~p (~p): Logged in as ~p~n", [ID, self(), User, New_user]),
		    connector(Sock, ID, 0, New_user, Parent_PID);
		{ok, Response} ->
		    io:fwrite("C~p ~p (~p): Message send: ~p~n", [ID, self(), User, Response]),    
		    gen_tcp:send(Sock, Response),
		    connector(Sock, ID, 0, User, Parent_PID);
		{error, Error} ->
		    Parent_PID ! {error, Error}
	    end
    end.	  
