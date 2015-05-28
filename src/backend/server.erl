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
-include_lib("eunit/include/eunit.hrl").
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
	    N = -1,
	    ?WRITE_SPAWNER("Port ~p busy~n", [Port]),
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
	    ?WRITE_SPAWNER("Exit called~n", []),
	    gen_tcp:close(LSock);
	disconnect ->  %% if received terminated -> reduce amount of connections
	    ?WRITE_SPAWNER("Process terminated~n", []),
	    connector_spawner(LSock, N-1);
	reload_code ->
	    code:load_file(server_utils),
	    code:purge(server_utils),
	    code:load_file(package_handler),
	    code:purge(package_handler),
	    code:load_file(booking_agent),
	    code:purge(booking_agent),
	    connector_spawner(LSock, N);
	{error, Error} ->
	    ?WRITE_SPAWNER("Error received! ~p~n", [{error, Error}]),
	    connector_spawner(LSock, N)
    after 100 ->
	    %% try connecting to other device for 100ms
	    case gen_tcp:accept(LSock, 100) of
		{ok, Sock} ->
		    %% spawn process to handle this connection
		    NewPID = spawn(?MODULE, connector, [Sock, N+1, 0, null, self()]),
		    ?WRITE_SPAWNER("New connection established: ~p~n", [NewPID]),
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
    ?WRITE_CONNECTION("~p timeouts reached, connection terminated~n", [?ALLOWEDTIMEOUTS]),
    Parent_PID ! disconnect;
    %% send message to client that it is timed out
    %% revert all database changes

connector(Sock, ID, Timeouts, User, Parent_PID) ->
    %% announce established connection to client and terminal
    case gen_tcp:recv(Sock, 0, 60000) of 
	{error, timeout} -> %% in case of timeout, reloop.
	    %% iterate a counter, which will warn client when timeouting too much
	    %% after 10 minutes it will break the connection.
	    ?WRITE_CONNECTION("Timeout ~p, ~p tries remaining~n", [Timeouts+1, ?ALLOWEDTIMEOUTS-Timeouts]), 
	    connector(Sock, ID, Timeouts+1, User, Parent_PID);
	{error, closed} -> %% in case of connection closed, tell parent. RECODE THIS TO IMPLEMENT HEARTBEAT
	    ?WRITE_CONNECTION("Connection unexpectantly closed, logging out user.~n", []),
	    package_handler:logout(User),
	    Parent_PID ! disconnect;
	{error, Error} -> %% In case of error, print error and announce termination to parent
	    ?WRITE_CONNECTION("{error, ~p}~n", [Error]),
	    Parent_PID ! disconnect;	
	{ok, Package} -> %% In case of package handle and responde
	    ?WRITE_CONNECTION("Message received:      ~p~n", [Package]),

	    %% Timestamp calculation
	    {Incoming_timestamp, Handled_package} = package_handler:handle_package(Package, User),
	    {Mega_S, S, Micro_S} = now(),
	    Time_taken = ((((Mega_S * 1000000) + S) * 1000000) + Micro_S) div 1000 - Incoming_timestamp,
	    ?WRITE_CONNECTION("Time to handle package: ~p~n", [Time_taken]),

	    %% case to handle package
	    case Handled_package of
		{ok, exit} ->
       		    ?WRITE_CONNECTION("Exit request~n", []),    
		    Parent_PID ! exit;
		{ok, disconnect} ->
		    ?WRITE_CONNECTION("Disconnecting~n", []),
		    Parent_PID ! disconnect;
		{ok, reload_code} ->
       		    ?WRITE_CONNECTION("Code reload request~n", []),    
		    Parent_PID ! reload_code;
		{ok, {admin, Response}} ->
		    ?WRITE_CONNECTION("Message sent:         ~p~n", [Response]),    
		    gen_tcp:send(Sock, Response),
		    ?WRITE_CONNECTION("Logged in as Admin~n", []),
		    connector(Sock, ID, 0, admin, Parent_PID);
		{ok, {New_user, Response}} ->
		    ?WRITE_CONNECTION("Message sent:          ~p~n", [Response]),    
		    gen_tcp:send(Sock, Response),
		    ?WRITE_CONNECTION("Logged in as ~p~n", [New_user]),
		    connector(Sock, ID, 0, New_user, Parent_PID);
		{ok, Response} ->
		    ?WRITE_CONNECTION("Message send:          ~p~n", [Response]),    
		    gen_tcp:send(Sock, Response),
		    connector(Sock, ID, 0, User, Parent_PID);
		{error, Error} ->
		    Parent_PID ! {error, Error}
	    end
    end.	  




