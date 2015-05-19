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

%% Returns a list of all airports. 

airport_list() ->
    ok. 

%% In case of entering airport, return all airports which that airport
%% has a route to

airport_list(Airport) ->
    ok.

%%---------------------------------------------------------------------%%

%% @doc - Returns a list of all available flights

flight_list() ->
    %% return list of flights with departure time that has not passed
    ok.

%%---------------------------------------------------------------------%%

%% @doc - Returns a list of flights between the two airports on that day. 

route_search(AirportDeparture, AirportArrival, Date) ->
    %% search through database for route
    %% check if route exists
    %% check if date fits
    ok.

%%---------------------------------------------------------------------%%

%% @doc Returns all information about flight. 

flight_details(Flight) ->
    %% return all information about flight 
    ok.

%%---------------------------------------------------------------------%%

%% @doc Returns whether a seat is booked, locked or avaliable. 

seat_avaiability(Seat) ->
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
