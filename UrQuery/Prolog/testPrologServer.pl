:- use_module(library(http/http_server)).

:- initialization
    http_server([port(8082)]).


:- http_handler('/', index, []).
:- http_handler('/test',test, [method(get)]).

index(_Request) :-
    format('Content-Type: text/plain~n~n'),
    format('Hola Mundo').
	
	
test(_Request) :-
    format('Content-Type: text/plain~n~n'),
    format('Test request').