
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
-export([start/0, start/1, connector_spawner/2, connector_inbox/6, connector_handler/5]).
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
	    ?DRAW_LOGO,
	    ?DRAW_TITLE("SERVER INITIATED, Version number" ++ ?VERSION),
	    ?DRAW_TABLE_HEADER,
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
	    ?DRAW_TITLE("Port " ++ integer_to_list(Port) ++ " busy "),
	    ?DRAW_LOGO,
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
	    ?DRAW_TITLE("Exiting Server~nThank you for choosing Cons-Air"),
	    ?DRAW_LOGO,
	    gen_tcp:close(LSock);
	disconnect ->  %% if received terminated -> reduce amount of connections
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
	    ?WRITE_SPAWNER("Error received! {error, ~p}~n", [Error], "E"),
	    connector_spawner(LSock, N)
    after 100 ->
	    %% try connecting to other device for 100ms
	    case gen_tcp:accept(LSock, 100) of
		{ok, Sock} ->
		    %% spawn process to handle this connection
		    New_connector_handler = spawn(?MODULE, connector_handler, [Sock, N+1, 0, null, self()]),
		    spawn(?MODULE, connector_inbox, [Sock, N+1, 0, null, self(), New_connector_handler]),
		    ?WRITE_SPAWNER("New connection establishing: ~p~n", [New_connector_handler], "N"),
		    connector_spawner(LSock, N+1);
		{error, timeout} ->
		    connector_spawner(LSock, N);
		{error, Error} ->
		    io:fwrite("Error: ~p~n", [{error, Error}])
	    end
    end.

connector_inbox(__, ID, ?ALLOWEDTIMEOUTS, User, Parent_PID, Handler_PID) ->
    ?WRITE_CONNECTION("~p timeouts reached, connection terminated~n", [?ALLOWEDTIMEOUTS], "D"),

    Handler_PID ! disconnect,
    Parent_PID  ! disconnect;

connector_inbox(Sock, ID, Timeouts, User, Parent_PID, Handler_PID) ->
    case gen_tcp:recv(Sock, 0, 60000) of
	{error, timeout} ->
	    ?WRITE_CONNECTION("Timeout ~p, ~p tries remaining~n", [Timeouts+1, ?ALLOWEDTIMEOUTS - Timeouts], "T"),
	    connector_inbox(Sock, ID, Timeouts+1, User, Parent_PID, Handler_PID);
	{error, closed} ->
	    Handler_PID ! {error, closed};
	{error, Error} ->
	    ?WRITE_CONNECTION("{error, ~p}~n", [Error], "E"),
	    Parent_PID ! disconnect;
	{ok, Package} ->
	    Package_list = lists:droplast(re:split(Package, ?MESSAGE_SEPERATOR)),
					  
	    %%---------- SEND TO HANDLER ------------%%
	    pass_message_list(Package_list, Handler_PID),
	    
	    connector_inbox(Sock, ID, Timeouts, User, Parent_PID, Handler_PID)
	end.


