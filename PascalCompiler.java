//Erik Ford
//CSCI 465
//Compiler Project
//last updated 11-14-19

/********************************************
 * @author Erik Ford
 * PascalCompiler is the main driver
   program for the Mini Pascal Compiler
 ********************************************/
public class PascalCompiler 
{
    public static void main(String[] args)
    {
    	//declaration of variables
    	String fileName;
    	
    	//get source file from command line
    	fileName = args[0];
    	
    	//call I/O module to handle input code
    	TokenQueue tokens = IOModule.LexicalAnalysis(fileName);
    	
    	//call I/O module to process tokens
    	IOModule.SyntaxAnalysis(tokens);
    	
    }
}
