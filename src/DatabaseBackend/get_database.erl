%%------------------------
%% @title DEV_FILE FOR SQL QUARRYS
%% @version 1.0.0
%% {@date}
%% @author Oskar Ahlberg
%% @doc This file is for testing the sql code for%% the serverhandeling part of the program.
%% @end
%%------------------------

-module(get_database).

-include ("air_database.hrl").

-include_lib("amnesia/include/amnesia_db_def.hrl").


get_quarry_one () ->
    {ok,Pid}=amnesia:open(air_database),
    amnesia:fetch(Pid,flightno).

get_quarry_tow () ->
    {ok,Pid}=amnesia:open(air_database),    
    amnesia:fetch(Pid,flightno).

get_quarry_three () ->
    {ok,Pid}=amnesia:open(air_database),    
    amnesia:fetch(Pid,flightno).

get_quarry_four () ->
    {ok,Pid}=amnesia:open(air_database),    
    amnesia:fetch(Pid,flightno).

get_quarry_five () ->
    {ok,Pid}=amnesia:open(air_database),    
    amnesia:fetch(Pid,flightno).

get_quarry_six () ->
    {ok,Pid}=amnesia:open(air_database),    
    amnesia:fetch(Pid,flightno).

get_quarry_seven () ->
    {ok, Pid)=amnesia:open(air_database),
    amnesia:fetch(Pid,flightno).

