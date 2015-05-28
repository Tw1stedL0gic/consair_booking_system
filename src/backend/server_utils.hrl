-import(server_utils, [translate_package/1, now_as_string_millis/0, list_to_regexp/2, flatten_tuples_to_list/1, connect_send_and_receive_manual/2, connect_send_and_receive/2, connect_send_and_receive_list/2, reload_code/0,reload_code/1, stop_server/0, stop_server/1, format_position_as_mod/2, format_position_as_mod/4, fill_with_white_space/2]).

-define(REG_EXP_SEPERATOR, "&"). %% must be enclosed in quotes




-define(PORT, 53535).
-define(ALT_PORT, 53335).
-define(CONNECTIONOPTIONS, [binary, {packet, 0}, {active, false}]).
-define(ALLOWEDTIMEOUTS, 10). %% Amount of minutes allowed before connection is terminated
-define(ADMIN_ALLOWEDTIMEOUTS, 20). %% Amount of minutes allowed before admin connection is terminated



%% Package IDs
-define(LOGIN,                         1).
-define(LOGIN_RESP,                    2).
-define(ERROR,                         3).
-define(DISCONNECT,                    4).
-define(INIT_BOOK,                     5).
-define(INIT_BOOK_RESP,                6).
-define(FIN_BOOK,                      7).
-define(FIN_BOOK_RESP,                 8).
-define(ABORT_BOOK,                    9).
-define(REQ_AIRPORTS,                 10).
-define(REQ_AIRPORTS_RESP,            11).
-define(SEARCH_ROUTE,                 12).
-define(SEARCH_ROUTE_RESP,            13).
-define(REQ_FLIGHT_DETAILS,           14).
-define(REQ_FLIGHT_DETAILS_RESP,      15).
-define(REQ_SEAT_SUGGESTION,          16).
-define(RESP_SEAT_SUGGESTION_RESP,    17).
-define(REQ_SEAT_MAP,                 -1).
-define(REQ_SEAT_MAP_RESP,            -1).
-define(TERMINATE_SERVER,             24).
 
-define(HEARTBEAT,                    25).
-define(RELOAD_CODE,                  26).
-define(REQSeatLock,                 100).
-define(RESPSeatLock,                101).






-define(REQReceipt,                   17).
-define(RESPReceipt,                  18).



-define(WRITE_CONNECTION(String, Arguments), 
	io:format(" C"
		  ++ fill_with_white_space(integer_to_list(ID), 3) 
		  ++ format_position_as_mod(ID, 5)
		  ++ " | " 
		  ++ fill_with_white_space(pid_to_list(self()), 10)
		  ++ " | " 
		  ++ fill_with_white_space(case is_atom(User) of true -> atom_to_list(User); _ -> User end, 14)
		  ++ " | " 
		  ++ String, 
		  Arguments)).

-define(WRITE_SPAWNER(String, Arguments), 
	io:format("*CS* " 
		  ++ format_position_as_mod(-1, 5) 
		  ++ " | " 
		  ++ fill_with_white_space(pid_to_list(self()), 10)
		  ++ " | " 
		  ++ "Connections: ~p"
		  ++ " | " 
		  ++ String, 
		  [N | Arguments])).
