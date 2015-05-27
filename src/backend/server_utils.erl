-module(server_utils).
-export([translate_package/1, now_as_string_millis/0, list_to_regexp/2, flatten_tuples_to_list/1, connect_send_and_receive_manual/2, connect_send_and_receive/2, connect_send_and_receive_list/2]).
-define(REG_EXP_SEPERATOR, "&"). %% must be enclosed in quotes
-define(CONNECTIONOPTIONS, [binary, {packet, 0}, {active, false}]).
-define(DISCONNECT, 4).

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
    
now_as_string_millis() ->
    {Mega_S, S, Micro_S} = now(),
    lists:append(lists:append(integer_to_list(Mega_S), integer_to_list(S)), integer_to_list(Micro_S div 1000)).  

    


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

flatten_tuples_to_list(Tuple) ->
    flatten_tuples_to_list([Tuple], []).

flatten_tuples_to_list([], Acc) ->
    Acc;

flatten_tuples_to_list([Head | Tuple_list], Acc) when is_tuple(Head)->
    flatten_tuples_to_list(lists:append(tuple_to_list(Head), Tuple_list), Acc);

flatten_tuples_to_list([Head | Tuples_list], Acc) ->
    flatten_tuples_to_list(Tuples_list, lists:append(Acc, [Head])).



connect_send_and_receive_manual(Message, Port) ->
    case gen_tcp:connect("localhost", Port, ?CONNECTIONOPTIONS) of
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
    case gen_tcp:connect("localhost", Port, ?CONNECTIONOPTIONS) of
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

connect_send_and_receive_list([Message_list], Port) ->
    case gen_tcp:connect("localhost", Port, ?CONNECTIONOPTIONS) of
	{ok, Sock} ->
	    case connect_send_and_receive_list([Message_list], Sock, []) of
		{ok, Response} ->
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    Response;
		{error, Error} ->
		    gen_tcp:send(Sock, translate_package({?DISCONNECT})),
		    {error, Error}
	    end;
	{error, Error} -> 
	    {error, Error}
    end.

connect_send_and_receive_list([], _, Acc) ->
    {ok, Acc};

connect_send_and_receive_list([{ID, Message} | Message_list], Sock, Acc) ->
    gen_tcp:send(Sock, translate_package({ID, Message})),
    case gen_tcp:recv(Sock, 0, 1000) of
	{error, Error} -> 
	    {error, Error};
	{ok, Response} -> 
	    connect_send_and_receive_list([Message_list], Sock, lists:append(Acc, Response));
	Response -> 
	    connect_send_and_receive_list([Message_list], Sock, lists:append(Acc, Response))
    end.
