%%------------------------
%% @title DEV_FILE FOR SQL QUARRYS
%% @version 1.0.0
%% {@date}
%% @author Oskar Ahlberg
%% @doc This file is for testing the sql code for%% the serverhandeling part of the program.
%% @end
%%------------------------

-module(get_database).

-export ([get_user_from_db/1,get_airport_from_db/0, get_airport_from_db_filter/1,get_flight_from_db/0,get_flight_from_db_f/1,get_airport_from_db_filter_id/1,get_all_flights_from_airport/1,get_test/0,get_airport_filter_and_date/3,get_seats_from_flight/1]).

-include ("consair_database.hrl").
-include("amnesia.hrl").

%%-include_lib("amnesia/include/amnesia_db_def.hrl").
%%-compile(export_all).


get_user_from_db(Usernamne)->
    {ok,Pid}=amnesia:open(consair_database),
    amnesia:fetch(Pid, user,{"user_name = $1",[Usernamne]}).


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
