-module(package_handler).
-export([handle_package/1]).

handle_package(Package) ->
    handle_package(translate_package);
handle_package(1, <<Message>>) -> %% ID 1 - Request passengers
    <<2>>; %% ID 2 - Send passengers
handle_package(3, <<Message>>) -> %% ID 3 - Request booking 
    <<4>>; %% ID 4 - Response to booking
handle_package(5, <<Message>>) -> %% ID 5 - Request Login
    <<6>>; %% ID 6 - Response to login
handle_package(6, <<Message>>) -> %% ID 7 - Disconnect
    exit;
handle_package(8, <<Message>>) -> %% ID 8 - Heartbeat
    <<8>>;
handle_package(9, <<Message>>) -> %% ID 9 - Request passenger info
    <<10>>; %% ID 10 - Response to passenger info
handle_package(_) ->
    <<11, "">>, %% a string that explains the error.  
    error(unknownID).


%% Translates from a bitstring to a tuple with ID and message

translate_package(<<ID:8/unsigned, Message/binary>>) ->			
    %% convert Package into a format that 
    %% message handler can read, also make
    %% sure that package is correct
    case message_handler(ID, Message) of
	{ok, SendData} ->
	    gen_tcp:send(S,SendData);
	{err, Reason}  -> tbi
    end;

translate_package({ID}) ->
    package;
translate_package({ID, Message}) ->
    package.
    


	
%%	    %% 1: Hämta passagerare (front-end to back-end)
%%	    %% 2: Passagerarlista (back-end to front-end)
%%	    %% 3: Boka plats
%%	    %% 4: Response to #3 (lyckat / misslyckat)
%%	    %% 5: Login
%%	    %% 6: Response #5
%%	    %% 7: Disconnect / Terminera Anslutning
%%	    %% 8: Heartbeat
%%	    %% 9: Get passenger info (front-end -> back-end)
%%	    %% 10: Response #9


%%Kan vara många fel här, under grov bearbetning. Ska bytas ut mot funktionen loop ovan
%message_handler(1,Mess) ->
%    tbi;
%message_handler(2,Mess) ->
%    tbi;
%message_handler(3,Mess) -> %% Get passengerlist
%    tbi;
%
%    <<AL:16/unsigned, FN/binary>> = Mess
%    {ok, createMessage(4,getPassengerlist(AL,FN))};
%message_handler(4,Mess) ->
%    {err, unsupported};
%message_handler(5,Mess) ->
%    tbi;
%message_handler(6,Mess) ->
%    tbi;
%message_handler(7,Mess) ->
%    tbi;
%message_handler(8,Mess) ->
%    tbi;
%message_handler(9,Mess) ->
%    tbi;
%message_handler(10,Mess) ->
%    tbi.
