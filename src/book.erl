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
%%-export([loop/1, get/1, login/1, validate/1, heartbeat/1, disconnect/1]).
-export([]).
%%---------------------------------------------------------------------%%


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
%%-spec set() -> ok when 
%%      Ref::{integer(),string()}.


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
      User::integer(),
      Key::string().


login(User,Key) ->
	%username/loyalprogramnr ? password
	tbi.

%%---------------------------------------------------------------------%%
