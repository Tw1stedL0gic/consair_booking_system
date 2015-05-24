%%------------------------
%% @title DEV_FILE FOR SQL QUARRYS
%% @version 1.0.0
%% {@date}
%% @author Oskar Ahlberg
%% @doc This file is for testing the sql code for%% the serverhandeling part of the program.
%% @end
%%------------------------

-module(get_database).

%%-export ([get_user_from_db/1,get_airport_from_db/0, get_airport_from_db_filter/1,get_flight_from_db/0,get_flight_from_db_f/1,get_airport_from_db_filter_id/1,get_all_flights_from_airport/1,get_test/0,get_airport_filter_and_date/3,get_seats_from_flight/1,get_seat_ids/0,update_seat_lock/2, update_seat_user/2,get_all_users_from_db/0,add_new_user/4,remove_user/1,remove_airport/1,add_new_airport/2,get_user_from_seats/1,get_seats_id_from_db/1]).
-compile(export_all).
-include ("consair_database.hrl").
-include("amnesia.hrl").

%%-include_lib("amnesia/include/amnesia_db_def.hrl").
%%-compile(export_all).


get_user_from_db(Usernamne)->
    {ok,Pid}=amnesia:open(consair_database),
    amnesia:fetch(Pid, user,{"user_name = $1",[Usernamne]}).

get_all_users_from_db()->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid, user).

get_airport_from_db () -> 
    {ok,Pid}=amnesia:open(consair_database),
    amnesia:fetch(Pid,airport).

get_airport_from_db_filter (Airport_id) ->
    {ok,Pid}=amnesia:open(consair_database),    
    amnesia:fetch(Pid,airport, {"id = $1", [Airport_id]}).
    
get_airport_filter_and_date(Airport,Arrivalpoint,Date)->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid, [airport, ?JOIN ,flights],{"iata= $1",[Airport],"arrival_point= $2",[Arrivalpoint],"departure_date = $3",[Date]}).

get_airport_from_db_filter_id (Airport_id) ->
    {ok,Pid}=amnesia:open(consair_database),    
    amnesia:fetch(Pid,airport, {"id = $1", [Airport_id]}).

get_flight_from_db_f (Flight) ->
    {ok, Pid}=amnesia:open(consair_database),
    amnesia:fetch(Pid,flights, {"id = $1", [Flight]}).

get_seats_from_flight (Flight) ->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid,[flights,?JOIN,seats],{"flight_nr = $1",[Flight]}).   

get_seats_id_from_db(Seat_id)->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid,seats,{"id = $1",[Seat_id]}).

get_flight_from_db()->
    {ok,Pid}=amnesia:open(consair_database),
    amnesia:fetch(Pid,flights).
    
get_all_flights_from_airport(Airport)->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid, [airport, ?JOIN ,flights],{"iata= $1",[Airport]}).

get_user_from_seats(User) ->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid, [seats, ?JOIN,user],{"user_name = $1",[User]}).
   
get_test()->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid, [airport, ?JOIN, flights, ?JOIN, seats, ?JOIN, user]).

get_seat_ids()->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid,seats).

%%------------------------------------------------------------
%%Update and remove funtions for DB

update_seat_lock(Seat_id, Lock) ->
    {ok, Pid} = amnesia:open(consair_database),
    {ok,[NewLock]} = amnesia:fetch(Pid, seats, {"id = $1",[Seat_id]}),
    NewL = NewLock#seats{lock_s = Lock },
    amnesia:update(Pid,NewL). %%Updates the lock

update_seat_lock_user(User, Lock) ->
    {ok, Pid} = amnesia:open(consair_database),
    {ok,[NewLock]} = amnesia:fetch(Pid, seats, {"user_id = $1",[User]}),
    NewL = NewLock#seats{lock_s = Lock },
    amnesia:update(Pid,NewL). %%Updates the lock

update_seat_user(Seat_id, User_id) ->
    {ok, Pid} = amnesia:open(consair_database),
    {ok,[NewUser]} = amnesia:fetch(Pid, seats, {"id = $1",[Seat_id]}),
    NewU = NewUser#seats{user = User_id },
    amnesia:update(Pid,NewU). %%Updates the User

