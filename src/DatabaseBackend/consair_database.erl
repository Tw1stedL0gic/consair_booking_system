%% ------------------------
%% @title consair_database
%% @version 1.0.0
%% {@date}
%% @author Oskar Ahlberg
%% @doc This file contains the outline for the database part of the consair booing program. This file will only bee usede one per installations of the database%% to give it the standard outline of database
%% OTHER INFOMATION
%%To make this file work you need MySql and amnesisa ( http://sourceforge.net/projects/amnesia/) 
%%This command is to "make" the diffrent database tables seen below, you need an mysql server and the amnesia lib to make this work
%%amnesia:db_tool(consair_database, [{make_hdr, "."},make_db, {dba_user, "root"}, {dba_password, "1337"}]). 
%%database U root ,pw 1337 (on Oskars laptop)
%% @end
%% ------------------------


-module(consair_database).

%% includes the amnesia lib
-include_lib("amnesia/include/amnesia_db_def.hrl").

%% Adds driver info for the Database
driver_info() ->
    [ {driver, mysql_drv},
      {host, "localhost"},
      {user, "consair"},
      {password, "grupp04"}].

%%The def of the diffrent tables   
tables () ->
    [user, airport, flights, seats].


table (user) ->
    [ {user_name, varchar, [unique, not_null]},
      {user_password, varchar, not_null},
      {user_class, integer, not_null},
      {user_email, varchar, not_null}];

%% table (airport_from) ->
%%     [{iata_f, varchar ,[unique, not_null]},
%%      {airport_name_f, varchar, not_null}];


table (airport) ->
    [{iata, varchar ,[unique, not_null]},
     {airport_name, varchar, not_null}];


table (flights) -> 
    [ refers_to(airport),
      %%{departuer_point, varchar, not_null}, 
      {arrival_point, varchar, not_null}, 
      %%refers_to(airport_to),
      {departure_date, datetime, not_null}, 
      {arrival_date, datetime, not_null}, 
      {flight_nr, varchar, not_null}];



table(seats) ->
    [ refers_to(flights),
      {class, int, not_null},
      %%{user_name, varchar, not_null}, 
      refers_to(user),
      {window, integer, not_null},
      {aisle, integer, not_null}, 
      {row, varchar, not_null},
      {col, varchar, not_null}, 
      {price, integer, not_null},
      {lock_s, integer, not_null}].
