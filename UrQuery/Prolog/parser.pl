:- module(parser, [writeOuput2/2]).
:- use_module(lexer).

%prourquery
emit_init(progUrQuery(Q),Stream):-
        emit_letprog(Q,Stream).

%letprog
emit_letprog(letprog(id(I),expr(_),U),Stream):-
        format(Stream,'import {ur_doc, ur_evaluate, ur_tag, ur_active_doc} from \'./urquery.mjs\'~n', []), 
        format(Stream,'function urQuery_01(~s){~n', [I]),
        emit_urquery(U,Stream,I).

%urquery
emit_urquery(urquery(U),Stream,DocId):-
        emit_tagquery(U,Stream,DocId).
        
%tag query
emit_tagquery(tagquery(tag(T),F,tag(T)),Stream,DocId):-
        format(Stream,'\tconst ~s_tag = children => ur_tag(\'~s\', children)~n', [T,T]),
        emit_forquery(F,Stream,DocId,T).

%for --> qvar, exprquery, varquery
emit_forquery(forquery(qvar(_),E,V),Stream,DocId,Tag):-
        format(Stream,'\t\tfunction* for_01(~s){~n',[DocId]),
        emit_exprquery(E,V,Stream,DocId,Tag).

%exprquery --> sourcequery, startxpath
%varquery
emit_exprquery(exprquery(_,S),V,Stream,DocId,Tag):-
        emit_startxpath(S,V,Stream,DocId,Tag).

%startxpath -> "/" xpath
%varquery
emit_startxpath(startxpath(S),V,Stream,DocId,Tag):-
        emit_xpath(S,V,Stream,DocId,Tag).

%xpathList
%varquery
emit_xpath(xpath(X),V,Stream,DocId,Tag):-
        format(Stream,'\t\tconst xpathResultIter = ur_evaluate(ur_doc(~s),\'',[DocId]),
        emit_pathList(X,Stream),
        emit_varquery(V,Stream,DocId,Tag).

%path list
emit_pathList([],Stream):- emit_pathEnd(Stream).
emit_pathList([F|R],Stream):- emit_path(F,Stream),emit_pathList(R,Stream).

%path
emit_path(tag(T),Stream):- 
        format(Stream,'/~s',[T]).

%path end
emit_pathEnd(Stream):- 
        format(Stream,'\')~n',[]).

%varquery --> vartag | varpath
emit_varquery(varquery(V),Stream,DocId,Tag):-
        emit_vartag(V,Stream,DocId,Tag).

%emit vartag --> tag, varpath, tag
emit_vartag(vartag(tag(T),vartag(qvar(id(I))),tag(T)),Stream,DocId,Tag):-
        format(Stream,'\t\tconst ~s_tag = children => ur_tag(\'~s\', children)~n', [T,T]),
        emit_end_script(I,Stream,DocId,Tag).

%end script
emit_end_script(Id,Stream,DocId,Tag):-
        format(Stream,'\t\tfor (~s of xpathResultIter){~n', [Id]),
        format(Stream,'\t\t\tyield ~s_ag(~s)~n', [Id,Id]),
        format(Stream,'\t\t}~n', []),
        format(Stream,'\t}~n', []),
        format(Stream,'\treturn ~s_tag([...for_01(~s)])~n', [Tag,DocId]),
        format(Stream,'}~n~n', []),
        emit_main(Stream,DocId).

%emit main
emit_main(Stream,DocId):- 
         format(Stream,'function main(){~n', []),
         format(Stream,'\tlet ~s = ur_active_doc()~n', [DocId]),
         format(Stream,'\treturn urQuery_01(~s)~n', [DocId]),
         format(Stream,'}~n', []).

%probar con ejemplo de urquery
test(Ast1,Ast2):-
T1 = "let uri = \".\" in <ul> { for $li in doc(aaa)/aaa return <li>{$li}</li> } </ul>",
T2 = "let uri = a in <ul> { for $li in doc(b) return <li>{$li}</li> } </ul>",
is_progUrQuery(T1,Ast1),
is_progUrQuery(T2,Ast2).

%probar archivo de urquery
testFile(Ast):-
        File = "testScript.txt",
        read_file_to_codes(File, Codes, []),
        phrase(progUrQuery(Ast),Codes).

%leer urquery desde archivo, poner resultado en el archivo output
testWrite1():-
        File = "urQueryScript3.txt",
        read_file_to_codes(File, Codes, []),
        phrase(progUrQuery(Ast),Codes),
        Ouput = "ouput.txt",
        open(Ouput, write, Stream),
        emit_init(Ast,Stream),
        close(Stream).

%leer urquery desde archivo, poner resultado en atomo
testWrite2():-
        File = "urQueryScript3.txt",
        read_file_to_codes(File, Codes, []),
        phrase(progUrQuery(Ast),Codes),

        open_memory_outputstream(Handle, InMemoryStream),
        emit_init(Ast,InMemoryStream),
        close(InMemoryStream),
        memory_file_to_atom(Handle, Ouput),
        writeln(Ouput),
        free_memory_file(Handle).

%recibe urquery como arg, poner resultado en archivo ouput
writeOuput1(Input):-
        atom_codes(Input,Codes),
        phrase(progUrQuery(Ast),Codes),
        Ouput = "ouput.txt",
        open(Ouput, write, Stream),
        emit_init(Ast,Stream),
        close(Stream).

%recibe urquery como arg, pone resultado en atomo
writeOuput2(Input,Ouput):-
        atom_codes(Input,Codes),
        phrase(progUrQuery(Ast),Codes),
        
        open_memory_outputstream(Handle, InMemoryStream),
        emit_init(Ast,InMemoryStream),
        close(InMemoryStream),
        memory_file_to_atom(Handle, Ouput),
        free_memory_file(Handle).


%abrir stream en memoria
open_memory_outputstream(Handle, InMemoryStream):-
  new_memory_file(Handle),
  open_memory_file(Handle, write, InMemoryStream)
.
