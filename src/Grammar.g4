grammar Grammar;

programm
    : (functionDefinition | structDefinition | globalVariableDeclaration)* EOF;

functionDefinition
	: functionReturnType IDENTIFIER '(' variableList? ')' functionBody
	;

structDefinition
    : STRUCT IDENTIFIER structBody
    ;

variableList
	: typeSpecifier IDENTIFIER (',' typeSpecifier IDENTIFIER)*
	;

functionReturnType
	: VOID
	| typeSpecifier
	;

typeSpecifier
	: primitiveTypeSpecifier
	| structType
	;

structType: IDENTIFIER;

primitiveTypeSpecifier
    : INT
    | BOOL
    | STRING
    ;


functionBody
    : '{' statement* (RETURN expression ';')? '}'
    ;

structBody
    : '{' (structFieldDeclaration ';')+ '}'
    ;
structFieldDeclaration
    : typeSpecifier IDENTIFIER
    ;

statement
	: compoundStatement
	| functionCall ';'
	| conditionStatement
	| localAssignment ';'
    | localVariableDeclaration ';'
    | unionStatement ';'
    | whileStatement
	;

unionStatement
    : UNION '{' (variableList)+ '}'
    ;

conditionStatement
	: IF '(' expression ')' compoundStatement
	| IF '(' expression ')' compoundStatement ELSE compoundStatement
	;

compoundStatement
	: '{' statement* '}'
	;

whileStatement
	: WHILE '(' expression ')' compoundStatement
	;

functionCall
    : WRITE '(' expression ')'
    | READ '(' IDENTIFIER ')'
    | IDENTIFIER '(' expressionList? ')'
    ;

structCall
    : IDENTIFIER ('.' IDENTIFIER)+
    ;

expressionList
    : expression (',' expression)*
    ;

expression
	: andExpr ('||' andExpr)*
	;

andExpr
	: equalityExpr ('&&' equalityExpr)*
	;

equalityExpr
  :  relationExpr (('==' | '!=') relationExpr)*
  ;

relationExpr
  :  additiveExpr (('>=' | '<=' | '>' | '<') additiveExpr)*
  ;

additiveExpr
  :  multyplicationExpr (('+' | '-') multyplicationExpr)*
  ;

multyplicationExpr
  :  atomicExpr (('*' | '/' | '%') atomicExpr)*
  ;

atomicExpr
  :  functionCall index?
  |  structCall
  |  IDENTIFIER index?
  |  primitiveExpr
  |  '(' expression ')'
  ;

primitiveExpr
  :  INTEGER_LITERAL
  |  STRING_LITERAL
  |  BOOL_LITERAL
  ;

index
  :  '[' expression ']'
  ;

globalVariableDeclaration
    : typeSpecifier  globalAssignment (',' globalAssignment)* ';'
    ;

globalAssignment
    : IDENTIFIER ('=' primitiveExpr)?
    ;

localAssignment
	: (typeSpecifier)? IDENTIFIER '=' expression
	| (typeSpecifier)? IDENTIFIER '(' expressionList ')'
	| structCall '=' expression
	;

localVariableDeclaration
	: typeSpecifier IDENTIFIER (',' IDENTIFIER)*
	;

/**------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------
 */
WHILE	: 'while' ;
RETURN  : 'return' ;
IF	: 'if' ;
ELSE	: 'else' ;
UNION : 'union' ;
STRUCT: 'struct';
WRITE : 'write' ;
READ  : 'read' ;

VOID : 'void';
INT : 'int';
BOOL : 'bool';
STRING : 'string';

BOOL_LITERAL
    : 'false'
    | 'true'
    ;

IDENTIFIER
 	: [a-zA-Z_][a-zA-Z0-9_]*;

STRING_LITERAL
	:	'"' ~["\\]* '"'
	;

INTEGER_LITERAL
    : '0'
   	| [1-9] [0-9]*
   	;

WS  :  [ \t\r\n\u000C]+ -> skip
    ;

COMMENT
    :   '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> skip
    ;