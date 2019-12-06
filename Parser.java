//Erik Ford
//CSCI 465
//Compiler Project
//last updated 11-14-19

/***********************************************************
 * @author Erik Ford
 * Parser performs the syntax analysis phase of compilation
 ***********************************************************/
public class Parser 
{
	public static int lineNumber = 1;
   /******************************************************
    * This method copies the original TokenQueue and calls
      the program symbol of the grammar
    * @param inTokens - TokenQueue of program source code
    * @returns true if valid, false if invalid
    ******************************************************/
	public static boolean start(TokenQueue inTokens)
	{
		TokenQueue tokens = new TokenQueue(inTokens);
		if (program(tokens))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the program symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean program(TokenQueue tokens)
   {//RULE: start --> <program> <id> <;> declarations compound_statement <.>
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return program(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("program_symbol")) //<program>
	   {
		   tokens.pop();
		   if (tokens.peek() != null && tokens.peek().getSymbol().equals("identifier_symbol")) //<id>
		   {
			   tokens.pop();
			   if (tokens.peek() != null && tokens.peek().getSymbol().equals("semicolon_symbol")) //<;>
			   {
			      tokens.pop();
				  if (declarations(tokens)) //declarations
				  {
				     if (compound_statement(tokens))
					 {
					    if (tokens.peek() != null && tokens.peek().getSymbol().equals("period_symbol")) //<.>
						{
						   return true;
						}
						else //next symbol is not <.>
						{
						   System.out.println("ERROR: symbol '.' expected on line " + lineNumber + " before " + tokens.peek().getText());
						   return false;
						}
					 }
					 else //compound_statement is not valid
					 {
					    System.out.println("ERROR: invalid compound_statement on line " + lineNumber);
						return false;
					 }
				 }
				 else //declarations are not valid
				 {
				    System.out.println("ERROR: invalid declarations on line " + lineNumber);
					return false;
				 }
			 }
			 else //next symbol is not <;>
			 {
			    System.out.println("ERROR: symbol ';' expected on line " + lineNumber + " before " + tokens.peek().getText());
				return false;
			 }
		  }
		  else //next symbol is not <id>
		  {
		     System.out.println("ERROR: identifier expected on line " + lineNumber + " before " + tokens.peek().getText());
			 return false;
		  }
	   }
	   else //first symbol is not <program>
	   {
		   System.out.println("ERROR: keyword 'program' expected on line " + lineNumber + " before " + tokens.peek().getText());
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the identifier_list symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean identifier_list(TokenQueue tokens)
   {//RULE: identifier_list --> <id> more_identifiers
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return identifier_list(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("identifier_symbol"))
	   {
		   tokens.pop();
		   if (more_identifiers(tokens))
		   {
			   return true;
		   }
		   else
		   {
			   System.out.println("ERROR: invalid more_identifiers on line " + lineNumber);
			   return false;
		   }
	   }
	   else //next symbol is not <id>
	   {
		   System.out.println("ERROR: identifier expected on line " + lineNumber + " before " + tokens.peek().getText());
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the more_identifiers symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean more_identifiers(TokenQueue tokens)
   {//RULE: more_identifiers --> <,> identifier_list | e
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return more_identifiers(tokens);
	   }

	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("comma_symbol")) //<,>
	   {
		   tokens.pop();
		   if (identifier_list(tokens)) //identifier_list
		   {
			   return true;
		   }
		   else //identifier_list is not valid
		   {
			   System.out.println("ERROR: invalid identifier_list on line " + lineNumber);
			   return false;
		   }
	   }
	   else //empty
	   {
		   return true;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the declarations symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean declarations(TokenQueue tokens)
   {//RULE: declarations --> <var> identifier_list <:> type <;> declarations | e
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return declarations(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("var_symbol")) //<var>
	   {
		   tokens.pop();
		   if (identifier_list(tokens)) //identifier_list
		   {
			   if (tokens.peek().getSymbol().equals("colon_symbol")) //<:>
			   {
				   tokens.pop();
				   if (type(tokens)) //type
				   {
					   if (tokens.peek().getSymbol().equals("semicolon_symbol")) //<;>
					   {
						   tokens.pop();
						   if (declarations(tokens)) //declarations
						   {
							   return true;
						   }
						   else //declarations are not valid
						   {
							   System.out.println("ERROR: invalid declarations on line " + lineNumber);
							   return false;
						   }
					   }
					   else //next symbol is not <;>
					   {
						   System.out.println("ERROR: symbol ';' expected on line " + lineNumber + " before " + tokens.peek().getText());
						   return false;
					   }
				   }
				   else //invalid type
				   {
					   System.out.println("ERROR: invalid type (3) on line " + lineNumber);
					   return false;
				   }
			   }
			   else //next symbol is not <:>
			   {
				   System.out.println("ERROR: symbol ':' expected on line " + lineNumber + " before " + tokens.peek().getText());
				   return false;
			   }
		   }
		   else //identifier_list is not valid
		   {
			   System.out.println("ERROR: invalid identifier_list on line " + lineNumber);
			   return false;
		   }		   
	   }
	   else //empty
	   {
		   return true;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the type symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean type(TokenQueue tokens)
   {//RULE: type --> standard_type | <array> <[> <num> <.> <.> <num> <]> <of> standard_type
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return type(tokens);
	   }
	   if (standard_type(tokens)) //standard_type
	   {
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("array_symbol")) //<array>
	   {
		   tokens.pop();
		   if (tokens.peek() != null && tokens.peek().getSymbol().equals("lbrack_symbol")) //<[>
		   {
			   tokens.pop();
			   if (tokens.peek() != null && tokens.peek().getSymbol().equals("number_symbol")) //<num>
			   {
				   tokens.pop();
				   if (tokens.peek() != null && tokens.peek().getSymbol().equals("dotdot_symbol")) //<..>
				   {
						   tokens.pop();
						   if (tokens.peek() != null && tokens.peek().getSymbol().equals("number_symbol")) //<num>
						   {
							   tokens.pop();
							   if (tokens.peek() != null && tokens.peek().getSymbol().equals("rbrack_symbol")) //<]>
							   {
								   tokens.pop();
								   if (tokens.peek() != null && tokens.peek().getSymbol().equals("of_symbol")) //<of>
								   {
									   tokens.pop();
									   if (standard_type(tokens)) //standard_type
									   {
										   return true;
									   }
									   else //standard_type is invalid
									   {
										   System.out.println("ERROR: invalid standard_type on line " + lineNumber);
										   return false;
									   }
								   }
								   else //next symbol is not <of>
								   {
									   System.out.println("ERROR: missing keyword 'of'");
									   return false;
								   }
							   }
							   else //next symbol is not <]>
							   {
								   System.out.println("ERROR: symbol ']' expected on line " + lineNumber + " before " + tokens.peek().getText());
								   return false;
							   }
						   }
						   else //next symbol is not a number
						   {
							   System.out.println("ERROR: number expected on line " + lineNumber + " before " + tokens.peek().getText());
							   return false;
						   }
				   }				   
				   else //next symbol is not <..>
				   {
					   System.out.println("ERROR: symbol '..' expected on line " + lineNumber + " before " + tokens.peek().getText());
					   return false;
				   }
			   }
			   else //next symbol is not a number
			   {
				   System.out.println("ERROR: number expected on line " + lineNumber + " before " + tokens.peek().getText());
				   return false;
			   }
		   }
		   else //next symbol is not <[>
		   {
			   System.out.println("ERROR: symbol '[' expected on line " + lineNumber + " before " + tokens.peek().getText());
			   return false;
		   }
	   }
	   else //invalid type
	   {
		   System.out.println("ERROR: invalid type (1) on line " + lineNumber);
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the standard_type symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean standard_type(TokenQueue tokens)
   {//RULE: standard_type --> <integer> | <real> | <char>
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return standard_type(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("integer_symbol")) //<integer>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("real_symbol")) //<real>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("char_symbol")) //<char>
	   {
		   tokens.pop();
		   return true;
	   }
	   else //next symbol is not <integer> or <real> or <char>
	   {
		   //System.out.println("ERROR: invalid type (2) on line " + lineNumber);
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the compound_statement symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean compound_statement(TokenQueue tokens)
   {//RULE: compound_statement --> <begin> statement_list <end>
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return compound_statement(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("begin_symbol")) //<begin>
	   {
		   tokens.pop();
		   if (statement_list(tokens)) //statement_list
		   {
			   if (tokens.peek() != null && tokens.peek().getSymbol().equals("end_symbol")) //<end>
			   {
				   tokens.pop();
				   return true;
			   }
			   else //next symbol is not <end>
			   {
				   System.out.println("ERROR: keyword 'end' expected on line " + lineNumber + " before " + tokens.peek().getText());
				   return false;
			   }
		   }
		   else //invalid statement_list
		   {
			   System.out.println("ERROR: invalid statement_list on line " + lineNumber);
			   return false;
		   }
	   }
	   else //not a compound statement
	   {
		   //System.out.println("ERROR: invalid compound_statement on line " + lineNumber);
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the statement_list symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean statement_list(TokenQueue tokens)
   {//RULE: statement_list --> statement <;> statement_list | e
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return statement_list(tokens);
	   }
	   if (statement(tokens)) //statement
	   {
		   if (tokens.peek() != null && tokens.peek().getSymbol().equals("semicolon_symbol")) //<;>
		   {
			   tokens.pop();
			   if (statement_list(tokens)) //statement_list
			   {
				   return true;
			   }
			   else //statement_list is not valid
			   {
				   System.out.println("ERROR: invalid statement_list on line " + lineNumber);
				   return false;
			   }
		   }
		   else //more_statements invalid
		   {
			   System.out.println("ERROR: missing ';' on line " + lineNumber);
			   return false;
		   }
	   }
	   else //empty
	   {
		   return true;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the statement symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean statement(TokenQueue tokens)
   {//RULE: statement --> variable <:=> expression | procedure_statement | compound_statement |
	                    //<if> boolean_expression <then> statement <else> statement | 
	                    //<while> boolean_expression <do> statement | read_statement | write_statement
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return statement(tokens);
	   }
	   if (variable(tokens)) //variable
	   {
	      if (tokens.peek() != null && tokens.peek().getSymbol().equals("assign_symbol")) //<:=>
		  {
		     tokens.pop();
			 if (expression(tokens)) //expression
			 {
			    return true;
			 }
			 else //expression is invalid
			 {
			    System.out.println("ERROR: invalid expression on line " + lineNumber);
				return false;
			 }
		  }
		  else //next symbol is not <:=>
		  {
		     System.out.println("ERROR: assignment operator ':=' expected on line " + lineNumber + " before " + tokens.peek().getText());
			 return false;
		  }
	   }
	   else if (compound_statement(tokens)) //compound_statement
	   {
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("if_symbol")) //<if>
	   {
		   tokens.pop();
		   if (boolean_expression(tokens)) //boolean_expression
		   {
			   if (tokens.peek() != null && tokens.peek().getSymbol().equals("then_symbol")) //<then>
			   {
				   tokens.pop();
				   if (statement(tokens)) //statement
				   {
					   if (tokens.peek() != null && tokens.peek().getSymbol().equals("else_symbol")) //<else>
					   {
						   tokens.pop();
						   if (statement(tokens))
						   {
							   return true;
						   }
						   else //statement is invalid
						   {
							   System.out.println("ERROR: invalid statement (1) on line " + lineNumber);
							   return false;
						   }
					   }
					   else //next symbol is not <else>
					   {
						   System.out.println("ERROR: keyword 'else' expected on line " + lineNumber + " before " + tokens.peek().getText());
						   return false;
					   }
				   }
				   else //statement is invalid
				   {
					   System.out.println("ERROR: invalid statement (2) on line " + lineNumber);
					   return false;
				   }
			   }
			   else //next symbol is not <then>
			   {
				   System.out.println("ERROR: keyword 'then' expected on line " + lineNumber + " before " + tokens.peek().getText());
				   return false;
			   }
		   }
		   else //boolean is invalid
		   {
			   System.out.println("ERROR: invalid boolean expression on line " + lineNumber);
			   return false;
		   }
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("while_symbol")) //<while>
	   {
		   tokens.pop();
		   if (boolean_expression(tokens)) //boolean_expression
		   {
			   if (tokens.peek() != null && tokens.peek().getSymbol().equals("do_symbol")) //<do>
			   {
				   tokens.pop();
				   if (statement(tokens)) //statement
				   {
					   return true;
				   }
				   else //statement is invalid
				   {
					   System.out.println("ERROR: invalid statement (3) on line " + lineNumber);
					   return false;
				   }
			   }
			   else //next symbol is not <do>
			   {
				   System.out.println("ERROR: keyword 'do' expected on line " + lineNumber + " before " + tokens.peek().getText());
				   return false;
			   }
		   }
		   else //invalid boolean
		   {
			   System.out.println("ERROR: invalid boolean expression on line " + lineNumber);
			   return false;
		   }
	   }
	   else if (read_statement(tokens)) //read_statement
	   {
		   return true;
	   }
	   else if (write_statement(tokens)) //write_statement
	   {
		   return true;
	   }
	   else //not a statement
	   {
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the variable symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean variable(TokenQueue tokens)
   {//RULE: variable --> <id> index
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return variable(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("identifier_symbol")) //<id>
	   {
		   tokens.pop();		   
		   if (index(tokens)) //index
		   {
			   return true;
		   }
		   else //not a valid index
		   {
			   System.out.println("ERROR: invalid index on line " + lineNumber);
			   return false;
		   }
	   }
	   else //not a variable
	   {
		   //System.out.println("ERROR: invalid variable on line " + lineNumber);
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the index symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean index(TokenQueue tokens)
   {//RULE: index --> <[> expression <]> | e
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return index(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("lbrack_symbol")) //<[>
	   {
		   tokens.pop();
		   if (expression(tokens)) //expression
		   {
			   if (tokens.peek() != null && tokens.peek().getSymbol().equals("rbrack_symbol")) //<]>
			   {
				   tokens.pop();
				   return true;
			   }
			   else //next symbol is not <]>
			   {
				   System.out.println("ERROR: symbol ']' expected on line " + lineNumber + " before " + tokens.peek().getText());
				   return false;
			   }
		   }
		   else //expression is invalid
		   {
			   System.out.println("ERROR: invalid expression on line " + lineNumber);
			   return false;
		   }
	   }
	   else //empty
	   {
		   return true;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the expression symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean expression(TokenQueue tokens)
   {//RULE: expression --> simple_expression rest_of_expression
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return expression(tokens);
	   }
	   if (simple_expression(tokens)) //simple_expression
	   {
		   if (rest_of_expression(tokens)) //rest_of_expression
		   {
			   return true;
		   }
		   else //rest_of_expression is not valid
		   {
			   System.out.println("ERROR: invalid rest_of_expression on line " + lineNumber);
			   return false;
		   }
	   }
	   else //not an expression
	   {
		   //System.out.println("ERROR: invalid expression on line " + lineNumber);
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the rest_of_expression symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean rest_of_expression(TokenQueue tokens)
   {//RULE: rest_of_expression --> relop simple_expression | e
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return rest_of_expression(tokens);
	   }
	   if (relop(tokens)) //relop
	   {
		   if (simple_expression(tokens)) //simple_expression
		   {
			   return true;
		   }
		   else //simple_expression is invalid
		   {
			   System.out.println("ERROR: invalid simple_expression on line " + lineNumber);
			   return false;
		   }
	   }
	   else //empty
	   {
		   return true;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the simple_expression symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean simple_expression(TokenQueue tokens)
   {//RULE: simple_expression --> sign term rest_of_simple_expression
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return simple_expression(tokens);
	   }
	   if (sign(tokens)) //sign
	   {
		   if (term(tokens)) //term
		   {
			   if (rest_of_simple_expression(tokens)) //rest_of_simple_expression
			   {
				   return true;
			   }
			   else //rest_of_simple_expression invalid
			   {
				   System.out.println("ERROR: invalid rest_of_simple_expression on line " + lineNumber);
				   return false;
			   }
		   }
		   else //term is invalid
		   {
			   System.out.println("ERROR: invalid term on line " + lineNumber);
			   return false;
		   }
	   }
	   else //not a simple_expression
	   {
		   //System.out.println("ERROR: invalid simple_expression on line " + lineNumber);
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the rest_of_simple_expression symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean rest_of_simple_expression(TokenQueue tokens)
   {//RULE: rest_of_simple_expression --> addop term | e
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return rest_of_simple_expression(tokens);
	   }
	   if (addop(tokens)) //addop
	   {
		   if (term(tokens)) //term
		   {
			   return true;
		   }
		   else //term is invalid
		   {
			   System.out.println("ERROR: invalid term on line " + lineNumber);
			   return false;
		   }		   
	   }
	   else //empty
	   {
		   return true;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the sign symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean sign(TokenQueue tokens)
   {//RULE: sign --> <+> | <-> | e
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return sign(tokens);
	   }
	   if (tokens.peek() != null && 
		   tokens.peek().getSymbol().equals("plus_symbol") ||
		   tokens.peek().getSymbol().equals("minus_symbol")) //<+> || <->
	   {
		   tokens.pop();
		   return true;
	   }
	   else //empty
	   {
		   return true;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the term symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean term(TokenQueue tokens)
   {//RULE: term --> factor rest_of_term
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return term(tokens);
	   }
	   if (factor(tokens)) //factor
	   {
		   if (rest_of_term(tokens)) //rest_of_term
		   {
			   return true;
		   }
		   else //rest_of_term not valid
		   {
			   System.out.println("ERROR: invalid rest_of_term on line " + lineNumber);
			   return false;
		   }
	   }
	   else //not a term
	   {
		   //System.out.println("ERROR: invalid term on line " + lineNumber);
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the rest_of_term symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean rest_of_term(TokenQueue tokens)
   {//RULE: rest_of_term --> multop term | e
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return rest_of_term(tokens);
	   }
	   if (multop(tokens)) //multop
	   {
		   if (term(tokens)) //term
		   {
			   return true;
		   }
		   else //term is not valid
		   {
			   System.out.println("ERROR: invalid term on line " + lineNumber);
			   return false;
		   }
	   }
	   else //empty
	   {
		   return true;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the start symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean factor(TokenQueue tokens)
   {//RULE: factor --> variable | <num> | <litchar> | <(> expression <)> | <not> factor
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return factor(tokens);
	   }
	   if (variable(tokens)) //variable
	   {
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("number_symbol")) //<num>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("litchar_symbol")) //<litchar>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("lparen_symbol")) //<(>
	   {
		   tokens.pop();
		   if (expression(tokens)) //expression
		   {
			   if (tokens.peek() != null && tokens.peek().getSymbol().equals("rparen_symbol")) //<)>
			   {
				   tokens.pop();
				   return true;
			   }
			   else //next symbol is not <)>
			   {
				   System.out.println("ERROR: symbol ')' expected on line " + lineNumber + " before " + tokens.peek().getText());
				   return false;
			   }
		   }
		   else //invalid expression
		   {
			   System.out.println("ERROR: invalid expression on line " + lineNumber);
			   return false;
		   }
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("not_symbol")) //<not>
	   {
		   tokens.pop();
		   if (factor(tokens)) //factor
		   {
			   return true;
		   }
		   else //invalid factor
		   {
			   System.out.println("ERROR: invalid factor on line " + lineNumber);
			   return false;
		   }
	   }
	   else //not a factor
	   {
		   //System.out.println("ERROR: invalid factor on line " + lineNumber);
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the read_statement symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean read_statement(TokenQueue tokens)
   {//RULE: read_statement --> <read> <(> variable <)>
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return read_statement(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("read_symbol")) //<read>
	   {
		   tokens.pop();
		   if (tokens.peek() != null && tokens.peek().getSymbol().equals("lparen_symbol")) //<(>
		   {
			   tokens.pop();
			   if (variable(tokens)) //variable
			   {
				   if (tokens.peek() != null && tokens.peek().getSymbol().equals("rparen_symbol")) //<)>
				   {
					   tokens.pop();
					   return true;
				   }
				   else
				   {
					   System.out.println("ERROR: symbol ')' expected on line " + lineNumber);
					   return false;
				   }
			   }
			   else //invalid variable
			   {
				   System.out.println("ERROR: invalid variable on line " + lineNumber);
				   return false;
			   }
		   }
		   else //next symbol is not <(>
		   {
			   System.out.println("ERROR: symbol '(' expected on line " + lineNumber);
			   return false;
		   }
	   }
	   else //not a read_statement
	   {
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the write_statement symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean write_statement(TokenQueue tokens)
   {//RULE: write_statement --> <write> <(> write_contents <)> | <writeln> <(> write_contents <)>
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return write_statement(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("write_symbol")) //<write>
	   {
		   tokens.pop();
		   if (tokens.peek() != null && tokens.peek().getSymbol().equals("lparen_symbol")) //<(>
		   {
			   tokens.pop();
			   if (write_contents(tokens)) //write_contents
			   {
				   if (tokens.peek() != null && tokens.peek().getSymbol().equals("rparen_symbol")) //<)>
				   {
					   tokens.pop();
					   return true;
				   }
				   else
				   {
					   System.out.println("ERROR: symbol ')' expected on line " + lineNumber);
					   return false;
				   }
			   }
			   else //invalid write_contents
			   {
				   System.out.println("ERROR: invalid write_contents on line " + lineNumber);
				   return false;
			   }
		   }
		   else //next symbol is not <(>
		   {
			   System.out.println("ERROR: symbol '(' expected on line " + lineNumber);
			   return false;
		   }
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("writeln_symbol")) //<writeln>
	   {
		   tokens.pop();
		   if (tokens.peek() != null && tokens.peek().getSymbol().equals("lparen_symbol")) //<(>
		   {
			   tokens.pop();
			   if (write_contents(tokens)) //write_contents
			   {
				   if (tokens.peek() != null && tokens.peek().getSymbol().equals("rparen_symbol")) //<)>
				   {
					   tokens.pop();
					   return true;
				   }
				   else
				   {
					   System.out.println("ERROR: symbol ')' expected on line " + lineNumber);
					   return false;
				   }
			   }
			   else //invalid write_contents
			   {
				   System.out.println("ERROR: invalid write_contents on line " + lineNumber);
				   return false;
			   }
		   }
		   else //next symbol is not <(>
		   {
			   System.out.println("ERROR: symbol '(' expected on line " + lineNumber);
			   return false;
		   }
	   }
	   else //not a write_statement
	   {
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the write_contents symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean write_contents(TokenQueue tokens)
   {//RULE: write_contents --> <litchar> | <quotestring> | simple_expression
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return write_contents(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("litchar_symbol")) //<litchar>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("quotestring_symbol")) //<quotestring>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (simple_expression(tokens)) //simple_expression
	   {
		   return true;
	   }
	   else //not valid write_contents
	   {
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the boolean_expression symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean boolean_expression(TokenQueue tokens)
   {//RULE: boolean_expression --> expression rest_of_boolean
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return boolean_expression(tokens);
	   }
	   if (expression(tokens)) //expression
	   {
		   if (rest_of_boolean(tokens)) //rest_of_boolean
		   {
			   return true;
		   }
		   else //invalid rest_of_boolean
		   {
			   System.out.println("ERROR: Invalid rest_of_boolean on line " + lineNumber);
			   return false;
		   }
	   }
	   else //not valid boolean_expression
	   {
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the rest_of_boolean symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean rest_of_boolean(TokenQueue tokens)
   {//RULE: rest_of_boolean --> <and> boolean_expression | <or> boolean_expression | <empty>
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return boolean_expression(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("and_symbol")) //<and>
	   {
		   tokens.pop();
		   if (boolean_expression(tokens)) //boolean_expression
		   {
			   return true;
		   }
		   else //invalid boolean_expression
		   {
			   System.out.println("ERROR: invalid boolean_expression on line " + lineNumber);
			   return false;
		   }
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("or_symbol")) //<or>
	   {
		   tokens.pop();
		   if (boolean_expression(tokens)) //boolean_expression
		   {
			   return true;
		   }
		   else //invalid boolean_expression
		   {
			   System.out.println("ERROR: invalid boolean_expression on line " + lineNumber);
			   return false;
		   }
	   }
	   else //empty
	   {
		   return true;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the multop symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean multop(TokenQueue tokens)
   {//RULE: multop --> <div> | <*> | <mod>
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return boolean_expression(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("div_symbol")) //<div>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("times_symbol")) //<*>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("mod_symbol")) //<mod>
	   {
		   tokens.pop();
		   return true;
	   }
	   else //not a valid multop
	   {
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the addop symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean addop(TokenQueue tokens)
   {//RULE: addop --> <+> | <->
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return boolean_expression(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("plus_symbol")) //<+>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("minus_symbol")) //<->
	   {
		   tokens.pop();
		   return true;
	   }
	   else //not a valid addop
	   {
		   return false;
	   }
   }
   
   /******************************************************
    * This method implements the Mini-Pascal production
      rules for the relop symbol of the grammar
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean relop(TokenQueue tokens)
   {//RULE: relop --> <=> | <<>> | <<> | <<=> | <>> | <>=>
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) //new line
	   {
		   tokens.pop();
		   lineNumber++;
		   return boolean_expression(tokens);
	   }
	   if (tokens.peek() != null && tokens.peek().getSymbol().equals("equal_symbol")) //<=>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("notequal_symbol")) //<<>>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("less_symbol")) //<<>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("lessequal_symbol")) //<<=>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("greater_symbol")) //<>>
	   {
		   tokens.pop();
		   return true;
	   }
	   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("greaterequal_symbol")) //<>=>
	   {
		   tokens.pop();
		   return true;
	   }
	   else //not a valid relop
	   {
		   return false;
	   }
   }
}
