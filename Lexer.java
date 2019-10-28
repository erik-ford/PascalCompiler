//Erik Ford
//CSCI 465
//Compiler Project
//last updated 9-26-19

/************************************************
 * @author Erik Ford
 * Lexer class parses Mini Pascal source code
   to create a stream of corresponding Tokens.
 ************************************************/
public class Lexer
{
   private static String currToken = ""; //token buffer
   private static SymbolTable st = new SymbolTable(); //symbol table
   
   /*************************************************************
    * main driver method for Lexer portion of PascalCompiler
    * @return a full TokenQueue representing the source
    *************************************************************/
   public static TokenQueue getTokens()
   {
	   //declaration of variables
	   char nextChar;
	   Token tempToken = null;
	   TokenQueue tokens = new TokenQueue();
	   int lineNumber = 1;
	   
	   //get next char from I/O and add to current token
	   nextChar = ' ';
	   while (nextChar != 'µ')
	   {
		   nextChar = IOModule.getNextChar();
		   if (nextChar == '\n')
		   {
			   lineNumber++;
		   }
		   
		   tempToken = buildToken(nextChar);
		   if (tempToken != null)
		   {
			   if (tempToken.getSymbol().equals("illegal_symbol"))
			   {
				   System.out.println("Illegal token ( " + tempToken.getText() + " ) on line " + lineNumber);
			   }
			   if (tempToken.getSymbol().equals("illegalString_symbol"))
			   {
				   System.err.println("Missing closing ' on " + lineNumber);
			   }
			   else //valid token
			   {
				   tokens.push(tempToken);
			   }
		   }
	   }
	   
	   return tokens;
	   
	   //end of file reached
	   
   }
   
   /******************************************************
    * Builds token one char at a time, checking
      for token forms along the way.
    * @param char c, the char to be added to the token
    * @return a Token if complete, null if incomplete
    ******************************************************/
   public static Token buildToken(char c)
   {
	   if (Character.isWhitespace(c)) //end of current token
	   {
		   //validate current token and flush buffer
		   String temp = currToken;
		   currToken = "";
		   return validateToken(temp);
	   }
	   
	   if (currToken.equals("")) //start of a new token
	   {
		   currToken += c; //add current char to buffer
	   }
	   else if (c == '{') //check for comment block {...} 
	   {
		   skipComments("{");
		   
		   //validate current token and flush buffer
		   String temp = currToken;
		   currToken = "";
		   return validateToken(temp);
	   }
	   else if (currToken.equals("(")) //check for comment block (*...*)
	   {
		   if (c == '*')
		   {
			   skipComments("(*");
			   currToken = ""; //flush buffer
		   }
		   else //just a parenthesis
		   {
			   //validate current token and flush buffer
			   String temp = currToken;
			   currToken = Character.toString(c);
			   return validateToken(temp);
		   }
	   }
	   else if (currToken.equals("'")) //check for quotestring/litchar '...'
	   {
		   return parseString(c);
	   }
	   else if (currToken.matches("\\d+")) //current token is a number
	   {
		   if (Character.isDigit(c))
		   {
			   currToken += c;
		   }
		   else //c is not a digit
		   {
			   //validate current token and flush buffer
			   String temp = currToken;
			   currToken = Character.toString(c);
			   return validateToken(temp);
		   }
	   }
	   else if (currToken.matches("\\W+")) //current token is string of symbols
	   {
		   if (c == ';' || c == ':' ||  c == '(' || c == ')')
		   {
			   //validate current token and flush buffer
			   String temp = currToken;
			   currToken = Character.toString(c);
			   return validateToken(temp);
		   }
		   else if (!Character.isLetterOrDigit(c)) //character is symbol
		   {
			   currToken += c;
		   }
		   else //character is not a symbol
		   {
			   //validate current token and flush buffer
			   String temp = currToken;
			   currToken = Character.toString(c);
			   return validateToken(temp);
		   }
	   }
	   else if (currToken.matches("[a-zA-Z]\\w*")) //current token is identifier/reserved word
	   {
		   if (Character.isLetterOrDigit(c))
		   {
			   currToken += c;
		   }
		   else //c is not alphanumeric
		   {
			   //validate current token and flush buffer
			   String temp = currToken;
			   currToken = Character.toString(c);
			   return validateToken(temp);
		   }
	   }
	   return null; //current token is not complete
   }
   
   /********************************************************
    * Checks if currToken is a valid token
    * @return a Token object to be added to the TokenQueue,
              or illegal token if not valid token type,
              or null if whitespace token
    ********************************************************/
   public static Token validateToken(String inToken)
   {
	   String type = st.get(inToken);
	   
	   if (type != null) //token is in symbol table
	   {
		   return new Token(inToken, type);
	   }
	   else //token not found in symbol table
	   {
		   if (inToken.matches("[a-zA-Z]\\w*")) //token is valid identifier
		   {
			   Token newToken =  new Token(inToken, "identifier_symbol");
			   st.put(newToken);
			   return newToken;
			   
		   }
		   else if (inToken.matches("\\d+")) //token is valid number
		   {
			   Token newToken =  new Token(inToken, "number_symbol");
			   st.put(newToken);
			   return newToken;
		   }
		   else if (inToken.matches("\\s*"))
		   {
			   return null;
		   }
		   else
		   {
			   return new Token (inToken, "illegal_symbol");
		   }
	   }
   }
   
   /*********************************************************
    * Parses through char stream to bypass comment blocks
    * @param type - type of comment block to look for
    *********************************************************/
   public static void skipComments(String type)
   {
	   if (type.equals("{")) //checking for comment block {...}
	   {
		   while (!type.contains("}"))
		   {
			   type += IOModule.getNextChar();
			   
			   if (type.contains("µ")) {return;} //end of file
		   }
	   }
	   else if (type.equals("(*")) //checking for comment block (*...*)
	   {
		   while (!type.contains("*)"))
		   {
			   type += IOModule.getNextChar();
			   
			   if (type.contains("Îµ")) {return;} //end of file
		   }
	   }
	   else if (type != null)
	   {
		   return;
	   }
   }
   
   /************************************************************
    * Parses through char stream to get string/char token
    * @param c - next char in sequence
    * @return - a new Token for the single quoted string/char
    ************************************************************/
   public static Token parseString(char c)
   {
	   String str = "'" + c;
	   while (true)
	   {
		   str += IOModule.getNextChar();
		   if (str.matches("'[\\w\\W]+'")) break;
	   }
	   
	   currToken = "";
	   
	   if (str.length() == 3)
	   {
		   Token newToken = new Token (str, "litchar_symbol");
		   st.put(newToken);
		   return newToken;
	   }
	   else
	   {
		   Token newToken = new Token (str, "quotestring_symbol");
		   st.put(newToken);
		   return newToken;
	   }
   }   
}