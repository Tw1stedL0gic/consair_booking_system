-module(server_utils).
-export([translate_package/1, now_as_string_millis/0, list_to_regexp/2, flatten_tuples_to_list/1, connect_send_and_receive_manual/2, connect_send_and_receive_manual/3, connect_send_and_receive/2, connect_send_and_receive/3, connect_send_and_receive_list/2, connect_send_and_receive_list/3, reload_code/0,reload_code/1, stop_server/0, stop_server/1]).
-define(REG_EXP_SEPERATOR, "&"). %% must be enclosed in quotes
-define(PORT, 53535).
-define(CONNECTIONOPTIONS, [binary, {packet, 0}, {active, false}]).
-define(LOGIN,                         1).
-define(DISCONNECT,                    4).
-define(TERMINATE_SERVER,             24).
-define(RELOAD_CODE,                  26).
-define(Admin_login, <<"1&1&carl&asdasd&\n">>).
-define(Terminate_server, <<"24&1&\n">>).


%%---------------------------------------------------------------------%%

translate_package({ID}) ->
    list_to_binary(list_to_regexp([integer_to_list(ID) | [now_as_string_millis()]], ?REG_EXP_SEPERATOR));
translate_package({ID, Message}) ->
    list_to_binary(list_to_regexp([integer_to_list(ID) | [now_as_string_millis() | Message]], ?REG_EXP_SEPERATOR));

%% Translates from a regexp to a tuple with ID and message

translate_package(Message) ->
    [Message_ID | [Timestamp | Message_list]] = lists:map(fun binary_to_list/1, lists:droplast(re:split(Message, ?REG_EXP_SEPERATOR))),
    case Message_list of
	[] -> {list_to_integer(Timestamp), {list_to_integer(Message_ID)}};
	_  -> {list_to_integer(Timestamp), {list_to_integer(Message_ID), Message_list}}
    end.

%%---------------------------------------------------------------------%%
    
now_as_string_millis() ->
    {Mega_S, S, Micro_S} = now(),
    lists:append(lists:append(integer_to_list(Mega_S), integer_to_list(S)), integer_to_list(Micro_S div 1000)).  

%%---------------------------------------------------------------------%%

list_to_regexp([Tail | []], _) ->
    string:concat(
      case is_integer(Tail) of
	  true -> integer_to_list(Tail);
	  _ -> Tail
      end,
      "&\n");

list_to_regexp([Head | Tail], Seperator) ->
    string:concat(string:concat(
		    case is_integer(Head) of 
			true -> integer_to_list(Head); 
			_ -> Head 
		    end,
		    Seperator), list_to_regexp(Tail, Seperator)).

%%---------------------------------------------------------------------%%

flatten_tuples_to_list(Tuple) ->
    flatten_tuples_to_list([Tuple], []).

flatten_tuples_to_list([], Acc) ->
    Acc;

flatten_tuples_to_list([Head | Tuple_list], Acc) when is_tuple(Head)->
    flatten_tuples_to_list(lists:append(tuple_to_list(Head), Tuple_list), Acc);

flatten_tuples_to_list([Head | Tuples_list], Acc) ->
    flatten_tuples_to_list(Tuples_list, lists:append(Acc, [Head])).

%%---------------------------------------------------------------------%%

connect_send_and_receive_manual(Message, Port) ->
    connect_send_and_receive_manual(Message, "localhost", Port).

connect_send_and_receive_manual(Message, IP, Port) ->
    case gen_tcp:connect(IP, Port, ?CONNECTIONOPTIONS) of
	{ok, Sock} ->
	    gen_tcp:send(Sock, Message),	    
	    case gen_tcp:recv(Sock, 0, 1000) of
		{ok, Response} -> 
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    Response;
		{error, Error} -> 
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    {error, Error};
		Response -> 
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    Response
	    end;
	{error, Error} -> {error, Error}
    end.

connect_send_and_receive(Message, Port) ->
    connect_send_and_receive(Message, "localhost", Port).

connect_send_and_receive(Message, IP, Port) ->
    case gen_tcp:connect(IP, Port, ?CONNECTIONOPTIONS) of
	{ok, Sock} ->
	    gen_tcp:send(Sock, translate_package(Message)),	    
	    case gen_tcp:recv(Sock, 0, 1000) of
		{ok, Response} -> 
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    Response;
		{error, Error} -> 
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    {error, Error};
		Response -> 
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    Response
	    end;
	{error, Error} -> {error, Error}
    end.

connect_send_and_receive_list(Message_list, Port) when is_integer(Port) ->
    connect_send_and_receive_list(Message_list, "localhost", Port).

connect_send_and_receive_list(Message_list, IP, Port) when is_integer(Port) ->
    case gen_tcp:connect(IP, Port, ?CONNECTIONOPTIONS) of
	{ok, Sock} ->
	    case connect_send_and_receive_list(Message_list, Sock, []) of
		{ok, Response} ->
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    Response;
		{error, Error} ->
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    {error, Error}
	    end;
	{error, Error} -> 
	    {error, Error}
    end;

connect_send_and_receive_list([], _, Acc) when is_list(Acc) ->
    {ok, Acc};

connect_send_and_receive_list([Message | Message_list], Sock, Acc) when is_list(Acc) ->
    gen_tcp:send(Sock, translate_package(Message)),
    case gen_tcp:recv(Sock, 0, 1000) of
	{error, Error} -> 
	    {error, Error};
	{ok, Response} -> 
	    connect_send_and_receive_list(Message_list, Sock, lists:append(Acc, [Response]));
	Response -> 
	    connect_send_and_receive_list(Message_list, Sock, lists:append(Acc, [Response]))
    end.

%%---------------------------------------------------------------------%%

reload_code() ->
    reload_code("localhost").

reload_code(IP) ->
    reload_code(IP, ?PORT).

reload_code(IP, Port) ->
    connect_send_and_receive_list([{?LOGIN, ["carl", "asdasd"]}, {?RELOAD_CODE}], IP, Port).

%%---------------------------------------------------------------------%%

stop_server() ->
    stop_server("localhost").

stop_server(IP) ->
    stop_server(IP, ?PORT).

stop_server(IP, Port) ->
    connect_send_and_receive_list([{?LOGIN, ["carl", "asdasd"]}, {?TERMINATE_SERVER}], IP, Port).

%%---------------------------------------------------------------------%%