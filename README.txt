Erik Ford
CSCI 465
Pascal Compiler - Second Delivery
Fall 2019

PascalCompiler - This is the main driver program. Makes a call to IOModule to start compilation.
		 Accepts the source code file name as a command line argument. Sample run command: java PascalCompiler SourceCode.pas

IOModule - This handles all I/O to and from the user/files. This also handles data transfer between other modules of the compiler. 
	   Makes a call to Lexer to start first phase of compilation. Sends output to file 'TokenOutput.txt'.

Lexer - This performs the tokenization process by processing one character at a time from IOModule. Returns a queue of tokens to IOModule.

Token - This is an object class that represents the notion of a token with a symbol type and value.

TokenQueue - This is a FIFO queue implementation that accepts objects of type Token. Has standard queue operations.

SymbolTable - This is a hashtable implementation of the symbol table that when instantiated, initializes the hashtable with all known symbol types.

Parser - This performs the syntax and semantic analysis of the program using the TokenQueue created by the Lexer.

TypeDirectory - This acts as a lookup for all declared variables and subroutines and their types.

MiniPascalGrammar.docx - Word document containing the Mini-Pascal grammar modified to be LL(1).

Examples:
GoodSourceCode.pas - valid source code, should compile without any errors. Output located in GoodOutput.txt.
BadSourceCode.pas - invalid source code with a missing else statement. Output located in BadOutput.txt.