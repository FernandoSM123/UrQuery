:- module(lexer, [progUrQuery/3,is_progUrQuery/2]).
:- use_module(library(dcg/basics)).

asc(C):- char_type(C,ascii). %comprobar si es un caracter asc
quotes(C):- char_type(C,quote). %comprobar si caracter es una comilla

letter(C) :- code_type(C, alpha). %caracter es una letra
num(C) :- code_type(C, digit). %caracter es un numero

%Espacios en blanco y caracteres de escape
ws --> (" ";"\t";"\n";"\r"), ws.
ws -->  [].

%1)STRING -> javascript_string;

start_str(I) --> "\"",{!},rest_str(R),{atom_codes(I,R)}.
rest_str([F|R])--> [F],{asc(F)},rest_str(R).
rest_str(R) --> "\"",end_str(R).
end_str([]) --> [].
str(string(I)) -->start_str(I),{!}.

is_string(I,Ast):- atom_codes(I,Codes), phrase(str(Ast),Codes).

%2)IDENTIFIER -> javascript_identifier;

start_id(I) --> [F], {letter(F)},{!}, rest_id(R), {atom_codes(I,[F|R])}.
rest_id([F|R]) --> [F],{letter(F);num(F)},rest_id(R).
rest_id([]) --> [].
id(id(I)) --> start_id(I),{!}.

is_id(I,Ast) :- atom_codes(I, Codes), phrase(id(Ast), Codes).

%3)XML TAG -> xml_unqualified_identifier;

start_tag(I) --> ws, [F], {letter(F)},{!}, rest_tag(R), {atom_codes(I,[F|R])}.
rest_tag([F|R]) --> [F],{letter(F);num(F)},rest_tag(R).
rest_tag([]) --> [].
tag(tag(I)) --> start_tag(I),{!}.

is_tag(I,Ast) :- atom_codes(I, Codes), phrase(tag(Ast), Codes).

%4)QVAR -> '$'identifier;

qvar(qvar(I)) --> "$",id(I),{!}.

is_qvar(I,Ast) :- atom_codes(I, Codes), phrase(qvar(Ast), Codes).


%5)EXPR -> identifier | string | '.'

start_expr(I) --> id(I).
start_expr('\".\"') --> [34,46,34].
start_expr('\'.\'') --> [39,46,39].
start_expr(I) --> str(I).
expr(expr(I)) --> start_expr(I),{!}.

is_expr(I,Ast) :- atom_codes(I, Codes), phrase(expr(Ast), Codes),writeln(Codes).

%6)XPATH -> tag | xpath '/' tag;

start_xpath(I) --> xp1(I),{!}.
xp1(I) --> xp2(I)|xp3(I).
xp2([F|R]) --> tag(F),"/",xp1(R).
xp3([F|R]) --> tag(F),xpath_end(R).
xpath_end([]) --> [].
xpath(xpath(I)) --> start_xpath(I),{!}.

is_xpath(I,Ast) :- atom_codes(I, Codes), phrase(xpath(Ast), Codes).

%7)STARTPATH -> "/" xpath;

%	/aaa/bbb/ccc

startxpath(startxpath(I)) --> "/",xpath(I),{!}.

is_startxpath(I,Ast) :- atom_codes(I, Codes), phrase(startxpath(Ast), Codes).

%8)VARPATH -> qvar (startxpath)?;

%	$li/aaa/bbb/ccc
%	$li

varpath(vartag(Q,S)) --> qvar(Q),startxpath(S),{!}.
varpath(vartag(Q)) --> qvar(Q).

is_varpath(I,Ast) :- atom_codes(I, Codes), phrase(varpath(Ast), Codes).

%9)VARTAG -> "<" tag ">" "{" varpath "}" "</" tag ">";

%	<u1>{$li}</u1>;

vartag(vartag(T,V,T)) --> "<", tag(T), ">", "{", varpath(V), "}", "</", tag(T), ">".

is_vartag(I,Ast) :- atom_codes(I, Codes), phrase(vartag(Ast), Codes).


%10)VARQUERY -> vartag | varpath;

