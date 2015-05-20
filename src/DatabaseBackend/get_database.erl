%%------------------------
%% @title DEV_FILE FOR SQL QUARRYS
%% @version 1.0.0
%% {@date}
%% @author Oskar Ahlberg
%% @doc This file is for testing the sql code for%% the serverhandeling part of the program.
%% @end
%%------------------------

-module(get_database).

-include ("consair_database.hrl").

%%-include_lib("amnesia/include/amnesia_db_def.hrl").
%%-compile(export_all).

-export ([get_airport_from_db/0, get_airport_from_db_filter/1]).

get_airport_from_db () ->
    {ok,Pid}=amnesia:open(consair_database),
    amnesia:fetch(Pid,airport).

get_airport_from_db_filter (Airport) ->
    {ok,Pid}=amnesia:open(consair_database),    
    amnesia:fetch(Pid,airport, {"iata = $1", [Airport]}).