rollback_booking(User_id)->
    {ok, Pid} = amnesia:open(consair_database),
    {ok, [Roll]} = amnesia:fetch(Pid, seats, {"user_id = $1",[User_id]}),   
    NewRoll = Roll#seats{user = 1, lock_s = 0},
    amnesia:update(Pid,NewRoll).

add_new_user (Name,Password,UserClass,Email) ->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:add_new(Pid, #user { user_name = Name,
			         user_password = Password,
				 user_class = UserClass,
				 user_email = Email}).


add_new_airport(Iata,PlainText) ->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:add_new(Pid, #airport {iata = Iata,
				   airport_name = PlainText}).

remove_airport(Iata) ->
    {ok, Pid} = amnesia:open(consair_database),
    {ok, [Del]} = amnesia:fetch(Pid, airport,{"iata = $1",[Iata]}),
    amnesia:delete(Pid,Del).

remove_user(Name)->
    {ok,Pid} = amnesia:open(consair_database),
    {ok, [Del]} = amnesia:fetch(Pid, user,{ "user_name = $1",[Name]}),
    amnesia:delete(Pid,Del).
%%--------------------------____TEST-_________---------------------    
%%{ok,[%% {user,5,"carl","asdasd",2,"carl@derp.se"}]}
%% start_booking(User,Seat_id)->
%%     {_,[{_,User_id,_,_,_,_}]} = get_database:get_user_from_db(User),
%%     get_database:update_seat_lock(Seat_id,1),
%%     get_database:update_seat_user(Seat_id,User_id),
%%     ok.
    



%% seat_details([Last_seat_ID]) ->
%%     [get_database:get_seats_id_from_db(Last_seat_ID)];
%% seat_details([Head_seat_ID | Tail_seat_ID]) ->
%%     lists:append([case get_database:get_seats_id_from_db(Head_seat_ID) of
%% 		      {ok,[{DB,S_id,Flight,Class,User,Window,Aisle,Row,Col,Price,2}]} ->    
%% 			  {ok,[{DB,S_id,Flight,Class,User,Window,Aisle,Row,Col,Price,1}]};
%% 		      Seat -> Seat
%% 		  end],						    
%% 		 seat_details(Tail_seat_ID)).



%% seat_details([Last_seat_ID],admin) ->
%%     [get_database:get_seats_id_from_db(Last_seat_ID)];
%% seat_details([Head_seat_ID | Tail_seat_ID],admin) ->
%%     lists:append([get_database:get_seats_id_from_db(Head_seat_ID)], seat_details(Tail_seat_ID,admin)).


%% seat_lock(Seat_ID) ->
%%     %% return availability of Seat_ID in Flight_ID
%%     Data = get_database:get_seats_id_from_db(Seat_ID),
%%     case Data of
%% 	{_,[{_,_,_,_,_,_,_,_,_,_,Lock_s}]} ->
%% 	    case Lock_s of
%% 		0->
%% 		    0;
%% 		1->
%% 		    1;
%% 		2 ->
%% 		    2
%% 	    end;
%% 	_->
%% 	    {error,no_seat}
%%     end.
	

%% test_login(Username, Password) ->
%%     {_,[{_,_,Name,Pass,Ulvl,_}]} = get_user_from_db(Username),

%%     case Username =:= Name of
%% 	true ->
%% 	    case Password =:= Pass of
%% 		true->
%% 		    case Ulvl =:= 1 of
%% 			true ->
%% 			    admin;
%% 			false ->
%% 			    user
%% 		    end;
%% 		false->
%% 		    wrong_pass
%% 	    end;
%% 	false ->
%% 	    no_user
%%     end.


%%     %% {ok,[[{seats,2,2,1,3,0,0,"1","A",1337,0},
%%     %%   {user,3,"Kalle","hehe",2,"kalle@derp.se"}]]}

       
%% disconnect(Username) ->
%%     Data  = get_database:get_user_from_seats(Username),
%%     case Data of
%% 	{_,[[{_,S_id,_,_,_,_,_,_,_,_,Lock_s},{_,_,U_name,_,_,_}]]} ->
%% 	    case U_name =:= Username of
%% 		true ->
%% 		    case Lock_s of
%% 			1 ->
%% 			    get_database:update_seat_lock(S_id,0),
%% 			    ok;
%% 			_ ->
%% 			    ok
%% 		    end;
%% 		_->    
%% 		    ok
%% 	    end;
%% 	_ ->
%% 	    ok
%%     end.