%	<u1>{$li}</u1>
%	$li/aaa/bbb/ccc

varquery(varquery(I)) --> vartag(I),{!}.
varquery(varquery(I)) --> varpath(I).

is_varquery(I,Ast) :- atom_codes(I, Codes), phrase(varquery(Ast), Codes).

%11)DOCPATH -> "doc" "(" expr ")"

%	doc(aaa)
%	doc(\".\")
%	doc(\"aaaa\")

docpath(docpath(I)) --> "doc", "(", expr(I), ")",{!}.

is_docpath(I,Ast) :- atom_codes(I, Codes), phrase(docpath(Ast), Codes).

%12)SOURCEQUERY -> docpath | qvar;

% doc(aaaa)
% doc(\"aaaa\")
% doc(\".\")
% $aaaa

sourcequery(sourcequery(I)) --> docpath(I),{!}.
sourcequery(sourcequery(I)) --> qvar(I).

is_sourcequery(I,Ast) :- atom_codes(I, Codes), phrase(sourcequery(Ast), Codes).

%13)EXPR_QUERY -> sourcequery ( startxpath )? ;

%   doc(aaaa)
%   doc(aaaa)/aaa

exprquery(exprquery(SO,ST)) --> sourcequery(SO),startxpath(ST),{!}.
exprquery(exprquery(SO)) --> sourcequery(SO).

is_exprquery(I,Ast) :- atom_codes(I, Codes), phrase(exprquery(Ast), Codes).

%14)FOR_QUERY -> "for" qvar "in" exprquery "return" varquery ;

%   for $li in doc(\".\") return <li>{$li}</li>
%   for $li in doc(aaa) return <li>{$li}</li>
%   for $li in doc(aaa) return $li/aaa/bbb/ccc

forquery(forquery(Q,E,V)) --> ws, "for", ws, qvar(Q), ws, "in", ws, exprquery(E), ws, "return", ws, varquery(V), {!}.

is_forquery(I,Ast) :- atom_codes(I, Codes), phrase(forquery(Ast), Codes).

%15)TAG_QUERY -> "<" tag ">" "{" forquery "}" "</" tag ">" ;

%   <p> { for $li in doc(\".\") return <li>{$li}</li> } </p>
%   <p> { for $li in doc(aaa)/aaa return <li>{$li}</li> } </p>
%   <p> { for $li in doc(aaa) return <li>{$li}</li> } </p>

tagquery(tagquery(T,F,T)) --> ws, "<", tag(T), ">", ws, "{", ws, forquery(F), ws, "}", ws, "</", tag(T), ">", {!}.

is_tagquery(I,Ast) :- atom_codes(I, Codes), phrase(tagquery(Ast), Codes).

%16)URQUERY -> tagquery;

%   <ul> { for $li in doc(\".\") return <li>{$li}</li> } </ul>
%   <ul> { for $li in doc(aaa)/aaa return <li>{$li}</li> } </ul>

urquery(urquery(I)) --> tagquery(I),{!}.

is_urquery(I,Ast) :- atom_codes(I, Codes), phrase(urquery(Ast), Codes).

%17)LET_PROG -> 'let' identifier '=' expr 'in' urQuery;

% let uri = \".\" in <ul> { for $li in doc(aaa)/aaa return <li>{$li}</li> } </ul>

letprog(letprog(I,E,U)) --> ws, "let", ws, id(I), ws, "=", ws, expr(E), ws, "in", ws, urquery(U),{!}.

is_letprog(I,Ast) :- atom_codes(I, Codes), phrase(letprog(Ast), Codes).

%18)PROG_UR_QUERY -> letprog | urQuery;

%   let uri = \".\" in <ul> { for $li in doc(aaa)/aaa return <li>{$li}</li> } </ul>
%   <ul> { for $li in doc(aaa)/aaa return <li>{$li}</li> } </ul>

progUrQuery(progUrQuery(I)) --> letprog(I),{!}.
progUrQuery(progUrQuery(I)) --> urquery(I).

is_progUrQuery(I,Ast) :- atom_codes(I, Codes), phrase(progUrQuery(Ast), Codes).