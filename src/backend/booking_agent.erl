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

airport_list() ->
    ok. 

%%---------------------------------------------------------------------%%

%% @doc - Returns a list of all available flights

flight_list() ->
    %% return list of flights with departure time that has not passed
    ok.

%%---------------------------------------------------------------------%%

route_search(route) ->
    %% search through database for route
    %% check if route exists
    %% check if date fits
    %% check if not full
    %% potential pathfinding
    ok.

%%---------------------------------------------------------------------%%

%% @doc TODO: add documentation
%%-spec set() -> ok when 
%%      Ref::{integer(),string()}.

flight_details(Flight) ->
    %% return information about flight 
    ok.

%%---------------------------------------------------------------------%%

seat_avaiability(Seat) ->
    ok. 

%%---------------------------------------------------------------------%%

suggest_seat() ->
    %% implement preferences
    ok. 

%%---------------------------------------------------------------------%%

start_booking() ->
    

start_booking(Flight, seat) ->
    %% record time of seat locking so that in case of crash, it can be cleared
    ok.

%%---------------------------------------------------------------------%%

finalize_booking() ->
    ok.

%%---------------------------------------------------------------------%%

receipt() ->
    ok.

%%---------------------------------------------------------------------%%

abort_booking() ->
    ok. 

%%---------------------------------------------------------------------%%
