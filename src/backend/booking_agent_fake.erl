%% ------------------------
%% @title Name Of This File
%% @version 1.0.0
%% {@date}
%% @author Arthur O'disfile
%% @doc Description of this file and a quick rundown of what the
%% user will use it for. 
%% @end
%% ------------------------

-module(booking_agent_fake).
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
%% Done need som patternmatching to make it work, 1 for admin 0 not admin,[] - no user. 
login(Username, Password) ->
    case {Username, Password} of
	{"carl", "asdasd"} ->       admin;
	{"loser", "notanadmin"} ->  user;
	{"OlleDerpatron","hej"} ->  user;
	{"Kalle","hehe"} ->         admin;
	{"pelle","asd"} ->          user;
	{"sir herp","asd"} ->       admin;
	{"meeeh","asd"} ->          user;
	_ ->                        {error, no_matching_user}
    end.


%%---------------------------------------------------------------------%%

%% @doc - Disconnect user from server. Unlock any seats being locked by 
%% this user. 
%% Input: Username
%% Output: ok / {error, Error} 

    %% check if User is currently locking a seat and unlock it. 
    %% Potential error: Database could not unlock?
    %% {ok,[[{seats,2,2,1,3,0,0,"1","A",1337,0},
    %%   {user,3,"Kalle","hehe",2,"kalle@derp.se"}]]}

disconnect(_User) ->
    ok.

%%---------------------------------------------------------------------%%


%% Returns a list of all airports. 
%% Output: List of airport tuples or error.
%% Example: {ok,[{100, "ARN", "Arlanda"}, 
%%           {101, "LAX", "Los Angeles International Airport"}]}
%% DONE SEE get_database:get_airport_from_db()
airport_list() ->
    {ok,  
     [{1, "ARN", "ARLANDA"},
      {2, "ERN", "ARNAAIRPORT UPPSALA"},
      {3, "LAX", "L.A. International Airport"},
      {4, "OSE", "Ostersund flygplats"},
      {5, "DRP", "DerpTown Airport"},
      {6, "TEX", "Texas internatonal Airport"}]}.


%% In case of departure airport, return all airports which that airport
%% has a route to. 
%% Input: Airport ID
%% Example: 100
%% Output: List of airport tuples ({id, iata, airporrt_name}).you dont see id hidden state
%% Example: [{101, "LAX", "Los Angeles Internation Airport"}]
%% DONE SEE get_database:get_airport_from_db_filter(Airport)
airport_list(Airport_ID) ->
    {ok, Airport_list} = airport_list(),
    New_airport_list = lists:keydelete(Airport_ID, 1, Airport_list),
    case New_airport_list of
	Airport_list ->
	    {error, no_such_airport};
	_ ->
	    New_airport_list
    end.
    

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

route_search(Airport_ID, Arrival_point, {{_Year, _Month, _Day},_}) ->
    case Airport_ID of
	Arrival_point ->
	    {error, identical_airport};
	_ ->
	    {ok, search_flight_list(Airport_ID, Arrival_point)}
    end.


search_flight_list(Airport_ID, Arrival_point) ->
    {ok, Airport_list} = airport_list(),
    keytake_recursive(lists:keyfind(Arrival_point, 1, Airport_list), 3, keytake_recursive(lists:keyfind(Arrival_point, 1, Airport_ID), 2, flight_list())). 

keytake_recursive(Key, N, List) ->
    keytake_recursive(Key, N, List, []).

keytake_recursive(Key, N, List, Acc) ->
    case lists:keytake(Key, N, List) of
	false ->
	    case length(Acc) of
		0 -> false;
		_ -> Acc
	    end;
	Tuple ->
	    keytake_recursive(Key, N, List, lists:append(Acc, Tuple))
    end.


flight_list() ->
    {ok, Airport_list} = airport_list(),
    [{1, lists:keyfind(1, 1, Airport_list), lists:keyfind("TEX", 2, Airport_list), 
      {{2015, 10, 03}, {13, 37, 00}}, {{2015, 10, 04}, {01, 01, 10}}, "SKS101"},
     {1, lists:keyfind(1, 1, Airport_list), lists:keyfind("ARN", 2, Airport_list),
      {{2015, 10, 03}, {13, 37, 00}}, {{2015, 10, 04}, {01, 01, 10}}, "SKS102"},
     {1, lists:keyfind(1, 1, Airport_list), lists:keyfind("ERN", 2, Airport_list), 
      {{2015, 10, 03}, {13, 37, 00}}, {{2015, 10, 04}, {01, 01, 10}}, "SKS103"},
     {1, lists:keyfind(1, 1, Airport_list), lists:keyfind("LAX", 2, Airport_list), 
      {{2015, 10, 03}, {13, 37, 00}}, {{2015, 10, 04}, {01, 01, 10}}, "SKS106"}].

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
%% 
flight_details(_Flight) ->
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