connector_handler(Sock, ID, Timeouts, User, Parent_PID) ->
    receive
	{ok, Package} -> %% In case of package handle and responde
	    ?WRITE_CONNECTION("Message received: <<<<<  ~p~n", [Package], "<"),

	    %% Timestamp calculation
	    {Incoming_timestamp, Handled_package} = package_handler:handle_package(Package, User),
	    {Mega_S, S, Micro_S} = now(),
	    Time_taken = ((((Mega_S * 1000000) + S) * 1000000) + Micro_S) div 1000 - Incoming_timestamp,
	    ?WRITE_CONNECTION("Time to handle package: ~p~n", [Time_taken], " "),

	    %% case to handle package
	    case Handled_package of
		{ok, exit} ->
       		    ?WRITE_CONNECTION("Exit request~n", [], "X"),    
		    Parent_PID ! exit;
		{ok, disconnect} ->
		    ?WRITE_CONNECTION("Disconnecting~n", [], "D"),
		    Parent_PID ! disconnect;
		{ok, reload_code} ->
       		    ?WRITE_CONNECTION("Code reload request~n", [], "R"),    
		    Parent_PID ! reload_code;
		{ok, {admin, Response}} ->
		    ?WRITE_CONNECTION("Message sent:     >>>>> ~n", [], ">"),    
		    gen_tcp:send(Sock, Response),
		    ?WRITE_CONNECTION("Logged in as Admin~n", [], " "),
		    connector_handler(Sock, ID, 0, admin, Parent_PID);
		{ok, {New_user, Response}} ->
		    ?WRITE_CONNECTION("Message sent:     >>>>> ~n", [], ">"),    
		    gen_tcp:send(Sock, Response),
		    ?WRITE_CONNECTION("Logged in as ~p~n", [New_user], " "),
		    connector_handler(Sock, ID, 0, New_user, Parent_PID);
		{ok, Response} ->
		    ?WRITE_CONNECTION("Message send:     >>>>>  ~n", [], ">"),    
		    gen_tcp:send(Sock, Response),
		    connector_handler(Sock, ID, 0, User, Parent_PID);
		{error, Error} ->
       		    Parent_PID ! {error, Error},
		    gen_tcp:send(Sock, translate_package({?ERROR, atom_to_list(Error)})),
		    connector_handler(Sock, ID, 0, User, Parent_PID);
		{client_error, Error} ->
		    ?WRITE_CONNECTION("{client_error, ~p}~n", [Error], "E"),
		    connector_handler(Sock, ID, 0, User, Parent_PID)
	    end;
	disconnect ->
	    ?WRITE_CONNECTION("Connection unexpectantly closed, logging out user.~n", [], "D"),
	    case package_handler:logout(User) of 
		ok -> 
		    ok;
		{error, Error} -> 
		    Parent_PID ! {error, Error},
		    {error, Error}
	    end;
	{error, closed} ->
	    ?WRITE_CONNECTION("Connection unexpentantly closed, logging out user. ~n", [], "D"),
	    case package_handler:logout(User) of
		{error, no_user} ->
		    ok;
		{error, Error} ->
		    Parent_PID ! {error, Error};
		{ok} ->
		    Parent_PID ! disconnect
	    end

    after 5000 -> 
	    connector_handler(Sock, ID, Timeouts, User, Parent_PID)
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
    %% LOGIN 
    ?assertMatch({ok, _}, connect_send_and_receive({?LOGIN, ["pelle", "asd"]}, ?PORT)),
    %% ERROR
    ?assertMatch({error, timeout}, connect_send_and_receive({?ERROR, ["Fake_error"]}, ?PORT)),
    %% LOGIN AND INIT BOOK
    ?assertMatch({ok, _}, connect_send_and_receive_list([{?LOGIN, ["pelle", "asd"]}, 
							     {?INIT_BOOK, ["1"]}], ?PORT)),

    %% LOGIN, INIT BOOK AND ABORT
    ?assertMatch({ok, _}, connect_send_and_receive_list([{?LOGIN, ["pelle", "asd"]},
							 {?INIT_BOOK, ["1"]},
							 {?ABORT_BOOK, []}], ?PORT)),

    %% LOGIN, INIT BOOK AND FIN
    ?assertMatch({ok, _}, connect_send_and_receive_list([{?LOGIN, ["pelle", "asd"]},
							 {?INIT_BOOK, ["1"]},
							 {?FIN_BOOK, []}], ?PORT)),

    %% REQ AIRPORTS
    ?assertMatch({ok, _}, connect_send_and_receive({?REQ_AIRPORTS,             []},   ?PORT)),

    %% REQ CONNECTING AIRPORTS
    ?assertMatch({ok, _}, connect_send_and_receive({?REQ_AIRPORTS,             ["1"]},   ?PORT)),

    %% SEARCH ROUTE
    ?assertMatch({ok, _}, connect_send_and_receive({?SEARCH_ROUTE,             []},   ?PORT)),

    %% REQ FLIGHT DETAILS
    ?assertMatch({ok, _}, connect_send_and_receive({?REQ_FLIGHT_DETAILS,       []},   ?PORT)),

    %% LOGIN AS ADMIN AND REQ FLIGHT DETAILS
    ?assertMatch({ok, _}, connect_send_and_receive({?REQ_SEAT_SUGGESTION,      []},   ?PORT)),

    %% REQ SEAT MAP
    ?assertMatch({ok, _}, connect_send_and_receive({?REQ_SEAT_MAP,             []},   ?PORT)).
    
%% sequential_stress_test() ->
%%     timer:sleep(2000),
%%     Login_info_list = [[User, Pass] || User <- ["Carl", "Lucas", "Oskar", "Erik", "Andreas", "Wentin"], Pass <- ["hej", "hehe", "asd", "asdasd", "rp", "asd"]],
%%     [?assertMatch({ok, _}, connect_send_and_receive({?LOGIN, Login_info},   ?PORT)) || Login_info <- Login_info_list].

%% concurrent_stress_test() ->
%%     timer:sleep(2000),
%%     Login_info_list = [[User, Pass] || User <- ["Carl", "Lucas", "Oskar", "Erik", "Andreas", "Wentin"], Pass <- ["hej", "hehe", "asd", "asdasd", "rp", "asd"]],
%%     ParentPID = self(),
%%     [spawn(fun() -> 
%% 		   timer:sleep(random:uniform(1000)), 
%% 		   ParentPID ! server_utils:connect_send_and_receive({?LOGIN, Login_info}, ?PORT) end) 
%%      ||	Login_info <- Login_info_list],
%%     Answers = [receive X -> X end || _ <- Login_info_list],
%%     io:fwrite("~p~n", Answers),
%%     Answers_ok = [{Status, Body} || {Status, Body} <- Answers, Status =:= ok],
%%     ?assertEqual(length(Login_info_list), length(Answers_ok)).

%% stop_test() ->    
%%     server_utils:start(?ALT_PORT),
%%     ?assertMatch({error, timout}, connect_send_and_receive({?TERMINATE_SERVER, []}, ?ALT_PORT)).
     

%% Function to close server, tests following this one are to test a closed server.
%% stop_server_test() ->
%%     server_utils:stop_server().

%% no_server_test() ->
%%     %% send before opening
%%     ?assertMatch({error, econnrefused}, connect_send_and_receive({?HEARTBEAT}, ?PORT)),
%%     ?assertMatch({error, econnrefused}, connect_send_and_receive({?HEARTBEAT}, ?ALT_PORT)).
