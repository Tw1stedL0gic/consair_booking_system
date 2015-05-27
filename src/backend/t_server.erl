-module(t_server).
-compile(export_all).
-include("server_utils.hrl").
-include_lib("eunit/include/eunit.hrl").

-define(PORT_A, 53535).
-define(PORT_B, 53533).





%%--------------------------------------------------------------%%

startup_test() ->
    %% send before opening
    ?assertMatch({error, econnrefused}, connect_send_and_receive({?HEARTBEAT}, ?PORT_A)),
    ?assertMatch({error, econnrefused}, connect_send_and_receive({?HEARTBEAT}, ?PORT_B)),

    %% open
    spawn(server, start, [?PORT_A]),
    
    ?assertMatch({error, eaddrinuse}, server:start(?PORT_A)), 
    %% fail opening on already open port
    %% ?assertMatch({error, eaddrinuse}, server:start()), 
    %% close
    ?assertMatch(ok, server:stop()).
   
login_test() ->
    %% send before opening
    ?assertMatch({error, econnrefused}, connect_send_and_receive({?HEARTBEAT}, ?PORT_A)),
    ?assertMatch({error, econnrefused}, connect_send_and_receive({?HEARTBEAT}, ?PORT_B)),

    %% open
    spawn(server, start, [?PORT_B]),
    %% login
    ?assertMatch({ok, _}, connect_send_and_receive({?LOGIN, ["fake", "user"]},   ?PORT_B)),
    ?assertMatch({ok, _}, connect_send_and_receive({?LOGIN, ["carl", "asdasd"]}, ?PORT_B)),
    ?assertMatch({ok, _}, connect_send_and_receive({?LOGIN, ["pelle", "asd"]},   ?PORT_B)),
    
    
    
    server:stop().
     