flight_details(_Flight, admin) ->
    %% returns all information, including admin info, about flight.
    %% In this function, seats are shown as available, locked or booked. 
    ok.
 

%%---------------------------------------------------------------------%%

%% @doc Returns availability of seat in flight. 
%% Input: Seat_ID, Flight_ID
%% Example: A21, 102
%% Output: Lock status
%% Example: 0, 1 or 2.


seat_lock(Seat_ID) ->
    %% return availability of Seat_ID in Flight_ID
    Data = get_database:get_seats_id_from_db(Seat_ID),
    case Data of
	{_,[{_,_,_,_,_,_,_,_,_,_,Lock_s}]} ->
	    case Lock_s of
		0->
		    0;
		1->
		    1;
		2 ->
		    2
	    end;
	_->
	    {error,no_seat}
    end.
    


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

seat_details([Last_seat_ID]) ->
    [get_database:get_seats_id_from_db(Last_seat_ID)];
seat_details([Head_seat_ID | Tail_seat_ID]) ->
    lists:append([case get_database:get_seats_id_from_db(Head_seat_ID) of
		      {ok,[{DB,S_id,Flight,Class,User,Window,Aisle,Row,Col,Price,2}]} ->    
			  {ok,[{DB,S_id,Flight,Class,User,Window,Aisle,Row,Col,Price,1}]};
		      Seat -> Seat
		  end],						    
		 seat_details(Tail_seat_ID)).

%% @doc Returns information about seat(s), including admin info. 
%% When inputting a list of seat IDs, the output will be about the included seats. When inputting a row id, all seats in that row will be returned. When inputting a column id, all the seats in that column will be returned. 
%% Admin info about lock is 0 = avail, 1 = locked, 2 = reserved). 
%% Input: Flight ID, Seat ID list OR Flight ID, Seat Column OR Flight ID, Seat Row. 
%% Example: {101, [A23, B23, C23, D23]}
%% Output: Seat tuple list ({id, flights, class, user, window, aisle, row, col, lock_s})
%% Example: Same as above

seat_details([Last_seat_ID],admin) ->
    [get_database:get_seats_id_from_db(Last_seat_ID)];
seat_details([Head_seat_ID | Tail_seat_ID],admin) ->
    lists:append([get_database:get_seats_id_from_db(Head_seat_ID)], seat_details(Tail_seat_ID)).

%%---------------------------------------------------------------------%%

%% @doc An algorithmic solution to finding a good seat depending on
%% preferences. Preferences can include class (first, business,
%% economy), seat type (window, middle, aisle), group size, location
%% in plane (front, back, emergency exit)

suggest_seat() ->
    %% implement preferences TBI
    ok. 

%%---------------------------------------------------------------------%%

%% @doc Start the booking process. This will lock the seat(s) to be
%% booked and wait for payment. Time of seat locking will be recorded
%% to make sure that a crash does not mean a endlessly locked seat.

start_booking(User, Seat_id) ->
    %% record time of seat locking so that in case of crash, it can be cleared
    {_,[{_,User_id,_,_,_,_}]} = get_database:get_user_from_db(User),
    get_database:update_seat_lock(Seat_id,1),
    get_database:update_seat_user(Seat_id,User_id),
    ok.
   

%%---------------------------------------------------------------------%%

%% @doc Finalize the booking process. Accept payment and change seat from locked to book. 
%%TODO
finalize_booking(User) ->
    {_,[{_,User_id,_,_,_,_}]} = get_database:get_user_from_db(User),
    get_database:update_seat_lock_user(User_id),    
    ok.

%%---------------------------------------------------------------------%%

receipt(_User) ->
    ok.

%%---------------------------------------------------------------------%%

abort_booking(User) ->
%%gets the user_id from the user database    
    {_,[{_,User_id,_,_,_,_}]} = get_database:get_user_from_db(User),
    get_database:rollback_booking(User_id),    
    ok. 

%%---------------------------------------------------------------------%%

