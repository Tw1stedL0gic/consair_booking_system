-module(book).
-export([loop/1, get/1, login/1, validate/1, heartbeat/1, disconnect/1]).

%% @doc TODO: add documentation
-spec loop(Input) -> ok when 
      Input::[integer()|string()].


loop(Input) ->
	receive 
		{1, Package, PID} -> %receive Id=1 with PID_1
			case validate({1,Package}) of
				false ->
					print_Bad_Argument, loop(Input);
				true -> get({PassengerList, PID}), loop(Input)
			end;
		{2, Package, PID} ->
			case validate({2,Package}) of
				false ->
					print_Bad_Argument;
				true -> PID ! {PassengerList, Package}
			end;
			loop(Input);
		{3, Package, PID} ->
			case validate({3,Package}) of
				false ->
					print_Bad_Argument, loop(Input);
				true -> get({BookSeat, PID}), loop(Input)
			end;
		{4, Package, PID} ->
			case validate({4,Package}) of
				false ->
					print_Bad_Argument;
				true -> PID ! {BookSeat, Package}
			end			
			loop(Input);
		{5, Package, PID} ->
			case validate({5,Package}) of
				false ->
					print_Bad_Argument, loop(Input);
				true -> login({Login, PID}), loop(Input)
			end;
		{6, Package, PID} ->
			case validate({6,Package}) of
				false ->
					print_Bad_Argument;
				true -> PID ! {Login, Package}
			end			
			loop(Input);
		{7, Package, PID} ->		
			case validate({7,Package}) of
				false ->
					print_Bad_Argument;
				true -> disconnect({Disconnect, PID})
			end
			loop(Input);
		{8, Package, PID} ->
			case validate({8,Package}) of
				false ->
					print_Bad_Argument;
				true -> heartbeat({Heartbeat, PID})
			end
			loop(Input);
		{9, Package, PID} ->
			case validate({9,Package}) of
				false ->
					print_Bad_Argument, loop(Input);
				true -> get({PassengerInfo, PID}), loop(Input)
			end;
		{10, Package, PID} ->
			case validate({10,Package}) of
				false ->
					print_Bad_Argument;
				true -> PID ! {PassengerInfo, Package}
			end			
			loop(Input);
	end.
		
		


	%flight_info_by_flightId
	%passenger_info_by_flightNr
	%number_seat_avail
	%book_rate
	


%% @doc TODO: add documentation
-spec set() -> ok when 
      Ref::integer(),string().


set(Ref) ->
	%(same as loop)
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