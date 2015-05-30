# Namn på projektet

OSPP (1DT096) 2015 - Grupp 04.

Projektarbete på kursen Operativsystem och processorienterad
programmering (1DT096) våren 2015, Uppsala universitet.

This program is a bookingsystem for an airliner/airport.
> - https://help.github.com/articles/markdown-basics/

> Det går att redigera, förhandsvisa och spara (commit) sidan direkt i
> en webläsare via [projektsidan på github.com](./README.md).

## Kompilera

####Compiling and loading the DB.
###First make sure that you have the following software installed.
1. MySQL server ver: 5.5.43-0ubuntu0.14.04.1
2. Amnesia ver: amnesia-1.6.2
3. Erlang/OTP ver: 17

###When you are installing the MySql-server you need to setup an root password make sure you have this in hand during this installation.

###When you have completede the prior-steps
4. run make init_db password=<your SQL rootPassword>
   This will create the Database structure and run a load script for the Database you will need to enter your password one time per Database table.
5. If you dont get any errors you are done.
 
#### Compiling and testing Server

#### Make.

##### To compile everything

Run ´make´

##### To reload code (using Erlangs build in code swapping)

Run ´make reload_code´

##### To test

Run ´make test_$$$$´, where $$$$ is the name of the .erl file that has tests written in eunit.

Running ´make test_server´ will override the previous statment and start a special test where a server is initiated in case there is not already one. 

### Running

#### Make

##### To start server:

Run ´make start_server´

It will, using Make, check if a server is already running. This is done by checking if there is a "current-session" file in the server-messages folder. This can cause problems in the event of a crash. In that case, manually remove or rename the current-session file.

##### To shut down server:

Run ´make stop_server´

## Testa

> Kortfattade instruktioner för hur automatiska testfall körs.

## Starta systemet

> Kortfattade instruktioner för hur systemet startas.

## Struktur

Projektet består av följande kataloger.

### doc

Dokumentation, projektrapporter och andra viktiga dokument.

### ebin

Erlang beam-filer.

> Ändra till `bin` eller `build` eller liknande om projektet inte
> använder Erlang.

### meta

- Presentation av gruppens medlemmar.
- Gruppkontrakt.
- Projektdagböcker.
- Reflektioner på gruppens arbete.

### src

All källkod.

## Fler rubriker

> Lägg till mer information allt eftersom.
