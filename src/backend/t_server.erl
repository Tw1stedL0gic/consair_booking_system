-module(t_server).
-compile(export_all).
-include_lib("eunit/include/eunit.hrl").

-define(PORT_A, 53535).
-define(PORT_B, 53533).
-define(CONNECTIONOPTIONS, [binary, {packet, 0}, {active, false}]).


connect_send_and_receive(Message, Port) ->
    case gen_tcp:connect("localhost", Port, ?CONNECTIONOPTIONS) of
	{ok, Sock} ->
	    gen_tcp:send(Sock, Message),	    
	    case gen_tcp:recv(Sock, 0, 5000) of
		{ok, Response} -> Response;
		Response -> Response
	    end,
	    gen_tcp:send(Sock, <<"4&\n">>),
	    %% [_ | Stringified] = lists:droplast(binary_to_list(Response)),
	    %% Stringified;

	    Response;
	{error, Error} -> {error, Error}
    end.

startup_test() ->
    %% open
    spawn(server, start, []),
    %% fail opening on already open port
    %% ?assertMatch({error, eaddrinuse}, server:start()), 
    %% close
    ?assertMatch(ok, server:stop()).
     
send_test() ->
    %% send before opening
    ?assertMatch({error, econnrefused}, connect_send_and_receive(<<>>, ?PORT_A)),
    ?assertMatch({error, econnrefused}, connect_send_and_receive(<<>>, ?PORT_B)),

    %% open
    spawn(server, start, [?PORT_B]),
    %% send message
    ?assertMatch([_, _, <<"1">>, _], re:split(connect_send_and_receive(<<"1&loser&notanadmin&\n">>, ?PORT_B), "&")),
    ?assertMatch([_, _, <<"2">>, _], re:split(connect_send_and_receive(<<"1&carl&asdasd&\n">>, ?PORT_B), "&")),
    ?assertMatch([_, _, <<"3">>, _], re:split(connect_send_and_receive(<<"1&hej&fel&\n">>, ?PORT_B), "&")),
    

    server:stop().
     
