-module(package_handler).			
%-export([handle_package/2]).
-compile(export_all).
-include_lib("eunit/include/eunit.hrl").

-define(RegExpSeperator, "&"). %% Needs to be enclosed in quotes.

%% Package IDs
%% REQ = Request, RESP = Respond
-define(LOGIN,                         1).
-define(LOGIN_RESP,                    2).
-define(ERROR,                         3).
-define(DISCONNECT,                    4).
-define(INIT_BOOK,                     5).
-define(INIT_BOOK_RESP,                6).
-define(FIN_BOOK,                      7).
-define(FIN_BOOK_RESP,                 8).
-define(ABORT_BOOK,                    9).
-define(REQ_AIRPORTS,                 10).
-define(REQ_AIRPORTS_RESP,            11).
-define(SEARCH_ROUTE,                 12).
-define(SEARCH_ROUTE_RESP,            13).
-define(REQ_FLIGHT_DETAILS,           14).
-define(REQ_FLIGHT_DETAILS_RESP,      15).
-define(REQ_SEAT_SUGGESTION,          16).
-define(RESP_SEAT_SUGGESTION_RESP,    17).
-define(REQ_SEAT_MAP,                 -1).
-define(REQ_SEAT_MAP_RESP,            -1).
-define(TERMINATE_SERVER,             24).

-define(Heartbeat,             -1).
-define(REQSeatLock,          100).
-define(RESPSeatLock,         101).






-define(REQReceipt,            17).
-define(RESPReceipt,           18).





%% @doc - A package handler which translates the received package from
%% bitstring to a tuple, calls the appropriate function, and
%% translates the answer back to bitstring.

handle_package(Package, User) when is_bitstring(Package) ->
    {Timestamp, Translated_package} = translate_package(Package),
    {Timestamp, handle_package(Translated_package, User)};
handle_package({?LOGIN, [Username, Password]}, User) -> %% ID 1 - Handshake
    %% grants either user or admin privilege alternatively a failure message in case handshake didn't work. 
    %% failed if username or password is incorrect. 
    
    %% 0x00 - user
   %% 0x10 - admin
    %% 0xff - failed
    
    case booking_agent:login(Username, Password) of
	{ok, user} when User =:= null ->
	    {ok, {user, translate_package({?LOGIN_RESP, [1]})}};
	{ok, admin} when User =:= null ->
	    {ok, {admin, translate_package({?LOGIN_RESP, [2]})}};
	{ok, user} ->
	    logout(User),
	    {ok, {user, translate_package({?LOGIN_RESP, [1]})}};
	{ok, admin} ->
	    logout(User),
	    {ok, {admin, translate_package({?LOGIN_RESP, [2]})}};
	{error, login_failed} ->
	    {ok, translate_package({?LOGIN_RESP, [3]})}
    end;
  
handle_package({?Heartbeat, _}, _) -> 
    translate_package(?Heartbeat); 

handle_package({?DISCONNECT}, User) -> 
    case logout(User) of
	ok ->
	    {ok, disconnect};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQ_AIRPORTS}, _) -> 
    case booking_agent:airport_list() of
	{ok, Airport_list} ->
	    {ok, translate_package({?REQ_AIRPORTS_RESP, flatten_tuples_to_list(Airport_list)})};
	{error, ERROR} ->
	    {error, ERROR}
    end;

handle_package({?REQ_AIRPORTS, Airport_ID}, _) -> 
    case booking_agent:airport_list(Airport_ID) of
	{ok, Airport_list} ->
	    {ok, translate_package({?REQ_AIRPORTS_RESP, flatten_tuples_to_list(Airport_list)})};
	{error, ERROR} ->
	    {error, ERROR}
    end;
	
handle_package({?SEARCH_ROUTE, [Airport_A, Airport_B, Year, Month, Day]}, _) ->
    case booking_agent:flight_details(Airport_A, Airport_B, {Year, Month, Day}) of
	{ok, Flight_list} ->
	    {ok, translate_package({?SEARCH_ROUTE_RESP, 
				    lists:map(fun flatten_tuples_to_list/1, Flight_list)})};
	{error, ERROR} ->
	    {error, ERROR}
    end;

handle_package({?REQ_FLIGHT_DETAILS, Flight_ID}, admin) -> 
    case booking_agent:flight_details(Flight_ID) of
	{ok, Flight} ->
	    {ok, translate_package({?REQ_FLIGHT_DETAILS_RESP,
				    flatten_tuples_to_list(Flight)})};
	{error, ERROR} ->
	    {error, ERROR}
    end;

