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
%% Done need som patternmatching to make it work, 1 for admin 0 not admin,[] - no user. 
login(Username, Password) ->
    {_,[{_,_,Name,Pass,Ulvl,_}]} = get_database:get_user_from_db(Username),

    case Username =:= Name of
	true ->
	    case Password =:= Pass of
		true->
		    case Ulvl =:= 1 of
			true ->
			    admin;
			false ->
			    user
		    end;
		false->
		    wrong_password
	    end;
	false ->
	    not_a_user
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

disconnect(Username) ->
    Data  = get_database:get_user_from_seats(Username),
    case Data of
	{_,[[{_,S_id,_,_,_,_,_,_,_,_,Lock_s},{_,_,U_name,_,_,_}]]} ->
	    case U_name =:= Username of
		true ->
		    case Lock_s of
			1 ->
			    get_database:update_seat_lock(S_id,0),
			    ok;
			_ ->
			    ok
		    end;
		_->    
		    ok
	    end;
	_ ->
	    ok
    end.

%%---------------------------------------------------------------------%%


%% Returns a list of all airports. 
%% Output: List of airport tuples or error.
%% Example: {ok,[{100, "ARN", "Arlanda"}, 
%%           {101, "LAX", "Los Angeles International Airport"}]}
%% DONE SEE get_database:get_airport_from_db()
airport_list() ->
    get_database:get_airport_from_db().
    

%% In case of departure airport, return all airports which that airport
%% has a route to. 
%% Input: Airport ID
%% Example: 100
%% Output: List of airport tuples ({id, iata, airporrt_name}).you dont see id hidden state
%% Example: [{101, "LAX", "Los Angeles Internation Airport"}]
%% DONE SEE get_database:get_airport_from_db_filter(Airport)
airport_list(Airport) ->
    get_database:get_airport_from_db_filter(Airport).
    

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

route_search(airport, arrival_point, {{year, month, day},_}) ->
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
%% 
flight_details(Flight) ->
    %% return all information about flight 
    %% In this function, booked and locked seats are both represented by 1, and available by 0. 
    get_database:get_flight_from_db_f(Flight).

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
%%TODO!
flight_details(Flight, admin) ->
    %% returns all information, including admin info, about flight.
    %% In this function, seats are shown as available, locked or booked. 
    get_database:get_flight_from_db_f(Flight).
 

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
	    Lock_s;    
	_ ->
	    {error,no_seat}
    end.
    


%%---------------------------------------------------------------------%%


%% @doc Returns information about seat(s). 
%% When inputting a list of seat IDs, the output will be about the included seats. When inputting a row id, all seats in that row will be returned. When inputting a column id, all the seats in that column will be returned. 
%% User information about lock is 0 = avail, 1 = reserved, 2 = reserved. 
%% Input: Flight ID, Seat ID list OR Flight ID, Seat Column OR Flight ID, Seat Row. 
%% Example: {101, A23}
%% Output: Seat tuple ({id, flights, class, user, window, aisle, row, col, lock_s})
%% Example: [{A23, 101, 1, carl, 0, 1, A, 23, 2},
%%           {B23, 101, 1, carl, 0, 1, B, 23, 2},
%%           ... ]

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

suggest_seat(Flight_ID, Group_size) ->
    find_chain_of_seats(get_database:get_seats_from_flight(Flight_ID), [], -1, Group_size). 

find_chain_of_seats([Seat | Seat_list], Chain_list, Chain_row, Chain_size) ->
    %% extract necessary info
    {_Seat_ID, _Flight_ID, _Class, _User, _Window, _Aisle, Seat_row, _Col, _Price, Lock_s} = Seat, 
    %% check if right size (or zero)
    case lists:sizeof(Chain_list) of 
	Chain_size ->
	    %% finished, return chain
	    Chain_list;
	0 ->
	    %% check seat lock
	    case Lock_s of
		0 ->
		    %% reset including new seat
		    find_chain_of_seats(Seat_list, [Seat], Seat_row, Chain_size);
		_ ->
		    %% reset
		    find_chain_of_seats(Seat_list, [], Seat_row, Chain_size)
	    end;
	_ ->
	    %% check seat lock
	    case Lock_s of
		0 ->
		    %% check if row matches. 
		    case Seat_row of
			Chain_row ->
			    %% append and continue
			    find_chain_of_seats(Seat_list, lists:append(Chain_list, Seat), Chain_row, Chain_size);
			_ -> 
			    %% reset including new seat
			    find_chain_of_seats(Seat_list, [Seat], Seat_row, Chain_size)
		    end;
		_ ->
		    %% reset
		    find_chain_of_seats(Seat_list, [], Seat_row, Chain_size)
	    end
    end.

%%---------------------------------------------------------------------%%

%% @doc Start the booking process. This will lock the seat(s) to be
%% booked and wait for payment. Time of seat locking will be recorded
%% to make sure that a crash does not mean a endlessly locked seat.

start_booking(User, Seat_id) ->
    %% record time of seat locking so that in case of crash, it can be cleared
    {_,[{_,User_id,_,_,_,_}]} = get_database:get_user_from_db(User),
    get_database:update_seat_lock(Seat_id,1),
    get_database:update_seat_user(Seat_id,User_id),
    %% timer:sleep(000)
    %% case get seat user_id
    %%    User -> ok
    %%    - -> Åerror
    timer:sleep(1000),
    Check = get_dabase:get_filter_seats_from_user_id(User_id),
    case Check of
	{ok,[]} ->
	    {error,seat_booked};
	{_,[{_,_,_,_,_,_,_,_,_,_,_}]}->
	    ok
    end,
    ok.


%%---------------------------------------------------------------------%%

%% @doc Finalize the booking process. Accept payment and change seat from locked to book. 
%%TODO
finalize_booking(User) ->
    {_,[{_,User_id,_,_,_,_}]} = get_database:get_user_from_db(User),
    get_database:update_seat_lock_user(User_id),    
    ok.

%%---------------------------------------------------------------------%%

receipt(user) ->
    ok.

%%---------------------------------------------------------------------%%

abort_booking(User) ->
    %%gets the user_id from the user database    
    {_,[{_,User_id,_,_,_,_}]} = get_database:get_user_from_db(User),
    get_database:rollback_booking(User_id),    
    ok. 

%%---------------------------------------------------------------------%%

