-module(package_handler).			
%-export([handle_package/2]).
-compile(export_all).
-include_lib("eunit/include/eunit.hrl").

-define(RegExpSeperator, "&"). %% Needs to be enclosed in quotes.

%% Package IDs
%% REQ = Request, RESP = Respond
-define(ClientLogin,            1).
-define(ServerLogin,            2).
-define(Heartbeat,              3).
-define(Disconnect,             4).
-define(REQAirportList,         5).
-define(RESPAirportList,        6).
-define(REQRouteSearch,         7).
-define(RESPRouteSearch,        8).
-define(REQFlightDetails,       9).
-define(RESPFlightDetails,     10).
-define(REQSeatLock,          100).
-define(RESPSeatLock,         101).
-define(REQSeatSuggestion,     11).
-define(RESPSeatSuggestion,    12).
-define(REQInitBooking,        13).
-define(RESPInitBooking,       14).
-define(REQFinalizeBooking,    15).
-define(RESPFinalizeBooking,   16).
-define(REQReceipt,            17).
-define(RESPReceipt,           18).
-define(AbortBooking,          19).
-define(Error,                 20).


%% @doc - A package handler which translates the received package from
%% bitstring to a tuple, calls the appropriate function, and
%% translates the answer back to bitstring.

handle_package(Package, User) when is_bitstring(Package) ->
    handle_package(translate_package(Package), User);
handle_package({?ClientLogin, [Username, Password]}, User) -> %% ID 1 - Handshake
    %% grants either user or admin privilege alternatively a failure message in case handshake didn't work. 
    %% failed if username or password is incorrect. 
    
    %% 0x00 - user
    %% 0x10 - admin
    %% 0xff - failed
    
    case booking_agent:login(Username, Password) of
	user ->
	    {ok, {user, translate_package({?ServerLogin, ["User"]})}};
	admin ->
	    {ok, {admin, translate_package({?ServerLogin, ["Admin"]})}};
	error ->
	    {error, loginError}
    end;
  
handle_package({?Heartbeat, _}, _) -> 
    translate_package(?Heartbeat); 

handle_package({?Disconnect}, User) -> 
    case booking_agent:disconnect(User) of
	ok ->
	    {ok};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQAirportList}, User) ->
    case booking_agent:airport_list() of
	{ok, AirportList} ->
	    {ok, translate_package({?RESPAirportList, 
				    booking_agent:airport_list()})};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQAirportList, Airport_ID}, User) -> 
    case booking_agent:airport_list() of
	{ok, AirportList} ->
	    {ok, translate_package({?RESPAirportList, 
				    booking_agent:airport_list(Airport_ID)})};
	{error, Error} ->
	    {error, Error}
    end;
	
handle_package({?REQRouteSearch, [Airport_A, Airport_B, Year, Month, Day]}, User) ->
    case booking_agent:flight_details(Airport_A, Airport_B, {Year, Month, Day}) of
	{ok, FlightList} ->
	    {ok, translate_package({?RESPRouteSearch, 
				    lists:map(fun basic_flight_tuple_to_list/1, FlightList)})};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQFlightDetails, Flight_ID}, admin) -> 
    case booking_agent:flight_details(Flight_ID) of
	{ok, Flight} ->
	    {ok, translate_package({?RESPFlightDetails,
				    admin_flight_tuple_to_list(Flight)})};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQFlightDetails, Flight_ID}, User) -> 
    case booking_agent:flight_details(Flight_ID) of
	{ok, Flight} ->
	    {ok, translate_package({?RESPFlightDetails,
				    flight_tuple_to_list(Flight)})};
	{error, Error} ->
	    {error, Error}
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
	{error, Error} ->
	    {error, Error}
    end;


handle_package({?REQSeatLock, Seat_ID}, User) ->
    case booking_agent:seat_lock(Seat_ID, User) of
	{ok, Lock} ->
	    {ok, translate_package({?RESPSeatLock, Lock})};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQSeatSuggestion, Message}, User) -> 
    booking_agent:suggest_seat(),
    <<?RESPSeatSuggestion>>; 

handle_package({?REQInitBooking, Message}, User) ->
    booking_agent:start_booking(),
    <<?RESPInitBooking>>;

handle_package({?REQFinalizeBooking, Message}, User) -> 
    booking_agent:finalize_booking(),
    <<?RESPFinalizeBooking>>;

handle_package({?REQReceipt, Message}, User) -> 
    booking_agent:receipt(),
    <<?RESPReceipt>>;

handle_package({?AbortBooking, Message}, User) -> 
    ok;


handle_package({ID, Message}, _) ->
    {error, wrongMessageFormat}.

%% admin_handle_package(<<Package>>) ->
%%     admin_handle_package(translate_package(<<Package>>));
%% admin_handle_package({?REQFlightDetails, Message}) ->

%% admin_handle_package({?REQSeatSuggestion, Message}) ->
%%     booking_agent:seat_availability(Message, admin).

%% Translates from ID and message or only ID to a regexp

translate_package({ID}) ->
    list_to_binary(integer_to_list(ID));
translate_package({ID, Message}) ->
    list_to_binary(list_to_regexp(lists:append([integer_to_list(ID)], Message), ?RegExpSeperator));

