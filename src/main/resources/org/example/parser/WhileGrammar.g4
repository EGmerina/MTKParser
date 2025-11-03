grammar WhileGrammar;

// Парсерные правила
program: whileStatement+ EOF;

whileStatement: 'while' '(' condition ')' '{' statementList '}';

condition: expression cmpOperator expression;

statementList: statement*;

statement: assignment ';';

assignment: IDENTIFIER '=' expression;

expression: term (addOperator term)*;

term: factor (multOperator factor)*;

factor: IDENTIFIER | NUMBER | '(' expression ')';

cmpOperator: '<' | '>' | '==' | '!=' | '<=' | '>=';

addOperator: '+' | '-';

multOperator: '*' | '/';

// Лексерные правила
IDENTIFIER: [a-zA-Z][a-zA-Z0-9]*;
NUMBER: [0-9]+;

WHITESPACE: [ \t\r\n]+ -> skip;