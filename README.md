# Namn på projektet

OSPP (1DT096) 2015 - Grupp 04

> Lägg till namnet på projektet och ändra XX till numret på din grupp.

Projektarbete på kursen Operativsystem och processorienterad
programmering (1DT096) våren 2015, Uppsala universitet.

> Lägg till en kort beskrivning av projektet.

> Denna fil är skrivet i formatet Markdown, läs mer här:
>
> - https://help.github.com/articles/markdown-basics/

> Det går att redigera, förhandsvisa och spara (commit) sidan direkt i
> en webläsare via [projektsidan på github.com](./README.md).

## Kompilera

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
