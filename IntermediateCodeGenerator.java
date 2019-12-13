import java.util.Map;

//Erik Ford
//CSCI 465
//Compiler Project
//last updated 11-14-19

/***********************************************************
 * @author Erik Ford
 * Generates intermediate representation of the source program
 ***********************************************************/
public class IntermediateCodeGenerator 
{
	private static int lineNumber = 1;
	private static String program = ".text\n";
	private static String data = ".data\n";
	private static int labelCount = 0;
	private static int regCount = 0;
	private static TypeDirectory td = new TypeDirectory();
	
   /******************************************************
    * This method copies the original TokenQueue and calls
      the program symbol of the grammar
    * @param inTokens - TokenQueue of program source code
    * @returns true if valid, false if invalid
    ******************************************************/
	public static String start(TokenQueue inTokens)
	{
		td = TypeChecker.getTypes(inTokens);
		TokenQueue tokens = new TokenQueue(inTokens);
		if (skipToBeginning(tokens))
		{
			program += "li $v0, 10\n";
			program += "syscall\n";
			return data + program;
		}
		else
		{
			return null;
		}
	}
	
   /******************************************************
    * This method skips until the begin statement
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean skipToBeginning(TokenQueue tokens)
   {
	   while (tokens.peek() != null && !tokens.peek().getSymbol().equals("begin_symbol"))
	   {
		   if (tokens.peek().getSymbol().equals("newline_symbol")) //new line
		   {
			   lineNumber++;
		   }
		   tokens.pop();
	   }
	   //System.out.println("Line "  + lineNumber);
	   return declarations(tokens);
	   
	    
   }
   
   
   /******************************************************
    * This method processes variables for data section
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean declarations(TokenQueue tokens)
   {
	   for (Map.Entry<String, String> e : td.td.entrySet())
	   {
		   String var = e.getKey();
		   String type = e.getValue();
		   //System.out.println(var + " : " + type);
		   data += var + ": ";
		   if (type.equals("char")) {data += ".word 0\n";}
		   else if (type.equals("integer")) {data += ".word 0\n";}
		   else if (type.equals("real")) {data += ".double 0\n";}
	   }
	   return statements(tokens);
	   
	    
   }
   
   /******************************************************
    * This method processes and converts all program
      statements into intermediate representation
    * @param tokens - TokenQueue of program source code 
    * @returns true if valid, false if invalid
    ******************************************************/
   public static boolean statements(TokenQueue tokens)
   {
	   tokens.pop();
	   while (tokens.peek() != null)
	   {
		   //System.out.println("here");
		  Token t = tokens.pop();
		  if (t.getSymbol().equals("identifier_symbol")) //variable
		  {
			  assignment(t, tokens, ";");
		  }
		  else if (t.getSymbol().equals("if_symbol")) //if
		  {
			  ifStatement(tokens);
		  }
		  else if (t.getSymbol().equals("while_symbol")) //while
		  {
			  whileStatement(tokens);
		  }
		  else if (t.getSymbol().equals("read_symbol")) //read
		  {
			  read(tokens);
		  }
		  else if (t.getSymbol().equals("write_symbol")) //write
		  {
			  write(tokens);
		  }
		  else if (t.getSymbol().equals("writeln_symbol")) //writeln
		  {
			  writeln(tokens);
		  }
	   }
	   
	   return true;
   }
   
   /********************************************************
    * This method will convert assignment statements
      into intermediate representation
    * @param lhs - Token for left hand side of statement
    * @param tokens - TokenQueue of source program
    ********************************************************/
   public static void assignment(Token lhs, TokenQueue tokens, String marker)
   {
	   tokens.pop(); //:=
	   //program += lhs.getText();
	   String reg = rhs(tokens, marker);
	   //program += "\n";
	   program += "sw " + reg + ", " + lhs.getText() + "\n";
	   regCount = 0;
   }
   
