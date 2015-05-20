%% ------------------------
%% @title air_database
%% @version 1.0.0
%% {@date}
%% @author Oskar Ahlberg
%% @doc This file contains the outline for the database part of the consair booing program. This file will only bee usede one per installations of the database%% to give it the standard outline of database
%% OTHER INFOMATION
%%To make this file work you need MySql and amnesisa ( http://sourceforge.net/projects/amnesia/) 
%%This command is to "make" the diffrent database tables seen below, you need an mysql server and the amnesia lib to make this work
%%amnesia:db_tool(air_database, [{make_hdr, "."},make_db, {dba_user, "root"}, {dba_password, "1337"}]). 
%%database U root ,pw 1337 (on Oskars laptop)
%% @end
%% ------------------------


-module(air_database).

%% includes the amnesia lib
-include_lib("amnesia/include/amnesia_db_def.hrl").


%% Adds driver info for the Database
driver_info() ->
    [{driver, mysql_drv},
     {host, "localhost"},
     {user, "air_line"},
     {password, "grupp04"}].

%%The def of the diffrent tables   
tables () -> [flightno,passengerno,seats].

%%The diffrent columns of the database
table (flightno) ->
  [{flight_no, varchar, not_null},
  {flight_to, varchar,not_null},
  {flight_from, varchar,not_null},
  {flight_take_off, integer, not_null},
  {flight_arrival_time, integer,not_null},
  {flight_date, interger, not_null}];

%%The table structure for the passangers.
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
