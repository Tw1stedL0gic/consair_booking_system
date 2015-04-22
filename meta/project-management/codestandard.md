# Code Standard

Underscore: Oskar, Wentin
Camel: 

One line functions/calls

# Erlang



Code header:
```
%% ------------------------
%% @title Name Of This File
%% @version 1.0.0
%% {@date}
%% @author Arthur O'disfile
%% @doc Description of this file and a quick rundown of what the
%% user will use it for. 
%% @end
%% ------------------------
```

Documentation and specification:
```
%% @doc A thorough description of what the function accomplishes,
%% and why it exists. With descpritions of what A, B and C are 
%% and what the function returns.
%% === Example ===
%% <div class="example">```
%% > example:function(A, B, C).
%% return_value'''
%% </div>

-spec function(A, B, C) -> ok when
      A::type_of_A(),
      B::type_of_B(),
      C::type_of_C().
```


Auxillary functions:
```
function(A, B, C) ->
      return_value.

aux_function(A, B, C, D) ->
      return_value.
```

Recursive functions:
```
%% A sentence describing the final case
recursive_function(0, 0, 0) ->
      return_value;

%% A sentence describing what case this pattern matching catches
recursive_function(0, B, C) ->
      recursive_function(A, B, C);

%% A sentence describing the first call
recursive_function(A, B, C) ->      recursive_function(A, B, C).

```




Documentati


# Java






# Make