-module(package_handler).			
-export([handle_package/1]).

-define(RegExpSeperator, "&"). %% Needs to be enclosed in quotes.

%% Package IDs
%% REQ = Request, RESP = Respond
-define(ClientLogin,        1).
-define(ServerLogin,        2).
-define(Heartbeat,              3).
-define(Disconnect,             4).
-define(REQAirportList,         5).
-define(RESPAirportList,        6).
-define(REQRouteSearch,         7).
-define(RESPRouteSearch,        8).
-define(REQFlightDetails,       9).
-define(RESPFlightDetails,     10).
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

handle_package(Package) ->
    handle_package(translate_package);
handle_package({?ClientLogin, Message}) -> %% ID 1 - Handshake
    %% grants either user or admin privilege alternatively a failure message in case handshake didn't work. 
    %% failed if username or password is incorrect. 
    
    %% 0x00 - user
    %% 0x10 - admin
    %% 0xff - failed

    <<?ServerLogin>>;
handle_package({?Heartbeat, Message}) -> %% ID 7 - Disconnect
    ok; 
handle_package({?Disconnect, Message}) -> %% ID N - Request airport list
    booking_agent:airport_list();
handle_package({?REQAirportList, Message}) -> %% ID N - Request route search
    booking_agent:route_search(Message),
    <<?RESPAirportList>>;
handle_package({?REQFlightDetails, Message}) -> %% ID N - Request flight details
    booking_agent:flight_details(Message),
    <<?RESPFlightDetails>>; %% ID 2 - Respond flight details
%handle_package({?REQSeatAvailability, Message}) -> %% ID N - Request seat availability
%    booking_agent:seat_availability(Message),
%    <<?RESPSeatAvailability>>; %% ID 4 - Response to booking
handle_package({?REQSeatSuggestion, Message}) -> %% ID N - Request seat suggestion
    booking_agent:suggest_seat(),
    <<?RESPSeatSuggestion>>; %% ID 6 - Response to login
handle_package({?REQInitBooking, Message}) -> %% ID N - Request start booking
    booking_agent:start_booking(),
    <<?RESPInitBooking>>;
handle_package({?REQFinalizeBooking, Message}) -> %% ID N - Request finalize booking
    booking_agent:finalize_booking(),
    <<?RESPFinalizeBooking>>;
handle_package({?REQReceipt, Message}) -> %% ID N - Request receipt
    booking_agent:receipt(),
    <<?RESPReceipt>>;
handle_package({?AbortBooking, Message}) -> %% ID N - Abort booking
    ok.

%% Translates from a regexp to a tuple with ID and message

translate_package(Message) ->
    re:split(Message, ?RegExpSeperator);

%% Translates from ID and message or only ID to a regexp

translate_package({ID}) ->
    <<integer_to_list(ID)>>;
translate_package({ID, Message}) ->
    <<list_to_regexp([integer_to_list(ID), Message], ?RegExpSeperator)>>.

list_to_regexp([Tail], _) ->
    string:concat(Tail, "\n");
list_to_regexp([Head | Tail], Seperator) ->
    string:concat(string:concat(Head, Seperator), list_to_regexp(Tail, Seperator)).

	
%% 1: Hämta passagerare (front-end to back-end)
%% 2: Passagerarlista (back-end to front-end)
%% 3: Boka plats
%% 4: Response to #3 (lyckat / misslyckat)
%% 5: Login
%% 6: Response #5
%% 7: Disconnect / Terminera Anslutning
%% 8: Heartbeat
%% 9: Get passenger info (front-end -> back-end)
%% 10: Response #9