   /********************************************************
    * This method will convert assignment statements
      into intermediate representation
    * @param marker - end of statement marker
    * @param tokens - TokenQueue of source program
    * @return string of code text
    ********************************************************/
   public static String rhs(TokenQueue tokens, String marker)
   {
	   while (!tokens.peek().getText().equals(marker))
	   {
		   if (tokens.peek().getText().equals("(") || tokens.peek().getText().equals(")"))
		   {
			   tokens.pop();
		   }
		   else if (tokens.peek().getSymbol().equals("identifier_symbol"))
		   {
			   String op1 = tokens.pop().getText();
			   if (tokens.peek().getText().equals("(") || tokens.peek().getText().equals(")"))
			   {
				   tokens.pop();
			   }
			   if (tokens.peek().getText().equals(marker))
			   {
				   program += "lw $t" + regCount + ", " + op1 + "\n";
				   return "$t" + regCount++;
			   }
			   if (tokens.peek().getText().equals("(") || tokens.peek().getText().equals(")"))
			   {
				   tokens.pop();
			   }
			   String operator = tokens.pop().getText();
			   String op2 = rhs(tokens, marker);
			   program += "lw $t" + regCount + ", " + op1 + "\n";
			   op1 = "$t" + regCount++;
			   return operation(op1, operator, op2);
		   }
		   else if (tokens.peek().getSymbol().equals("number_symbol"))
		   {
			   String op1 = tokens.pop().getText();
			   if (tokens.peek().getText().equals(marker))
			   {
				   program += "li $t" + regCount + ", " + op1 + "\n";
				   return "$t" + regCount++;
			   }
			   String operator = tokens.pop().getText();
			   String op2 = rhs(tokens, marker);
			   program += "li $t" + regCount + ", " + op1 + "\n";
			   op1 = "$t" + regCount++;
			   return operation(op1, operator, op2);
		   }
		   else if (tokens.peek().getSymbol().equals("litchar_symbol"))
		   {
			   String op1 = tokens.pop().getText();
			   program += "li $t" + regCount + ", " + op1 + "\n";
			   return "$t" + regCount++;
		   }
	   }
	   return "$error";
   }
   
   /********************************************************
    * This method will determine the correct operation for
      the MIPS instruction set
    * @param marker - end of statement marker
    * @param tokens - TokenQueue of source program
    * @return string of code text
    ********************************************************/
   public static String operation(String op1, String operator, String op2)
   {
	   if (operator.equals("+"))
	   {
		   program += "add $t" + regCount + ", " + op1 + ", " + op2 + "\n";
		   return "$t" + regCount++;
	   }
	   else if (operator.equals("-"))
	   {
		   program += "sub $t" + regCount + ", " + op1 + ", " + op2 + "\n";
		   return "$t" + regCount++;
	   }
	   else if (operator.equals("*"))
	   {
		   program += "mult " + op1 + ", " + op2 + "\n";
		   program += "mflo $t" + regCount + "\n";
		   return "$t" + regCount++;
	   }
	   else if (operator.equals("div"))
	   {
		   program += "div " + op1 + ", " + op2 + "\n";
		   program += "mflo $t" + regCount + "\n";
		   return "$t" + regCount++;
	   }
	   else if (operator.equals("mod"))
	   {
		   program += "div " + op1 + ", " + op2 + "\n";
		   program += "mfhi $t" + regCount + "\n";
		   return "$t" + regCount++;
	   }
	   return "$error";
   }
   
   /********************************************************
    * This method will convert if statements
      into intermediate representation
    * @param lhs - Token for left hand side of statement
    * @param tokens - TokenQueue of source program
    ********************************************************/
   public static void ifStatement(TokenQueue tokens)
   {
	   program += "IF" + labelCount + ": ";
	   String op1 = tokens.pop().getText();
	   String relop = tokens.pop().getText();
	   String op2 = tokens.pop().getText();
	   program += "lw $t1, " + op1 + "\n";
	   program += "lw $t2, " + op2 + "\n";
	   if (relop.equals("<")) {program += "bge";}
	   else if (relop.equals("<=")) {program += "bgt";}
	   else if (relop.equals("=")) {program += "bne";}
	   else if (relop.equals("<>")) {program += "beq";}
	   else if (relop.equals(">=")) {program += "blt";}
	   else if (relop.equals(">")) {program += "ble";}
	   program += " $t1, $t2, FALSE" + labelCount + "\n";
	   program += "TRUE" + labelCount + ": ";
	   tokens.pop(); //then
	   assignment(tokens.pop(), tokens, "else"); //thenStmt
	   program += "j END" + labelCount + "\n";
	   program += "FALSE" + labelCount + ": ";
	   tokens.pop(); //else
	   assignment(tokens.pop(), tokens, ";"); //elseStmt
	   program += "END" + labelCount + ":\n";
	   labelCount++;
   }
   
