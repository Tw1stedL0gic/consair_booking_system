-import(server_utils, [translate_package/1, now_as_string_millis/0, list_to_regexp/2, flatten_tuples_to_list/1, connect_send_and_receive/2, connect_send_and_receive/2]).

-define(REG_EXP_SEPERATOR, "&"). %% must be enclosed in quotes
-define(CONNECTIONOPTIONS, [binary, {packet, 0}, {active, false}]).
