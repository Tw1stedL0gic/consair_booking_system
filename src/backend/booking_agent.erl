%% ------------------------
%% @title Name Of This File
%% @version 1.0.0
%% {@date}
%% @author Arthur O'disfile
%% @doc Description of this file and a quick rundown of what the
%% user will use it for. 
%% @end
%% ------------------------

-module(booking_agent).
%%-export([loop/1, get/1, login/1, validate/1, heartbeat/1, disconnect/1]).
-export([]).

	%flight_info_by_flightId
	%passenger_info_by_flightNr
	%number_seat_avail
	%book_rate

%%---------------------------------------------------------------------%%

login() ->
    ok.

%%---------------------------------------------------------------------%%

disconnect(User) ->
    %% check if User is currently locking a seat and unlock it. 
    ok.

%%---------------------------------------------------------------------%%

%% Returns a list of all airports. 
%% Output: List of airport tuples. 
%% Example: [{100, "ARN", "Arlanda"}, 
%%           {101, "LAX", "Los Angeles International Airport"}]

airport_list() ->
    ok. 

%% In case of departure airport, return all airports which that airport
%% has a route to. 
%% Input: Airport ID
%% Example: 100
%% Output: List of airport tuples ({id, iata, airporrt_name}).
%% Example: [{101, "LAX", "Los Angeles Internation Airport"}]

airport_list(Airport) ->
    ok.

%%---------------------------------------------------------------------%%

%% @doc - Returns a list of flights between the two airports on that day. 
%% Output: List of fligth tuples excluding arrival_date, flight_id and price. 
%% ({id, airport, arrival_point, departure_date, flight_id, price})
%% Example: [{1111,
%%            {100, "ARN", "Arlanda"},
%%            {101, "LAX", "Los Angeles International Airport"},
%%            {2015,12,30},{11,59,59}}},
%%           {1112,
%%            {100, "ARN", "Arlanda"},
%%            {101, "LAX", "Los Angeles International Airport"},
%%            {{2015,12,30},{20,10,08}}}]

route_search(airport, arrival_point, departure_date) ->
    %% search through database for route
    %% check if route exists
    %% check if date fits
    ok.

%%---------------------------------------------------------------------%%

%% @doc Returns all information about flight.
%% Input:  Flight ID
%% Example: 100
%% Output: Flight tuple
%% Example: {1112,
%%           {100, "ARN", "Arlanda"},
%%           {101, "LAX", "Los Angeles International Airport"},
%%           {{2015,12,30},{20,10,08}},
%%           {{2015,12,31},{10,32,00}},
%%           SEAT_TABLE,
%%           ARNLAX120}

flight_details(Flight) ->
    %% return all information about flight 
    ok.

%%---------------------------------------------------------------------%%

%% @doc Returns information about seat(s). 
%% When inputting a list of seat IDs, the output will be about the included seats. When inputting a row id, all seats in that row will be returned. When inputting a column id, all the seats in that column will be returned. 
%% User information about lock is 0 = avail, 1 = reserved, 2 = reserved. 
%% Input: Flight ID, Seat ID list OR Flight ID, Seat Column OR Flight ID, Seat Row. 
%% Example: {101, A23}
%% Output: Seat tuple ({id, flights, class, user, window, aisle, row, col, lock_s})
%% Example: [{A23,
%%            101,
%%            1, 
%%            carl,
%%            0,
%%            1,
%%            A,
%%            23,
%%            2},
%%           {B23,
%%            101,
%%            1, 
%%            carl,
%%            0,
%%            1,
%%            B,
%%            23,
%%            2}, ... ]

seat_avaiability({Flight, Seat}, user) ->
    ok. 

%% @doc Returns information about seat(s), including admin info. 
%% When inputting a list of seat IDs, the output will be about the included seats. When inputting a row id, all seats in that row will be returned. When inputting a column id, all the seats in that column will be returned. 
%% Admin info about lock is 0 = avail, 1 = locked, 2 = reserved). 
%% Input: Flight ID, Seat ID list OR Flight ID, Seat Column OR Flight ID, Seat Row. 
%% Example: {101, [A23, B23, C23, D23]}
%% Output: Seat tuple list ({id, flights, class, user, window, aisle, row, col, lock_s})
%% Example: Same as above

seat_avaiability({Flight, Seat}, admin) ->
    ok. 

%%---------------------------------------------------------------------%%

%% @doc An algorithmic solution to finding a good seat depending on
%% preferences. Preferences can include class (first, business,
%% economy), seat type (window, middle, aisle), group size, location
%% in plane (front, back, emergency exit)

suggest_seat() ->
    %% implement preferences
    ok. 

%%---------------------------------------------------------------------%%

%% @doc Start the booking process. This will lock the seat(s) to be
%% booked and wait for payment. Time of seat locking will be recorded
%% to make sure that a crash does not mean a endlessly locked seat.

start_booking(User, Flight, seat) ->
    %% record time of seat locking so that in case of crash, it can be cleared
    ok.

%%---------------------------------------------------------------------%%

%% @doc Finalize the booking process. Accept payment and change seat from locked to book. 

finalize_booking(User, Flight, seat) ->
    ok.

%%---------------------------------------------------------------------%%

receipt(User) ->
    ok.

%%---------------------------------------------------------------------%%

abort_booking(User, Flight, seat) ->
    ok. 

%%---------------------------------------------------------------------%%

