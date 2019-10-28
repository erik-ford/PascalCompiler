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
   public static void LexicalAnalysis(String inFile)
   {
	   //declaration of variables
	   File src = null;
	   Scanner scnr = null;
	   TokenQueue tokens = null;
	   
	   
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
	   
	   //initialize char and line counters
	   currLine = sourceLineChars.get(currLineIndex);
	   tokens = Lexer.getTokens();
	   
	   //First delivery: Print out list of tokens and symbol types////////////////////////////////////////////
	   PrintWriter writer = null;
	   try 
	   {
		   writer = new PrintWriter("output.txt");
	   }
	   catch (Exception e)
	   {
		   System.err.println("Error writing to output file.");
		   System.exit(-1);
	   }
	   
	   writer.printf("%-25s %-25s \n", "Token text:", "Symbol type:");
	   writer.printf("%-25s %-25s \n", "----------------", "-----------------");
	   
	   while (!tokens.isEmpty())
	   {
		   Token t = tokens.pop();
		   //System.out.println(t.getText() + "\t\t" + t.getSymbol());
		   writer.printf("%-25s %-25s \n", t.getText(), t.getSymbol());
	   }
	   
	   writer.close();
	   //////////////////////////////////////////////////////////////////////////////////////////////////////
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
			   return '�';
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
}