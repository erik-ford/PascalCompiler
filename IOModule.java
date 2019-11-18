//Erik Ford
//CSCI 465
//Compiler Project
//last updated 9-26-19

import java.io.*;
import java.util.*;

/************************************************
 * @author Erik Ford
 * IOModule handles input and output functions
   for the entire Mini Pascal Compiler
 ************************************************/
public class IOModule 
{
	private static int currCharIndex = 0; //index to keep track of current char on line
	private static int currLineIndex = 0; //index to keep track of current line from file
	private static char[] currLine; //current line being processed
	private static ArrayList<char[]> sourceLineChars = new ArrayList<char[]>(); //arraylist of source file lines split into char arrays
	
	/***************************************
	 * Handles I/O for lexical analysis
	   phase of compilation
	 * @param inFile - name of source file
	 ***************************************/
   public static TokenQueue LexicalAnalysis(String inFile)
   {
	   
	   TokenQueue tokens;
	   readFile(inFile);
	   
	   //initialize char and line counters
	   currLine = sourceLineChars.get(currLineIndex);
	   tokens = Lexer.getTokens();
	   
	   //First delivery: Print out list of tokens and symbol types////////////////////////////////////////////
	   TokenQueue printTokens = new TokenQueue(tokens);
	   PrintWriter writer = null;
	   try 
	   {
		   writer = new PrintWriter("TokenOutput.txt");
	   }
	   catch (Exception e)
	   {
		   System.err.println("Error writing to output file.");
		   System.exit(-1);
	   }
	   
	   writer.printf("%-25s %-25s \n", "Token text:", "Symbol type:");
	   writer.printf("%-25s %-25s \n", "----------------", "-----------------");
	   
	   if (printTokens.isEmpty()) {System.out.println("Token copying failed.");}
	   
	   while (!printTokens.isEmpty())
	   {
		   Token t = printTokens.pop();
		   //System.out.println(t.getText() + "\t\t" + t.getSymbol());
		   writer.printf("%-25s %-25s \n", t.getText(), t.getSymbol());
	   }
	   if (tokens.isEmpty()) {System.out.println("Reference copy made.");}
	   writer.close();
	   //////////////////////////////////////////////////////////////////////////////////////////////////////
	   
	   return tokens;
   }
   
   /************************************************
    * readFile will read the source file and
      decompose it into a readable format for
     the lexical analysis phase
    * @param inFile - file name to be read
    ************************************************/
   public static void readFile(String inFile)
   {
	 //declaration of variables
	   File src = null;
	   Scanner scnr = null;
	   
	   //initialize file pointer and scanner
	   src = new File(inFile);
	   try
	   {
		   scnr = new Scanner(src);
	   }
	   catch (FileNotFoundException e)
	   {
		   System.err.println("Source file not found.");
		   System.exit(404);
	   }
	   
	   //read file line by line
	   while (scnr.hasNextLine())
	   {
		   String line = scnr.nextLine();
		   line += "\n";
		   char[] chars = line.toCharArray();
		   sourceLineChars.add(chars);
	   }
   }
   
   /*************************************************
    * Sends next char from file to Lexer
    * @return a String of length 1 with next char
      or "!_eof_!" if end of file
    *************************************************/
   public static char getNextChar()
   {
	   if (currCharIndex > currLine.length - 1)
	   {
		   currLineIndex++;
		   currCharIndex = 0;
		   if (currLineIndex >= sourceLineChars.size())
			   return '`';
		   else
			   currLine = sourceLineChars.get(currLineIndex);
	   }
	   	   
	   return currLine[currCharIndex++];
   }
   
   /************************************************
    * Increments indices to beginning of next line
      from source file.
    ************************************************/
   public static void nextLine()
   {
	   currLineIndex++;
	   currLine = sourceLineChars.get(currLineIndex);
	   currCharIndex = 0;
   }
   
   /***************************************************
    * Calls parser module to perform syntax analysis
    * @param tokens - TokenQueue constructed by Lexer
    ***************************************************/
   public static void SyntaxAnalysis(TokenQueue tokens)
   {
	   boolean success = Parser.start(tokens);
	   
	   if (success) 
	   {
		   System.out.println("Program has no syntax errors.");
	   }
   }
}

