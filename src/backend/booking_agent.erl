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
-compile(export_all).
%%-export([loop/1, get/1, login/1, validate/1, heartbeat/1, disconnect/1]).


	%flight_info_by_flightId
	%passenger_info_by_flightNr
	%number_seat_avail
	%book_rate

%%---------------------------------------------------------------------%%

%% @doc - Login to server. 
%% Input: Username, password
%% Example: sk8erboi, iluvmileycyrus
%% Ouput: Failed or user level (error, user, admin)

login(Username, Password) ->
    case Username =:= Password of
	true ->
	    admin;
	_ ->
	    user
    end.

%%---------------------------------------------------------------------%%

%% @doc - Disconnect user from server. Unlock any seats being locked by 
%% this user. 
%% Input: Username
%% Output: ok / {error, Error} 

disconnect() ->
    %% check if User is currently locking a seat and unlock it. 
    %% Potential error: Database could not unlock?
    ok.	    

%%---------------------------------------------------------------------%%

%% Returns a list of all airports. 
%% Output: List of airport tuples or error.
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
%% Input: Departure airport ID, arrival airport ID, departure date.
%% Example: 100, 101, {{2015,12,30},{10,10,10}}
%% Output: List of fligth tuples excluding arrival_date, flight_id and price. 
%% ({id, airport, arrival_point, departure_date, flight_id, price})
%% Example: [{1111,
%%            {100, "ARN", "Arlanda"},
%%            {101, "LAX", "Los Angeles International Airport"},
%%            {{2015,12,30},{11,59,59}}},
%%           {1112,
%%            {100, "ARN", "Arlanda"},
%%            {101, "LAX", "Los Angeles International Airport"},
%%            {{2015,12,30},{20,10,08}}}]

route_search(airport, arrival_point, {{Year, Month, Day},_}) ->
    %% search through database for route
    %% check if route exists
    %% check if date fits
    ok.

%%---------------------------------------------------------------------%%

%% @doc Returns all information about flight.
%% Input:  Flight ID
%% Example: 100
%% Output: Flight tuple ({Flight id, airport_a_id, airport_b_id, departure_date, arrival_date, seat_table, flight number})
%% Example: {1112,
%%           {100, "ARN", "Arlanda"},
%%           {101, "LAX", "Los Angeles International Airport"},
%%           {{2015,12,30},{20,10,08}},
%%           {{2015,12,31},{10,32,00}},
%%           SEAT_TABLE,
%%           ARNLAX120}

flight_details(Flight) ->
    %% return all information about flight 
    %% In this function, booked and locked seats are both represented by 1, and available by 0. 
    ok.

%% @doc Returns all information about flight.
%% Input:  Flight ID
%% Example: 100
%% Output: Flight tuple ({Flight id, airport_a_id, airport_b_id, departure_date, arrival_date, seat_table, flight number})
%% Example: {1112,
%%           {100, "ARN", "Arlanda"},
%%           {101, "LAX", "Los Angeles International Airport"},
%%           [carl, oskar, wenting, ...],
%%           {{2015,12,30},{20,10,08}},
%%           {{2015,12,31},{10,32,00}},
%%           SEAT_TABLE,
%%           ARNLAX120}

flight_details(Flight, admin) ->
    %% returns all information, including admin info, about flight.
    %% In this function, seats are shown as available, locked or booked. 
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

seat_avaiability({Flight, Seat}) ->
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

