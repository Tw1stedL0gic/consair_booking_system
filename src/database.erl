%% ------------------------
%% @title Name Of This File
%% @version 1.0.0
%% {@date}
%% @author Arthur O'disfile
%% @doc Description of this file and a quick rundown of what the
%% user will use it for. 
%% @end
%% ------------------------
%%To make this file work you need MySql and amnesisa ( http://sourceforge.net/projects/amnesia/) 
% %amnesia:db_tool(database, [{make_hdr, "."},make_db, {dba_user, "airline"}, {dba_password, "airline"}]). 
-module(database).
%%-include_lib("../lib/amnesia-1.6.2/include/amnesia_db_def.hrl").
-include_lib("amnesia/include/amnesia_db_def.hrl").


%%-export([]).
%% Adds driver info for the Database

driver_info() ->
    [{driver, mysql_drv},
     {host, "localhost"},
     {user, "airline"},
     {password, "airline"}].

%%The def of the diffrent tables   
tables () -> [flightno,passengerno,seats].

%%The diffrent columns of the database
table (flightno) ->
  [{flight_no, varchar, not_null},
  {flight_to, varchar,not_null},
  {flight_from, varchar,not_null},
  {flight_take_off, integer, not_null},
  {flight_arrival_time, integer,not_null}];

table (passengerno) ->
[ {passenger_no, integer, [unique, not_null]},
     {name, varchar, not_null},
     {address, varchar, not_null},
     {payment_info, integer, not_null},
     {email, varchar, [not_null, {default, ""}]}];
%%The diffrent columns of the database as well links the relations between the tabels.
table (seats) -> 
%%links tables seats to flight no
[ refers_to(flightno),
  {seat, varchar, not_null},
  %%links tables to Passenger id
  refers_to(passengerno),
  {seat_price, {decimal, 10,2}, not_null},
  {seat_res, integer, not_null},
  {seat_booked, integer, not_null}].
