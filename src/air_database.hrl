%% ------------------------
%% @title air_database.hrl
%% @version 1.0.0
%% {@date}
%% @author Oskar Ahlberg
%% @doc This file is used when you add and populate the database.
%% OTHER INFOMATION
%%To make this file work you need MySql-server 
%%and amnesisa ( http://sourceforge.net/projects/amnesia/) 
%%database U root ,pw 1337 (on Oskars laptop)
%% @end
%% ------------------------


%%The outline for table flightno
-record (flightno, {
	id = null,
	flight_no = null,
	flight_to = null,
	flight_from = null,
	flight_take_off = null,
	flight_arrival_time = null}).

%%The outline for table passangerno
-record (passengerno, {
	id = null,
	passenger_no = null,
	name = null,
	address = null,
	payment_info = null,
	email = []}).

%%The outline for table seats
-record (seats, {
	id = null,
	flightno = null,
	seat = null,
	passengerno = null,
	seat_price = null,
	seat_res = null,
	seat_booked = null}).

