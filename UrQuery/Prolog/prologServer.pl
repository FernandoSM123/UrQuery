:- use_module(library(http/http_server)).
:- use_module(library(http/thread_httpd)).
:- use_module(library(http/http_dispatch)).
:- use_module(library(http/http_json)).
:- use_module(library(http/http_log)).

:- use_module(parser).

:- initialization
    http_server([port(8082)]).


:- http_handler('/', index, []).
:- http_handler('/test',test, [method(get)]).
:- http_handler('/compile', handle_request, [method(post)]).

index(_Request) :-
    format('Content-Type: text/plain~n~n'),
    format('Hola Mundo').
	
	
test(_Request) :-
    format('Content-Type: text/plain~n~n'),
    format('Test request').

%PROCESAR REQUEST
handle_request(Request) :-
    http_read_json_dict(Request, Query),
    solve(Query, Solution),
    reply_json_dict(Solution).

solve(_{urQuery:Script}, _{status: true, result:Result, msg:'succeed'}):-
    writeOuput2(Script,Result).

solve(_, _{status: false, msg:'Error: compilation failed'}).



%sudo kill -9 $(lsof -ti :$8082)