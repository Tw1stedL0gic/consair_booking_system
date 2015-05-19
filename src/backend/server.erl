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
-export([start/0, start/1, connector_spawner/2, connector/4]).
-define(PORT, 53535).
-define(CONNECTIONOPTIONS, [binary, {packet, 4}, {active, false}]).
-define(ALLOWEDTIMEOUTS, 10). %% Amount of minutes allowed before conenction is terminated

%%--------------------------------------------------------------%%

%% @doc The start function, used to start a loop which listens to a
%% certain port number.
-spec start() -> ok.

start () ->
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
	    spawn(?MODULE, connector_spawner, [LSock, 0]);
	{error, eaddrinuse} ->
	    io:fwrite("Port busy~n")
%	    gen_tcp:listen(0, ?CONNECTIONOPETIONS),
%	    io:fwrite("New port assigned: ~p~n", [inet:port()
    end.

%%--------------------------------------------------------------%%

%% @doc a spawner for listening processes. will switch between
%% listening for messages from processes and listening to
%% attempts to create connections. 
%% LSock is the listening sock
%% N is the number of active connections

%% Calling this function with N = 0 will close the server. 

connector_spawner(_, -1) ->
    io:fwrite("Exit message received, closing~n"),
    %% call exit function to close port
    exit(normal);

connector_spawner(LSock, N) ->
    %% receive message from other processes for 100ms
    receive
	exit ->        %% if received exit -> exit the loop (connection processes are still alive)
	    io:fwrite("Number of connections: ~p  Exit called~n", [N]),
	    connector_spawner(LSock, -1);
	terminated ->  %% if received terminated -> reduce amount of connections
	    io:fwrite("Number of Connections: ~p  Process terminated~n", [N-1]),
	    connector_spawner(LSock, N-1)
    after 100 ->
	    %% try connecting to other device for 100ms
	    case gen_tcp:accept(LSock, 100) of
		{ok, Sock} ->
		    %% spawn process to handle this connection
		    NewPID = spawn(?MODULE, connector, [Sock, N+1, 0, self()]),
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
connector(_, ID, 10, ParentPID) ->
    io:fwrite("C~p: ~p timeouts reached, connection terminated~n", [ID, ?ALLOWEDTIMEOUTS]),
    ParentPID ! terminated;
    %% send message to client that it is timed out
    %% revert all database changes

connector(Sock, ID, Timeouts, ParentPID) ->
    %% announce established connection to client and terminal
    gen_tcp:send(Sock, <<ID>>),
    io:fwrite("C~p: Connection established~n", [ID]),
    
    case gen_tcp:recv(Sock, 0, 60000) of
	{error, timeout} -> %% in case of timeout, reloop.
	    %% iterate a counter, which will warn client when timeouting too much
	    %% after 10 minutes it will break the connection.
	    io:fwrite("C~p: Timeout ~p, ~p tries remaining~n", [ID, Timeouts, ?ALLOWEDTIMEOUTS-Timeouts]), 
	    connector(Sock, ID, Timeouts+1, ParentPID);
	{error, closed} -> %% in case of connection closed, tell parent. RECODE THIS TO IMPLEMENT HEARTBEAT
	    io:fwrite("C~p: Connection closed, terminating.~n", [ID]),
	    ParentPID ! terminated;
	{error, Error} -> %% In case of error, print error and announce termination to parent
	    io:fwrite("C~p: {error, ~p}~n", [ID, Error]),
	    ParentPID ! terminated;	
	{ok, Package} -> %% In case of random package, print and send back "Thanks"
	    io:fwrite("C~p: Message received: ~p~n", [ID, Package]),
	    %% case to handle package
	    case package_handler:handle_package(Package) of
		exit ->
		    ParentPID ! exit;	    
		Response ->
		    gen_tcp:send(Sock, Response)
	    end,
	    %% reloop and reset timeouts
	    connector(Sock, ID, 0, ParentPID)
    end.	  

