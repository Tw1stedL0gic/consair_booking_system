%% ------------------------
%% @title Name Of This File
%% @version 1.0.0
%% {@date}
%% @author Arthur O'disfile
%% @doc Description of this file and a quick rundown of what the
%% user will use it for. 
%% @end
%% ------------------------

-module(book).
-export([loop/1, get/1, login/1, validate/1, heartbeat/1, disconnect/1]).

%%---------------------------------------------------------------------%%

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
		{2, Package, PID} -> % receive XXXXXXXX, do YYYYYYYYYY
			case validate({2,Package}) of
				false ->
					print_Bad_Argument;
				true -> PID ! {PassengerList, Package}
			end;
			loop(Input);
		{3, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
			case validate({3,Package}) of
				false ->
					print_Bad_Argument, loop(Input);
				true -> get({BookSeat, PID}), loop(Input)
			end;
		{4, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
			case validate({4,Package}) of
				false ->
					print_Bad_Argument;
				true -> PID ! {BookSeat, Package}
			end			
			loop(Input);
		{5, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
			case validate({5,Package}) of
				false ->
					print_Bad_Argument, loop(Input);
				true -> login({Login, PID}), loop(Input)
			end;
		{6, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
			case validate({6,Package}) of
				false ->
					print_Bad_Argument;
				true -> PID ! {Login, Package}
			end			
			loop(Input);
		{7, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY		 
			case validate({7,Package}) of
				false ->
					print_Bad_Argument;
				true -> disconnect({Disconnect, PID})
			end
			loop(Input);
		{8, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
			case validate({8,Package}) of
				false ->
					print_Bad_Argument;
				true -> heartbeat({Heartbeat, PID})
			end
			loop(Input);
		{9, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
			case validate({9,Package}) of
				false ->
					print_Bad_Argument, loop(Input);
				true -> get({PassengerInfo, PID}), loop(Input)
			end;
		{10, Package, PID} ->  % receive XXXXXXXX, do YYYYYYYYYY
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
	
%% COMMENT: Remember that an atom is as large as an integer in Erlang, 
%% so to make it clearer we might want to change the numbers 1-10 to 
%% atoms. Atoms take up space in the atom table, but a constant 10
%% atoms will not take up too much space. 
%%
%% This is because I was not there to write this, but why does
%% each case need the validate({X, Package}) part? Surely, if the
%% input manages to pass the pattern matching then we shouldn't need
%% to validate. (Also I can't find what the validate() function does. 
%% Have we written it ourselves?
%%
%% We should also add comments to each case so we know what each 
%% case is handling. 
%% - Carl

%%---------------------------------------------------------------------%%

%% @doc TODO: add documentation
-spec set() -> ok when 
      Ref::integer(),string().


set(Ref) ->
	%(same as loop)
	%passenger_info: preference
	tbi.

%%---------------------------------------------------------------------%%

%% @doc TODO: add documentation
-spec seat(Flight,Date) -> ok when 
      Flight::string(),
      Date::integer().


seat(Flight,Date) ->
	%by_preference
	%by_random
	%user_choice
	tbi.

%%---------------------------------------------------------------------%%

%% @doc TODO: add documentation
-spec login(User,Key) -> ok when 
      User::interger(),
      Key::string().


login(User,Key) ->
	%username/loyalprogramnr ? password
	tbi.

%%---------------------------------------------------------------------%%
