%%This file is to populate the database


-module ('pop_database').
-include ("consair_database.hrl").

-export ([populate_airport/0,populate_flights/0]).

populate_airport() ->

%%adding airports
    {ok, Pid} = amnesia:open(consair_database),
    {ok,Air1} = amnesia:add_new (Pid, #airport {iata = "ARN",airport_name= "ARLANDA"}),
    {ok,Air2} = amnesia:add_new (Pid, #airport {iata = "ERN",airport_name= "ARNAAIRPORT UPPSALA"}),
    {ok,Air3} = amnesia:add_new (Pid, #airport {iata = "LAX",airport_name= "L.A. International Airport"}),
    {ok, Air4} = amnesia:add_new (Pid, #airport {iata = "OSE",airport_name= "Ostersund flygplats"}),
    {ok, Air5} = amnesia:add_new (Pid, #airport {iata = "DRP",airport_name= "DerpTown Airport"}),
    {ok, Air6} = amnesia:add_new (Pid, #airport {iata = "TEX",airport_name= "Texas internatonal Airport"}),

%%adding flights

    {ok, F1} = amnesia:add_new (Pid, #flights {airport = Air1, arrival_point = "TEX", depature_date = {{2015, 10, 03}, {13, 37, 00}},arrival_date = {{2015, 10, 04}, {01, 01, 10}}, flight_nr = "SKS101"}),

    {ok, F2} = amnesia:add_new (Pid, #flights {airport = Air2, arrival_point = "ARN", depature_date = {{2015, 10, 03}, {13, 37, 00}},arrival_date = {{2015, 10, 04}, {01, 01, 10}}, flight_nr = "SKS102"}),

    {ok, F5} = amnesia:add_new (Pid, #flights {airport = Air3, arrival_point = "ERN", depature_date = {{2015, 10, 03}, {13, 37, 00}},arrival_date = {{2015, 10, 04}, {01, 01, 10}}, flight_nr = "SKS103"}),

    {ok, F4} = amnesia:add_new (Pid, #flights {airport = Air4, arrival_point = "LAX", depature_date = {{2015, 10, 03}, {13, 37, 00}},arrival_date = {{2015, 10, 04}, {01, 01, 10}}, flight_nr = "SKS106"}),
    
%%adding users

