-module (test_database).

 -include ("airline.hrl").

 -export ([populate/0]).


 populate() ->
   {ok, Pid} = amnesia:open(database),

   
   ok.
