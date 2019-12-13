//Erik Ford
//CSCI 465
//Compiler Project
//last updated 11-14-19

/***********************************************************
 * @author Erik Ford
 * TypeChecker performs the semantic analysis phase of compilation
 ***********************************************************/
public class TypeChecker 
{
	private static int lineNumber = 1;
	private static TypeDirectory td = new TypeDirectory();
	
   /******************************************************
    * This method copies the original TokenQueue and calls
      the program symbol of the grammar
    * @param inTokens - TokenQueue of program source code
    * @returns true if valid, false if invalid
    ******************************************************/
	public static boolean start(TokenQueue inTokens)
	{
		TokenQueue tokens = new TokenQueue(inTokens);
		if (skipToDeclarations(tokens))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static TypeDirectory getTypes(TokenQueue inTokens)
	{
		TokenQueue tokens = new TokenQueue(inTokens);
		if (skipToDeclarations(tokens))
		{
			return td;
		}
		else
		{
			return null;
		}
	}
	
   /******************************************************
    * This method skips until the first declaration
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean skipToDeclarations(TokenQueue tokens)
   {
	   while (tokens.peek() != null && !tokens.peek().getSymbol().equals("var_symbol"))
	   {
		   if (tokens.peek().getSymbol().equals("newline_symbol")) //new line
		   {
			   lineNumber++;
		   }
		   tokens.pop();
	   }
	   
	   return declarations(tokens);
	   
	    
   }
   
   /******************************************************
    * This method processes and inserts all declared
      variables into the TypeDictionary
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean declarations(TokenQueue tokens)
   {
	   //parse through declarations
	   while (true)
	   {
		   if (tokens.peek() != null && tokens.peek().getSymbol().equals("var_symbol")) //pop <var>
		   {
			   tokens.pop();
		   }
		   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("identifier_symbol")) //push id to td stack
		   {
			   Token t = tokens.pop();
			   td.push(t);
		   }
		   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("comma_symbol")) //pop <,>
		   {
			   tokens.pop();
		   }
		   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("colon_symbol")) //pop <:>
		   {
			   tokens.pop();
			   //send type and flush stack
			   String type = tokens.pop().getText();
			   if (type.equals("array"))
			   {
				   tokens.pop(); //[
				   tokens.pop(); //num
				   tokens.pop(); //..
				   tokens.pop(); //num
				   tokens.pop(); //]
				   tokens.pop(); //of
				   type += "_" + tokens.pop().getText(); //type
			   }
			   td.flush(type);
			   
		   }
		   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("semicolon_symbol")) //pop <;>
		   {
			   tokens.pop();
			   
		   }
		   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol")) ///increment line counter
		   {
			   tokens.pop();
			   lineNumber++;
		   }
		   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("begin_symbol")) //exit loop
		   {
			   break;
		   }
		   else //unexpected token
		   {
			   return false;
		   }
		}
	   
	   return assignments(tokens);
	}

   
   /******************************************************
    * This method verifies type compatibility of all
      assignment statements
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean assignments(TokenQueue tokens)
   {
	   Token t = tokens.pop();
	   while (tokens.peek() != null)
	   {
		   if (tokens.peek().getSymbol().equals("assign_symbol"))
		   {
			   tokens.pop(); //<:=>
			   String leftType = td.get(t.getText()); //id before <:=>
			   if (leftType.contains("array"))
			   {
				   leftType = leftType.substring(leftType.lastIndexOf("_") + 1);
				   
			   }
			   String rightType = evalExpression(tokens);

			   if (!validateType(leftType, rightType))
			   {
				   System.out.println("ERROR: type incompatibility on line " + lineNumber);
				   return false;
			   }
		   }
		   else if (tokens.peek().getSymbol().equals("lbrack_symbol"))
		   {
			   while (!tokens.pop().getSymbol().equals("rbrack_symbol"));
		   }
		   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("newline_symbol"))
		   {
			   tokens.pop();
			   lineNumber++;
		   }
		   else
		   {
			   t = tokens.pop();
		   }
		   
	   }
	   return true;
   }
   
   /******************************************************
    * This method evaluates the type of an expression
      on the RHS of an assignment statement
    * @param tokens - TokenQueue of program source code 
    * @returns String containing type of expression
    ******************************************************/
   public static String evalExpression(TokenQueue tokens)
   {
	   while (true)
	   {
		   if (tokens.peek() != null && tokens.peek().getSymbol().equals("identifier_symbol"))
		   {
			   String type1 = td.get(tokens.pop().getText());
			   if (type1.contains("array"))
			   {
				   type1 = type1.substring(type1.lastIndexOf("_") + 1);
				   while (!tokens.pop().getSymbol().equals("rbrack_symbol"));
				   
			   }
			   if (tokens.peek() != null && (tokens.peek().getSymbol().equals("semicolon_symbol")
					   || tokens.peek().getSymbol().equals("else_symbol")
					   || tokens.peek().getSymbol().equals("end_symbol")))
			   {
				   return type1;
			   }
			   else //multop or addop
			   {
				   tokens.pop();
				   String type2 = evalExpression(tokens);
				   return calcType(type1, type2);
			   }
		   }
		   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("litchar_symbol"))
		   {
			   tokens.pop();
			   String type1 = "char";
			   if (tokens.peek() != null && tokens.peek().getSymbol().equals("semicolon_symbol"))
			   {
				   return type1;
			   }
			   else //multop or addop
			   {
				   tokens.pop();
				   String type2 = evalExpression(tokens);
				   return calcType(type1, type2);
			   }
		   }
		   else if (tokens.peek() != null && tokens.peek().getSymbol().equals("number_symbol"))
		   {
			   String type1;
			   if (tokens.pop().getText().contains("."))
			   {
				   type1 = "real";
			   }
			   else
			   {
				   type1 = "integer";
			   }
			   
			   if (tokens.peek() != null && tokens.peek().getSymbol().equals("semicolon_symbol"))
			   {
				   return type1;
			   }
			   else //multop or addop
			   {
				   tokens.pop();
				   String type2 = evalExpression(tokens);
				   return calcType(type1, type2);
			   }
		   }
		   
		   else
		   {
			   tokens.pop();
		   }
	   }
	   
   }
   
   /******************************************************
    * This method calculates the type of an expression
      given the two operands' types
    * @param type1 - type of left operand
    * @param type2 - type of right operand
    * @returns String containing type of expression
    ******************************************************/
   public static String calcType(String type1, String type2)
   {
	   if (type1.equals("char") || type2.equals("char") || type1.equals("invalid") || type2.equals("invalid"))
	   {
		   return "invalid";
	   }
	   else if (type1.equals("int") && type2.equals("int"))
	   {
		   return "integer";
	   }
	   else
	   {
		   return "real";
	   }
   }
   
   /******************************************************
    * This method calculates the type of an expression
      given the two operands' types
    * @param type1 - type of left operand
    * @param type2 - type of right operand
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean validateType(String type1, String type2)
   {
	   if (type1.equals("char") && type2.equals("char"))
	   {
		   return true;
	   }
	   else if (type1.equals("char") || type2.equals("char") || type1.equals("invalid") || type2.equals("invalid"))
	   {
		   return false;
	   }
	   else
	   {
		   return true;
	   }
   }
   
   
}