%% Translates from a regexp to a tuple with ID and message

translate_package(Message) ->
    [Message_ID | Message_list] = lists:map(fun binary_to_list/1, lists:droplast(re:split(Message, ?RegExpSeperator))),
    {list_to_integer(Message_ID), Message_list}.


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


airport_tuple_to_list({Airport_ID, IATA, Name}) ->
    [Airport_ID, IATA, Name].

basic_flight_tuple_to_list({Flight_ID, Airport_A, Airport_B, {{Year, Month, Day},{Hour, Minute, Second}}}) ->
    lists:append(lists:append(lists:append([Flight_ID], 
					   airport_tuple_to_list(Airport_A)), 
			      airport_tuple_to_list(Airport_B)), 
		 [Year, Month, Day, Hour, Minute, Second]).

flight_tuple_to_list({Flight_ID, Airport_A, Airport_B, Seat_list, {{Year_D, Month_D, Day_D},{Hour_D, Minute_D, Second_D}}, {{Year_A, Month_A, Day_A},{Hour_A, Minute_A, Second_A}}}) ->
    lists:append(lists:append(lists:append(lists:append([Flight_ID],
							airport_tuple_to_list(Airport_A)), 
					   airport_tuple_to_list(Airport_B)),
			      lists:foldr(fun lists:append/2, [],
					  lists:map(fun seat_tuple_to_list/1, Seat_list))),
		 [Year_D, Month_D, Day_D, Hour_D, Minute_D, Second_D, Year_A, Month_A, Day_A, Hour_A, Minute_A, Second_A]).

admin_flight_tuple_to_list({Flight_ID, Airport_A, Airport_B, Seat_list, {{Year_D, Month_D, Day_D},{Hour_D, Minute_D, Second_D}}, {{Year_A, Month_A, Day_A},{Hour_A, Minute_A, Second_A}}}) ->
    lists:append(lists:append(lists:append(lists:append([Flight_ID],
							airport_tuple_to_list(Airport_A)), 
					   airport_tuple_to_list(Airport_B)),
			      lists:foldr(fun lists:append/2, [], 
					  lists:map(fun admin_seat_tuple_to_list/1, Seat_list))),
		 [Year_D, Month_D, Day_D, Hour_D, Minute_D, Second_D, Year_A, Month_A, Day_A, Hour_A, Minute_A, Second_A]).

seat_tuple_to_list({Seat_ID, Flight_ID, Class, _, Window, Aisle, Row, Col, Price, Lock_s}) ->
    [Seat_ID, Flight_ID, 
     Class, Window, Aisle, 
     Row, Col, Price, 
     (case Lock_s of 
	  0 -> 0;
	  1 -> 1;
	  2 -> 1 end)];
seat_tuple_to_list(_) ->
    {error, wrongFormat}.

admin_seat_tuple_to_list({Seat_ID, Flight_ID, Class, User, Window, Aisle, Row, Col, Price, Lock_s}) ->
    [Seat_ID, Flight_ID, 
     Class, User, Window, Aisle, 
     Row, Col, Price, Lock_s];
admin_seat_tuple_to_list(_) ->
    {error, wrongFormat}.




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
		 airport_tuple_to_list(Airport_A)),

    ?assertMatch([3, 2, "ARN", "Arlanda Airport", 3, "LAX", "Los Angeles International Airport", 2015, 05, 21, 13, 25, 00],
		 basic_flight_tuple_to_list({3, Airport_A, Airport_B, {{2015, 05, 21},{13, 25, 00}}})),


    ?assertMatch(["A32", 2, 1, 1, 0, 32, "A", 500, 1],
		 seat_tuple_to_list(hd(Seat_list))),

    ?assertMatch(["A32", 2, 1, "Carl", 1, 0, 32, "A", 500, 2],
		 admin_seat_tuple_to_list(hd(Seat_list))),

    ?assertMatch([3, 2, "ARN", "Arlanda Airport", 3, "LAX", "Los Angeles International Airport", "A32", 2, 1, 1, 0, 32, "A", 500, 1, "B32", 2, 1, 0, 1, 32, "B", 500, 0, "C2", 2, 2, 0, 0, 2, "C", 1200, 1, 2015, 5, 21, 13, 25, 0, 2015, 5, 22, 1, 21, 12],
       flight_tuple_to_list({3, Airport_A, Airport_B, Seat_list, {{2015, 05, 21},{13, 25, 00}}, {{2015, 05, 22},{01, 21, 12}}})),

    ?assertMatch([3, 2, "ARN", "Arlanda Airport", 3, "LAX", "Los Angeles International Airport", "A32", 2, 1, "Carl", 1, 0, 32, "A", 500, 2, "B32", 2, 1, "Carl", 0, 1, 32, "B", 500, 0, "C2", 2, 2, "Andreas", 0, 0, 2, "C", 1200, 1, 2015, 5, 21, 13, 25, 0, 2015, 5, 22, 1, 21, 12],
       admin_flight_tuple_to_list({3, Airport_A, Airport_B, Seat_list, {{2015, 05, 21},{13, 25, 00}}, {{2015, 05, 22},{01, 21, 12}}})).