handle_package({?REQ_FLIGHT_DETAILS, Flight_ID}, _) -> 
    case booking_agent:flight_details(Flight_ID) of
	{ok, Flight} ->
	    {ok, translate_package({?REQ_FLIGHT_DETAILS_RESP,
				    flatten_tuples_to_list(Flight)})};
	{error, ERROR} ->
	    {error, ERROR}
    end;



handle_package({?REQSeatLock, Seat_ID, Flight_ID}, User) ->
    case booking_agent:seat_lock(Seat_ID, Flight_ID) of
	{ok, Lock} ->
	    case User of
		admin ->
		    {ok, translate_package({?RESPSeatLock, Lock})};
		_ ->
		    {ok, translate_package({?RESPSeatLock, (case Lock of 2 -> 1; _ -> Lock end)})}
	    end;
	{error, ERROR} ->
	    {error, ERROR}
    end;


handle_package({?REQSeatLock, Seat_ID}, User) ->
    case booking_agent:seat_lock(Seat_ID, User) of
	{ok, Lock} ->
	    {ok, translate_package({?RESPSeatLock, Lock})};
	{error, ERROR} ->
	    {error, ERROR}
    end;

handle_package({?REQ_SEAT_SUGGESTION, _Message}, _User) -> 
    {error, not_yet_implemented};
   
handle_package({?INIT_BOOK, [_ | Seat_ID_list]}, User) ->
    case booking_agent:start_booking(User, Seat_ID_list) of
	{ok, {Success_code, Book_time}} ->
	    {ok, translate_package({?INIT_BOOK_RESP, [Success_code, Book_time]})};
	{error, ERROR} ->
	    {error, ERROR}
    end;

handle_package({?FIN_BOOK}, User) -> 
    case booking_agent:finalize_booking(User) of
	{ok, Success_code} ->
	    {ok, translate_package({?FIN_BOOK_RESP, [Success_code]})};
	{error, ERROR} ->
	    {error, ERROR}
    end;
    
handle_package({?REQReceipt, _Message}, _User) -> 
    % booking_agent:receipt(),
    {error, not_yet_implemented};

handle_package({?ABORT_BOOK, _Message}, _User) -> 
    {error, not_yet_implemented};

handle_package({?TERMINATE_SERVER}, admin) ->
    {ok, exit};

handle_package(_, _) ->
    {error, wrong_message_format}.

handle_package(_) ->
    {error, wrong_message_format}.


logout(User) ->
    case User of
	null ->
	    {error, no_user};
	_ ->
	    case booking_agent:disconnect(User) of
		ok ->
		    {ok};
		{error, Error} ->
		    {error, Error}
	    end
    end.


translate_package({ID}) ->
    translate_package({ID, [now_as_string_millis()]});
translate_package({ID, Message}) ->
    list_to_binary(list_to_regexp(lists:append([integer_to_list(ID)], [now_as_string_millis() | Message]), ?RegExpSeperator));

%% Translates from a regexp to a tuple with ID and message

translate_package(Message) ->
    [Message_ID | [Timestamp | Message_list]] = lists:map(fun binary_to_list/1, lists:droplast(re:split(Message, ?RegExpSeperator))),
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

%% airport_tuple_to_list({Airport_ID, IATA, Name}) ->
%%     [Airport_ID, IATA, Name].

%% basic_flight_tuple_to_list({Flight_ID, Airport_A, Airport_B, {{Year, Month, Day},{Hour, Minute, Second}}}) ->
%%     lists:append(lists:append(lists:append([Flight_ID], 
%% 					   airport_tuple_to_list(Airport_A)), 
%% 			      airport_tuple_to_list(Airport_B)), 
%% 		 [Year, Month, Day, Hour, Minute, Second]).

%% flight_tuple_to_list({Flight_ID, Airport_A, Airport_B, Seat_list, {{Year_D, Month_D, Day_D},{Hour_D, Minute_D, Second_D}}, {{Year_A, Month_A, Day_A},{Hour_A, Minute_A, Second_A}}}) ->
%%     lists:append(lists:append(lists:append(lists:append([Flight_ID],
%% 							airport_tuple_to_list(Airport_A)), 
%% 					   airport_tuple_to_list(Airport_B)),
%% 			      lists:foldr(fun lists:append/2, [],
%% 					  lists:map(fun seat_tuple_to_list/1, Seat_list))),
%% 		 [Year_D, Month_D, Day_D, Hour_D, Minute_D, Second_D, Year_A, Month_A, Day_A, Hour_A, Minute_A, Second_A]).

