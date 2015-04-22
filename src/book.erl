-module(book).
-export([start/3, start/4]).

%% @doc TODO: add documentation
-spec get(Ref) -> ok when 
      Ref::integer(),string().


get(Ref) ->
	%flight_info_by_flightnr
	%passenger_info (logged-in)
	%number_seat_avail
	%book_rate
	tbi.



%% @doc TODO: add documentation
-spec set(Ref) -> ok when 
      Ref::integer(),string().


set(Ref) ->
	%(same as get)
	%passenger_info: preference
	tbi.



%% @doc TODO: add documentation
-spec seat(Flight,Date) -> ok when 
      Flight::string(),
      Date::integer().


seating(Flight,Date) ->
	%by_preference
	%by_random
	%user_choice
	tbi.



%% @doc TODO: add documentation
-spec login(User,Key) -> ok when 
      User::interger(),
      Key::string().

login(User,Key) ->
	%username/loyalprogramnr ? password
	tbi.