   /********************************************************
    * This method will convert if statements
      into intermediate representation
    * @param lhs - Token for left hand side of statement
    * @param tokens - TokenQueue of source program
    ********************************************************/
   public static void whileStatement(TokenQueue tokens)
   {
	   program += "WHILE" + labelCount + ": ";
	   String op1 = tokens.pop().getText();
	   String relop = tokens.pop().getText();
	   String op2 = tokens.pop().getText();
	   program += "lw $t1, " + op1 + "\n";
	   program += "lw $t2, " + op2 + "\n";
	   if (relop.equals("<")) {program += "bge";}
	   else if (relop.equals("<=")) {program += "bgt";}
	   else if (relop.equals("=")) {program += "bne";}
	   else if (relop.equals("<>")) {program += "beq";}
	   else if (relop.equals(">=")) {program += "blt";}
	   else if (relop.equals(">")) {program += "ble";}
	   program += " $t1, $t2, END" + labelCount + "\n";
	   program += "DO" + labelCount + ": ";
	   tokens.pop(); //do
	   assignment(tokens.pop(), tokens, ";"); //doStmt
	   program += "j WHILE" + labelCount + "\n";
	   program += "END" + labelCount + ":\n";
	   labelCount++;
   }
   
   /********************************************************
    * This method will convert write statements
      into intermediate representation
    * @param lhs - Token for left hand side of statement
    * @param tokens - TokenQueue of source program
    ********************************************************/
   public static void write(TokenQueue tokens)
   {
	   tokens.pop(); //(
	   if (tokens.peek().getSymbol().equals("quotestring_symbol"))
	   {
		   String label = "string" + labelCount++;
		   data += label + ": .asciiz " + tokens.pop().getText().replace('\'', '"') + "\n";
		   program += "li $v0, 4\n";
		   program += "la $a0, " + label + "\n";
	   }
	   else if (tokens.peek().getSymbol().equals("identifier_symbol"))
	   {
		   String var = tokens.pop().getText();
		   String type = td.get(var);
		   if (type.equals("char"))
		   {
			   program += "li $v0, 11\n";
			   program += "lb $a0, " + var + "\n";
		   }
		   if (type.equals("integer"))
		   {
			   program += "li $v0, 1\n";
			   program += "lw $a0, " + var + "\n";
		   }
		   if (type.equals("real"))
		   {
			   program += "li $v0, 3\n";
			   program += "l.d $f12, " + var + "\n";
		   }
	   }
	   else if (tokens.peek().getSymbol().equals("litchar_symbol"))
	   {
		   String chr = tokens.pop().getText();
		   program += "li $v0, 11\n";
		   program += "li $a0, " + chr + "\n";
	   }

	   program += "syscall\n";
	   tokens.pop(); //)
	   tokens.pop(); //;
   }
   
   /********************************************************
    * This method will convert write statements
      into intermediate representation
    * @param lhs - Token for left hand side of statement
    * @param tokens - TokenQueue of source program
    ********************************************************/
   public static void writeln(TokenQueue tokens)
   {
	   write(tokens);
	   program += "li $v0, 11\n";
	   program += "li $a0, '\\n'\n";
	   program += "syscall\n";
   }
   
   /********************************************************
    * This method will convert read statements
      into intermediate representation
    * @param lhs - Token for left hand side of statement
    * @param tokens - TokenQueue of source program
    ********************************************************/
   public static void read(TokenQueue tokens)
   {
	   tokens.pop(); //(
	   String var = tokens.pop().getText();
	   String type = td.get(var);
	   if (type.equals("char"))
	   {
		   program += "li $v0, 12\n";
		   program += "syscall\n";
		   program += "sb $v0, " + var + "\n";
		   program += "li $v0, 11\n";
		   program += "li $a0, '\\n'\n";
		   program += "syscall\n";
	   }
	   if (type.equals("integer"))
	   {
		   program += "li $v0, 5\n";
		   program += "syscall\n";
		   program += "sw $v0, " + var + "\n";
	   }
	   if (type.equals("real"))
	   {
		   program += "li $v0, 7\n";
		   program += "syscall\n";
		   program += "s.d $f0, " + var + "\n";
	   }
	   tokens.pop(); //)
	   tokens.pop(); //;
   }
   
   

   
}