-module(server_utils).
-export([translate_package/1, now_as_string_millis/0, list_to_regexp/2, flatten_tuples_to_list/1, connect_send_and_receive_manual/2, connect_send_and_receive_manual/3, connect_send_and_receive/2, connect_send_and_receive/3, connect_send_and_receive_list/2, connect_send_and_receive_list/3, reload_code/0,reload_code/1, stop_server/0, stop_server/1,format_position_as_mod/2, format_position_as_mod/4, fill_with_white_space/2, pass_message_list/2]).

-define(ELEMENT_SEPERATOR, "&"). 
-define(MESSAGE_SEPERATOR, "\n").

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
    list_to_binary(list_to_regexp([integer_to_list(ID) | [now_as_string_millis()]], ?ELEMENT_SEPERATOR));
translate_package({ID, Message}) ->
    list_to_binary(list_to_regexp([integer_to_list(ID) | [now_as_string_millis() | Message]], ?ELEMENT_SEPERATOR));

%% Translates from a regexp to a tuple with ID and message

translate_package(Message) ->
    [Message_ID | [Timestamp | Message_list]] = lists:map(fun binary_to_list/1, lists:droplast(re:split(Message, ?ELEMENT_SEPERATOR))),
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
      ?ELEMENT_SEPERATOR ++ ?MESSAGE_SEPERATOR);

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

flatten_tuples_to_list([Head | Tuple_list], Acc) when is_tuple(Head) ->
    flatten_tuples_to_list(tuple_to_list(Head) ++ Tuple_list, Acc);

flatten_tuples_to_list([Head | Tuple_list], Acc) when is_list(Head) ->
    case io_lib:printable_list(Head) of
	true ->
	    flatten_tuples_to_list(Tuple_list, Acc ++ [Head]);
	_ ->
	    flatten_tuples_to_list(Tuple_list, Acc ++ flatten_tuples_to_list(Head, []))
    end;

flatten_tuples_to_list([Head | Tuple_list], Acc) ->
    flatten_tuples_to_list(Tuple_list, Acc ++ [Head]).

%%---------------------------------------------------------------------%%

format_position_as_mod(N, Mod) ->
    format_position_as_mod(N, Mod, $-, $x).

format_position_as_mod(N, Mod, CharA, CharB) ->
    format_position_as_mod(N, Mod, CharA, CharB, []).

format_position_as_mod(_, Mod, _, _, Acc) when length(Acc) =:= Mod ->
    Acc;

format_position_as_mod(-1, Mod, CharA, CharB, Acc) ->
    format_position_as_mod(0, Mod, CharB, CharA, Acc);

format_position_as_mod(N, Mod, CharA, CharB, Acc) ->
    Position = length(Acc) + 1,
    case (((N-1) rem Mod) + 1) of
	Position ->
	    format_position_as_mod(N, Mod, CharA, CharB, lists:append(Acc, [CharB]));
	_ ->
	    format_position_as_mod(N, Mod, CharA, CharB, lists:append(Acc, [CharA]))
    end.
	
%%---------------------------------------------------------------------%%

fill_with_white_space(String, N) ->	
    lists:append(String, [$  || _ <- lists:seq(1,N-length(String))]).  

%%---------------------------------------------------------------------%%

connect_send_and_receive_manual(Message, Port) ->
    connect_send_and_receive_manual(Message, "localhost", Port).

connect_send_and_receive_manual(Message, IP, Port) ->
    case gen_tcp:connect(IP, Port, ?CONNECTIONOPTIONS) of
	{ok, Sock} ->
	    gen_tcp:send(Sock, Message),	    
	    case gen_tcp:recv(Sock, 0, 500) of
		{ok, Response} -> 
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    {ok, Response};
		{error, Error} -> 
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    {error, Error};
		Response -> 
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    {ok, Response}
	    end;
	{error, Error} -> {error, Error}
    end.

connect_send_and_receive(Message, Port) ->
    connect_send_and_receive(Message, "localhost", Port).

connect_send_and_receive(Message, IP, Port) ->
    case gen_tcp:connect(IP, Port, ?CONNECTIONOPTIONS) of
	{ok, Sock} ->
	    gen_tcp:send(Sock, translate_package(Message)),	    
	    case gen_tcp:recv(Sock, 0, 500) of
		{ok, Response} -> 
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    {ok, Response};
		{error, Error} -> 
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    {error, Error};
		Response -> 
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    {ok, Response}
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
		    {ok, Response};
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
    case gen_tcp:recv(Sock, 0, 500) of
	{error, timeout} ->
	    connect_send_and_receive_list(Message_list, Sock, Acc);
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

pass_message_list([], _) ->
    ok;

pass_message_list([Message | Message_list], PID) ->
    PID ! {ok, Message},
    pass_message_list(Message_list, PID).

%%---------------------------------------------------------------------%%
