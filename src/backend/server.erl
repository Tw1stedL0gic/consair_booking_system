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
-export([start/0, start/1, stop/0, stop/1, stop/2, connector_spawner/2, connector/5]).
-define(PORT, 53535).
-define(CONNECTIONOPTIONS, [binary, {packet, 0}, {active, false}]).
-define(ALLOWEDTIMEOUTS, 10). %% Amount of minutes allowed before connection is terminated
-define(ADMIN_ALLOWEDTIMEOUTS, 20). %% Amount of minutes allowed before admin connection is terminated
-define(Admin_login, <<"1&carl&asdasd&\n">>).
-define(Terminate_server, <<"24&\n">>).

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
	    connector_spawner(LSock, 0);
	{error, eaddrinuse} ->
	    io:fwrite("Port busy~n");
	_ ->
	    {error, could_not_listen}		
    end.

%%--------------------------------------------------------------%%

stop() ->
    stop("localhost").

stop(IP) ->
    stop(IP, ?PORT).

stop(IP, Port) ->
    case gen_tcp:connect(IP, Port, ?CONNECTIONOPTIONS) of
	{ok, Sock} ->
	    gen_tcp:send(Sock, ?Admin_login),
	    timer:sleep(1000),
	    gen_tcp:send(Sock, ?Terminate_server);
	{error, Error} ->
	    {error, Error}
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
	    io:fwrite("Number of connections: ~p  Exit called~n", [N]),
	    gen_tcp:close(LSock);
	disconnect ->  %% if received terminated -> reduce amount of connections
	    io:fwrite("Number of Connections: ~p  Process terminated~n", [N-1]),
	    connector_spawner(LSock, N-1)
    after 100 ->
	    %% try connecting to other device for 100ms
	    case gen_tcp:accept(LSock, 100) of
		{ok, Sock} ->
		    %% spawn process to handle this connection
		    NewPID = spawn(?MODULE, connector, [Sock, N+1, 0, null, self()]),
		    io:fwrite("Number of Connections: ~p  New process: ~p~n", [N+1, NewPID]),
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
connector(_, ID, ?ALLOWEDTIMEOUTS, _, ParentPID) ->
    io:fwrite("C~p: ~p timeouts reached, connection terminated~n", [ID, ?ALLOWEDTIMEOUTS]),
    ParentPID ! disconnect;
    %% send message to client that it is timed out
    %% revert all database changes

connector(Sock, ID, Timeouts, User, ParentPID) ->
    %% announce established connection to client and terminal
    gen_tcp:send(Sock, <<ID>>),
    case User of
	null -> io:fwrite("C~p: Connection established~n", [ID]);
	admin -> io:fwrite("C~p: Admin connection established~n", [ID]);
	User -> io:fwrite("C~p: User (~p) connection established~n", [ID, User])
    end,    
    case gen_tcp:recv(Sock, 0, 60000) of
	{error, timeout} -> %% in case of timeout, reloop.
	    %% iterate a counter, which will warn client when timeouting too much
	    %% after 10 minutes it will break the connection.
	    io:fwrite("C~p: Timeout ~p, ~p tries remaining~n", [ID, Timeouts+1, ?ALLOWEDTIMEOUTS-Timeouts]), 
	    connector(Sock, ID, Timeouts+1, User, ParentPID);
	{error, closed} -> %% in case of connection closed, tell parent. RECODE THIS TO IMPLEMENT HEARTBEAT
	    io:fwrite("C~p: Connection closed, disconnecting user.~n", [ID]),
	    package_handler:handle_package(disconnect, User),
	    ParentPID ! disconnect;
	{error, Error} -> %% In case of error, print error and announce termination to parent
	    io:fwrite("C~p: {error, ~p}~n", [ID, Error]),
	    ParentPID ! disconnect;	
	{ok, Package} -> %% In case of package handle and responde
	    io:fwrite("C~p: Message received: ~p~n", [ID, Package]),
	    %% case to handle package
	    case package_handler:handle_package(Package, User) of
		{ok, exit} ->
		    ParentPID ! exit;
		{ok, {admin, Response}} ->
		    gen_tcp:send(Sock, Response),
		    connector(Sock, ID, 0, admin, ParentPID);
		{ok, {NewUser, Response}} ->
		    gen_tcp:send(Sock, Response),
		    connector(Sock, ID, 0, NewUser, ParentPID);
		{ok, Response} ->
		    gen_tcp:send(Sock, Response),
		    connector(Sock, ID, 0, User, ParentPID);
		{error, Error} ->
		    ParentPID ! {error, Error}
	    end
    end.	  
