-import(server_utils, [translate_package/1, now_as_string_millis/0, list_to_regexp/2, flatten_tuples_to_list/1, connect_send_and_receive_manual/2, connect_send_and_receive/2, connect_send_and_receive_list/2]).

-define(REG_EXP_SEPERATOR, "&"). %% must be enclosed in quotes
-define(CONNECTIONOPTIONS, [binary, {packet, 0}, {active, false}]).



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
-define(REQSeatLock,                 100).
-define(RESPSeatLock,                101).






-define(REQReceipt,                   17).
-define(RESPReceipt,                  18).


