-module(package_handler).			
-export([handle_package/1]).

%% Package IDs
%% REQ = Request, RESP = Respond
-define(ClientHandshake,        1)
-define(ServerHandshake,        2)
-define(Heartbeat,              3)
-define(Disconnect,             4)
-define(REQAirportList,         5)
-define(RESPAirportList,        6)
-define(REQRouteSearch,         7)
-define(RESPRouteSearch,        8)
-define(REQFlightDetails,       9)
-define(RESPFlightDetails,     10)
-define(REQSeatSuggestion,     11)
-define(RESPSeatSuggestion,    12)
-define(REQStartBooking,       13)
-define(RESPStartBooking,      14)
-define(REQFinalizeBooking,    15)
-define(RESPFinalizeBooking,   16)
-define(REQReceipt,            17)
-define(RESPReceipt,           18)
-define(AbortBooking,          19)
-define(Error,                 20)


%% @doc - A package handler which translates the received package from
%% bitstring to a tuple, calls the appropriate function, and
%% translates the answer back to bitstring.

handle_package(Package) ->
    handle_package(translate_package);
handle_package({?ClientHandshake, Message}) -> %% ID 1 - Handshake
    %% grants either user or admin privilege alternatively a failure message in case handshake didn't work. 
    %% failed if username or password is incorrect. 
    
    %% 0x00 - user
    %% 0x10 - admin
    %% 0xff - failed

    <<?ServerHandshake>>;
handle_package({?Heartbeat, Message}) -> %% ID 7 - Disconnect
    ok; 
handle_package({?Disconnect, Message}) -> %% ID N - Request airport list
    booking_agent:airport_list();
handle_package({?REQAirportList, Message}) -> %% ID N - Request route search
    booking_agent:route_search(Message),
    <<?RESPAirportList>>
handle_package({?REQFlightDetailst, Message}) -> %% ID N - Request flight details
    booking_agent:flight_details(Message),
    <<?RESPFlightDetails>>; %% ID 2 - Respond flight details
handle_package({?REQSeatAvailability, Message}) -> %% ID N - Request seat availability
    booking_agent:seat_availability(Message),
    <<?RESPSeatAvailability>>; %% ID 4 - Response to booking
handle_package({?REQSeatSuggestion, Message}) -> %% ID N - Request seat suggestion
    booking_agent:suggest_seat(),
    <<?RESPSeatSuggestion>>; %% ID 6 - Response to login
handle_package({?REQStartBooking, Message}) -> %% ID N - Request start booking
    booking_agent:start_booking(),
    <<?RESPStartBooking>>;
handle_package({?REQFinalizeBooking, Message}) -> %% ID N - Request finalize booking
    booking_agent:finalize_booking(),
    <<?RESPFinalizeBooking>>;
handle_package({?REQReceipt, Message}) -> %% ID N - Request receipt
    booking_agent:receipt() ->
    <<?SENDReceipt>>;
handle_package({?AbortBooking, Message}) -> %% ID N - Abort booking
    ok.

%% Translates from a bitstring to a tuple with ID and message

translate_package(<<ID:8/unsigned>>) ->
    {ID};
translate_package(<<ID:8/unsigned, Message/binary>>) ->			
    %% convert Package into a format that 
    %% message handler can read, also make
    %% sure that package is correct
    {ID, Message},
    end;

translate_package({ID}) ->
    package;
translate_package({ID, Message}) ->
    package.
	
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
