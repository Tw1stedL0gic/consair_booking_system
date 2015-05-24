%%------------------------
%% @title DEV_FILE FOR SQL QUARRYS
%% @version 1.0.0
%% {@date}
%% @author Oskar Ahlberg
%% @doc This file is for testing the sql code for%% the serverhandeling part of the program.
%% @end
%%------------------------

-module(get_database).

-export ([get_user_from_db/1,get_airport_from_db/0, get_airport_from_db_filter/1,get_flight_from_db/0,get_flight_from_db_f/1,get_airport_from_db_filter_id/1,get_all_flights_from_airport/1,get_test/0,get_airport_filter_and_date/3,get_seats_from_flight/1,get_seat_ids/0,update_seat_lock/2, update_seat_user/2,get_all_users_from_db/0,add_new_user/4,remove_user/1,remove_airport/1,add_new_airport/2]).

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

get_airport_from_db_filter (Airport) ->
    {ok,Pid}=amnesia:open(consair_database),    
    amnesia:fetch(Pid,airport, {"iata = $1", [Airport]}).
    
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

get_flight_from_db()->
    {ok,Pid}=amnesia:open(consair_database),
    amnesia:fetch(Pid,flights).
    
get_all_flights_from_airport(Airport)->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid, [airport, ?JOIN ,flights],{"iata= $1",[Airport]}).
   
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

update_seat_user(Seat_id, User_id) ->
    {ok, Pid} = amnesia:open(consair_database),
    {ok,[NewUser]} = amnesia:fetch(Pid, seats, {"id = $1",[Seat_id]}),
    NewU = NewUser#seats{user = User_id },
    amnesia:update(Pid,NewU). %%Updates the lock

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
    
