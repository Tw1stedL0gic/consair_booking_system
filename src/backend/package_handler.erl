-module(package_handler).			
-export([handle_package/1, admin_handle_package/1]).

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
-define(REQSeatAvailability,  100).
-define(RESPSeatAvailability, 101).
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

handle_package(<<Package>>, User) ->
    handle_package(translate_package(<<Package>>), User);
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
    {ok, translate_package({?RESPAirportList, booking_agent:airport_list()})};

handle_package({?REQAirportList, Airport_ID}, User) -> 
    {ok, translate_package({?RESPAirportList, booking_agent:airport_list(Airport_ID)})};


handle_package({?REQRouteSearch, [Airport_A, Airport_B, Year, Month, Day]}, User) ->
    case booking_agent:flight_details(Airport_A, Airport_B, {Year, Month, Day}) of
	{ok, FlightList} ->
	    {ok, lists:map(basic_flight_tuple_to_list, FlightList)};
	{error, Error} ->
	    {error, Error}
    end;

handel_package({?REQFlightDetails, Message}, admin) -> 
    booking_agent:flight_details(Message);
handel_package({?REQFlightDetails, Message}, User) -> 
    booking_agent:flight_details(Message):

handle_package({?REQSeatAvailability, Message}, User) ->
    booking_agent:seat_availability(Message),
    <<?RESPSeatAvailability>>; 

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

admin_handle_package(<<Package>>) ->
    admin_handle_package(translate_package(<<Package>>));
admin_handle_package({?REQFlightDetails, Message}) ->

admin_handle_package({?REQSeatSuggestion, Message}) ->
    booking_agent:seat_availability(Message, admin).

%% Translates from ID and message or only ID to a regexp

translate_package({ID}) ->
    list_to_binary(integer_to_list(ID));
translate_package({ID, Message}) ->
    list_to_binary(list_to_regexp(lists:append([integer_to_list(ID)], Message), ?RegExpSeperator));

%% Translates from a regexp to a tuple with ID and message

translate_package(Message) ->
    re:split(Message, ?RegExpSeperator).


list_to_regexp([Tail | []], _) ->
    string:concat(Tail, "\n");
list_to_regexp([Head | Tail], Seperator) ->
    string:concat(string:concat(Head, Seperator), list_to_regexp(Tail, Seperator)).


airport_tuple_to_list({ID, IATA, Name}) ->
    [ID, IATA, Name].

basic_flight_tuple_to_list({ID, Airport_A, Airport_B, {{Year, Month, Day},{Hour, Minute, Second}}}) ->
    lists:append(lists:append(lists:append([ID], 
					   airport_tuple_to_list(Airport_A)), 
			      airport_tuple_to_list(Airport_B)), 
		 [Year, Month, Day, Hour, Minute, Second]).


