-module(t_server).
-compile(export_all).
-include("server_utils.hrl").
-include_lib("eunit/include/eunit.hrl").

-define(PORT_A, 53535).
-define(PORT_B, 53533).





%%--------------------------------------------------------------%%

startup_test() ->
    %% send before opening
    ?assertMatch({error, econnrefused}, connect_send_and_receive(<<>>, ?PORT_A)),
    ?assertMatch({error, econnrefused}, connect_send_and_receive(<<>>, ?PORT_B)),

    %% open
    spawn(server, start, [?PORT_A]),
    
    ?assertMatch({error, eaddrinuse}, server:start(?PORT_A)), 
    %% fail opening on already open port
    %% ?assertMatch({error, eaddrinuse}, server:start()), 
    %% close
    ?assertMatch(ok, server:stop()).
   
login_test() ->
    %% send before opening
    ?assertMatch({error, econnrefused}, connect_send_and_receive(<<>>, ?PORT_A)),
    ?assertMatch({error, econnrefused}, connect_send_and_receive(<<>>, ?PORT_B)),

    %% open
    spawn(server, start, [?PORT_B]),
    %% send message
    ?assertMatch({ok, _}, connect_send_and_receive(<<>>, ?PORT_B)),
    ?assertMatch({ok, _}, connect_send_and_receive(<<"1&carl&asdasd&\n">>, ?PORT_B)),
    ?assertMatch({ok, _}, connect_send_and_receive(<<"1&hej&fel&\n">>, ?PORT_B)),
    
    
    
    server:stop().
     