%%--------------------------------------------------------------%%
%%TESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTE%%
%%--------------------------------------------------------------%%

%% startup_test() ->

%%     %% open
%%     spawn(server, start, [?PORT]),
    
%%     ?assertMatch({error, eaddrinuse}, server:start(?PORT)), 
%%     %% fail opening on already open port
%%     %% ?assertMatch({error, eaddrinuse}, server:start()), 
%%     %% close
%%     ?assertMatch(ok, server:stop()).
   

login_test() ->
    %% login
    ?assertMatch({ok, _}, connect_send_and_receive({?LOGIN, ["fake", "user"]},   ?PORT)),
    ?assertMatch({ok, _}, connect_send_and_receive({?LOGIN, ["carl", "asdasd"]}, ?PORT)),
    ?assertMatch({ok, _}, connect_send_and_receive({?LOGIN, ["pelle", "asd"]},   ?PORT)).
    

one_of_each_message_test() ->
    ?assertMatch({ok, _}, connect_send_and_receive({?LOGIN,                    ["pelle", "asd"]},   ?PORT)),
    ?assertMatch({error, timeout}, connect_send_and_receive({?ERROR,                    []},   ?PORT)),
    ?assertMatch({ok, _}, connect_send_and_receive_list([{?LOGIN, ["pelle", "asd"]}, {?INIT_BOOK,                ["1"]}],   ?PORT)),
    ?assertMatch({ok, _}, connect_send_and_receive({?ABORT_BOOK,               []},   ?PORT)),
    ?assertMatch({ok, _}, connect_send_and_receive({?INIT_BOOK,                ["1"]},   ?PORT)),
    ?assertMatch({ok, _}, connect_send_and_receive({?FIN_BOOK,                 []},   ?PORT)),

    ?assertMatch({ok, _}, connect_send_and_receive({?REQ_AIRPORTS,             []},   ?PORT)),
    ?assertMatch({ok, _}, connect_send_and_receive({?REQ_AIRPORTS,             ["1"]},   ?PORT)),
    ?assertMatch({ok, _}, connect_send_and_receive({?SEARCH_ROUTE,             []},   ?PORT)),
    ?assertMatch({ok, _}, connect_send_and_receive({?REQ_FLIGHT_DETAILS,       []},   ?PORT)),
    ?assertMatch({ok, _}, connect_send_and_receive({?REQ_SEAT_SUGGESTION,      []},   ?PORT)),
    ?assertMatch({ok, _}, connect_send_and_receive({?REQ_SEAT_MAP,             []},   ?PORT)),
    ?assertMatch({ok, _}, connect_send_and_receive({?TERMINATE_SERVER,         []},   ?PORT)).
    
%% sequential_stress_test() ->
%%     Login_info_list = [[User, Pass] || User <- ["Carl", "Lucas", "Oskar", "Erik", "Andreas", "Wentin"], Pass <- ["hej", "hehe", "asd", "asdasd", "rp", "asd"]],
%%     [?assertMatch({ok, _}, connect_send_and_receive({?LOGIN, Login_info},   ?PORT)) || Login_info <- Login_info_list].

%% concurrent_stress_test() ->
%%     Login_info_list = [[User, Pass] || User <- ["Carl", "Lucas", "Oskar", "Erik", "Andreas", "Wentin"], Pass <- ["hej", "hehe", "asd", "asdasd", "rp", "asd"]],
%%     ParentPID = self(),
%%     [spawn(fun() -> 
%% 		   ParentPID ! server_utils:connect_send_and_receive({?LOGIN, Login_info}, ?PORT) end) ||
%% 	Login_info <- Login_info_list],
%%     [?assertMatch({ok, _}, Answer) || Answer <- [receive X -> X end || _ <- Login_info_list]].

stop_test() ->    
    server_utils:stop_server().
     
no_server_test() ->
    %% send before opening
    ?assertMatch({error, econnrefused}, connect_send_and_receive({?HEARTBEAT}, ?PORT)),
    ?assertMatch({error, econnrefused}, connect_send_and_receive({?HEARTBEAT}, ?PORT)).
