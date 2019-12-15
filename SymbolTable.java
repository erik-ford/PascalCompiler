//Erik Ford
//CSCI 465
//Compiler Project
//last updated 12-15-19

import java.util.*;

/**********************************************************
 * @author Erik Ford
 * Implements a HashTable that contains definitions
   for each symbol defined in the Mini Pascal grammar.
 **********************************************************/
public class SymbolTable 
{
   public Hashtable<String, String> st = new Hashtable<String, String>(); //symbol table
	
   /***************************************
    * Constructor method for SymbolTable
    * @return an initialized SymbolTable
    ***************************************/
   public SymbolTable()
   {
	   st.put("and", "and_symbol");
	   st.put("array", "array_symbol");
	   st.put("begin", "begin_symbol");
	   st.put("char", "char_symbol");
	   st.put("div", "div_symbol");
	   st.put("do", "do_symbol");
	   st.put("else", "else_symbol");
	   st.put("end", "end_symbol");
	   st.put("if", "if_symbol");
	   st.put("integer", "integer_symbol");
	   st.put("mod", "mod_symbol");
	   st.put("not", "not_symbol");
	   st.put("of", "of_symbol");
	   st.put("or", "or_symbol");
	   st.put("program", "program_symbol");
	   st.put("read", "read_symbol");
	   st.put("real", "real_symbol");
	   st.put("then", "then_symbol");
	   st.put("var", "var_symbol");
	   st.put("while", "while_symbol");
	   st.put("write", "write_symbol");
	   st.put("writeln", "writeln_symbol");
	   st.put("+", "plus_symbol");
	   st.put("-", "minus_symbol");
	   st.put("*", "times_symbol");
	   st.put("<", "less_symbol");
	   st.put("<=", "lessequal_symbol");
	   st.put("<>", "notequal_symbol");
	   st.put(">", "greater_symbol");
	   st.put(">=", "greaterequal_symbol");
	   st.put("=", "equal_symbol");
	   st.put(":=", "assign_symbol");
	   st.put(":", "colon_symbol");
	   st.put(";", "semicolon_symbol");
	   st.put(",", "comma_symbol");
	   st.put("(", "lparen_symbol");
	   st.put("[", "lbrack_symbol");
	   st.put(")", "rparen_symbol");
	   st.put("]", "rbrack_symbol");
	   st.put(".", "period_symbol");
	   st.put("..", "dotdot_symbol");
	   st.put("!_identifier_!", "identifier_symbol");
	   st.put("!_number_!", "number_symbol");
	   st.put("!_quote_!", "quotestring_symbol");
	   st.put("!_char_!", "litchar_symbol");
	   st.put("`", "eofsym_symbol");
	   st.put("!_illegal_!", "illegal_symbol");
	   st.put("!_newline_!", "newline_symbol");
   }
   
   /************************************************************
    * Returns symbol identifier based on token text
    * @param inKey - token text to lookup in SymbolTable
    * @return associated symbol identifier, null if not found
    ************************************************************/
   public String get(String inKey)
   {
	   return st.get(inKey);
   }
   
   /************************************************************
    * Inserts new symbol into SymbolTable
    * @param inToken - token to be added to SymbolTable
    ************************************************************/
   public void put(Token inToken)
   {
	   st.put(inToken.getText(), inToken.getSymbol());
   }
}