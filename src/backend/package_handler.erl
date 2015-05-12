-module(package_handler).
-export([handle_package/1]).

handle_package(<<"Hello">>) ->
    <<"Goodbye">>;
handle_package(<<"Foo">>) ->
    <<"Baz">>;
handle_package(<<"exit">>) ->
    exit;
handle_package(_) ->
    <<"I didn't understand that">>.



	
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



%		{ok, <<ID:8/unsigned, Mess/binary>>} ->
%		    %% convert Package into a format that 
%		    %% message handler can read, also make
%		    %% sure that package is correct
%		    case message_handler(ID, Mess) of
%			{ok, SendData} ->
%			    gen_tcp:send(S,SendData);
%			{err, Reason}  -> tbi
%		    end
			    
