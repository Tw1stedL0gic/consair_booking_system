ERLC_FLAGS=-Wall

SOURCES=$(wildcard src/*.erl src/backend/*.erl src/DatabaseBackend/*.erl)

HEADERS=$(wildcard src/*.hrl src/backend/*.hrl src/DatabaseBackend/*.hrl)

OBJECTS:=$(SOURCES:src/%.erl=ebin/%.beam)

APPNAME= ConsAir_Bookingsystem

.PHONY: doc doc_url skeleton start_server

all: $(OBJECTS)

ebin/%.beam: src/%.erl 
	erlc $(ERLC_FLAGS) -o ebin/ $<

clean:
	rm -Rf ebin/*
	rm -Rf src/*.beam
	rm -Rf src/backend/*.beam
	rm -Rf doc/*.html

doc: 
	erl -noshell -run edoc_run application "'$(APPNAME)'"  '"."' '[{def,{vsn,"$(VSN)"}}, {stylesheet, "my_style.css"}]'

doc_url:
	@echo 
	@echo "EDoc index page available at file://$(PWD)/doc/index.html"
	@echo

start_server: all
	erl -pa ebin/ -s server -s init stop -noshell

stop_server: all
	erl -pa ebin/ -s server_utils stop_server -s init stop -noshell

reload_code: all
	erl -pa ebin/ -s server_utils reload_code -s init stop -noshell

client_terminal:
	java -jar src/JavaNetLib/target/JavaNetLib.jar

### EUnit ###

# Make a comma separated list:
# http://ftp.gnu.org/old-gnu/Manuals/make-3.79.1/html_chapter/make_8.html

comma:= ,
empty:=
space:= $(empty) $(empty)

OBJECTS_LIST:= $(subst $(space),$(comma),$(OBJECTS:ebin/%.beam=%))

test: $(OBJECTS)
	erl -noshell -pa ebin -eval 'eunit:test([$(OBJECTS_LIST)], [])' -s init stop

testv: $(OBJECTS)
	erl -noshell -pa ebin -eval 'eunit:test([$(OBJECTS_LIST)], [verbose])' -s init stop

test_%: ebin/%.beam
	erl -noshell -pa ebin -eval "eunit:test($(subst test_,, $@), [])" -s init stop

testv_%: ebin/%.beam
	erl -noshell -pa ebin -eval "eunit:test($(subst testv_,, $@), [verbose])" -s init stop

