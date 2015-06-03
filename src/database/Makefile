ERLC_FLAGS=-Wall
APPNAME=KUK

.PHONY: doc doc_url skeleton start_server



MAKE_DB:
	erl make_db.erl
clean:
	rm -Rf *.beam

doc:
	erl -noshell -run edoc_run files '["get_database.erl"]'

#erl -noshell -run edoc_run application "'$(APPNAME)'"  '"."' '[{def,{vsn,"$(VSN)"}}, {stylesheet, "my_style.css"}]'




start_server: all server-messages/current-session

server-messages/current-session:
	gnome-terminal -x sh -c "erl -pa ebin/ -s server -s init stop -noshell | tee server-messages/current-session; ./server-messages/save-session; bash;"

stop_server: all
	erl -pa ebin/ -s server_utils stop_server -s init stop -noshell

reload_code: all
	erl -pa ebin/ -s server_utils reload_code -s init stop -noshell

client_terminal:
	java -jar src/JavaNetLib/target/JavaNetLib.jar