%% admin_flight_tuple_to_list({Flight_ID, Airport_A, Airport_B, Seat_list, {{Year_D, Month_D, Day_D},{Hour_D, Minute_D, Second_D}}, {{Year_A, Month_A, Day_A},{Hour_A, Minute_A, Second_A}}}) ->
%%     lists:append(lists:append(lists:append(lists:append([Flight_ID],
%% 							airport_tuple_to_list(Airport_A)), 
%% 					   airport_tuple_to_list(Airport_B)),
%% 			      lists:foldr(fun lists:append/2, [], 
%% 					  lists:map(fun admin_seat_tuple_to_list/1, Seat_list))),
%% 		 [Year_D, Month_D, Day_D, Hour_D, Minute_D, Second_D, Year_A, Month_A, Day_A, Hour_A, Minute_A, Second_A]).

%% seat_tuple_to_list({Seat_ID, Flight_ID, Class, _, Window, Aisle, Row, Col, Price, Lock_s}) ->
%%     [Seat_ID, Flight_ID, 
%%      Class, Window, Aisle, 
%%      Row, Col, Price, 
%%      (case Lock_s of 
%% 	  0 -> 0;
%% 	  1 -> 1;
%% 	  2 -> 1 end)];
%% seat_tuple_to_list(_) ->
%%     {error, wrong_format}.

%% admin_seat_tuple_to_list({Seat_ID, Flight_ID, Class, User, Window, Aisle, Row, Col, Price, Lock_s}) ->
%%     [Seat_ID, Flight_ID, 
%%      Class, User, Window, Aisle, 
%%      Row, Col, Price, Lock_s];
%% admin_seat_tuple_to_list(_) ->
%%     {error, wrong_format}.




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%                         EUnit Test Cases                                  %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

package_handler_test() ->
    ?assertMatch(true, true).

translate_package_test() ->
    List_of_strings = ["a", "b", "c", "d", "e", "f"],
    List_of_numbers = [1, 2, 3, 4, 5, 6, 7, 8],
    List_of_mixed = ["a", 1, "b", 2, "c", 3, "d", 4],

    ?assertMatch(<<"1&a&b&c&d&e&f&\n">>, translate_package({1, List_of_strings})),
    ?assertMatch(<<"2&1&2&3&4&5&6&7&8&\n">>, translate_package({2, List_of_numbers})),
    ?assertMatch(<<"3&a&1&b&2&c&3&d&4&\n">>, translate_package({3, List_of_mixed})).

tuple_to_list_test() ->
    Airport_A = {2, "ARN", "Arlanda Airport"},
    Airport_B = {3, "LAX", "Los Angeles International Airport"},
    Seat_list = [{"A32", 2, 1, "Carl",    1, 0, 32, "A", 500,  2},
		 {"B32", 2, 1, "Carl",    0, 1, 32, "B", 500,  0},
		 {"C2",  2, 2, "Andreas", 0, 0, 2,  "C", 1200, 1}],

    ?assertMatch([2, "ARN", "Arlanda Airport"], 
		 flatten_tuples_to_list(Airport_A)),

    ?assertMatch([3, 2, "ARN", "Arlanda Airport", 3, "LAX", "Los Angeles International Airport", 2015, 05, 21, 13, 25, 00],
		 flatten_tuples_to_list({3, Airport_A, Airport_B, {{2015, 05, 21},{13, 25, 00}}})),


    ?assertMatch(["A32", 2, 1, 1, 0, 32, "A", 500, 1],
		 flatten_tuples_to_list(hd(Seat_list))),

    ?assertMatch(["A32", 2, 1, "Carl", 1, 0, 32, "A", 500, 2],
		 flatten_tuples_to_list(hd(Seat_list))),

    ?assertMatch([3, 2, "ARN", "Arlanda Airport", 3, "LAX", "Los Angeles International Airport", "A32", 2, 1, 1, 0, 32, "A", 500, 1, "B32", 2, 1, 0, 1, 32, "B", 500, 0, "C2", 2, 2, 0, 0, 2, "C", 1200, 1, 2015, 5, 21, 13, 25, 0, 2015, 5, 22, 1, 21, 12],
       flatten_tuples_to_list({3, Airport_A, Airport_B, Seat_list, {{2015, 05, 21},{13, 25, 00}}, {{2015, 05, 22},{01, 21, 12}}})),

    ?assertMatch([3, 2, "ARN", "Arlanda Airport", 3, "LAX", "Los Angeles International Airport", "A32", 2, 1, "Carl", 1, 0, 32, "A", 500, 2, "B32", 2, 1, "Carl", 0, 1, 32, "B", 500, 0, "C2", 2, 2, "Andreas", 0, 0, 2, "C", 1200, 1, 2015, 5, 21, 13, 25, 0, 2015, 5, 22, 1, 21, 12],
       flatten_tuples_to_list({3, Airport_A, Airport_B, Seat_list, {{2015, 05, 21},{13, 25, 00}}, {{2015, 05, 22},{01, 21, 12}}})).

