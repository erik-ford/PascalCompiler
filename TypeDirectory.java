//Erik Ford
//CSCI 465
//Compiler Project
//last updated 12-15-19

import java.util.Hashtable;

/***********************************************************
 * @author Erik Ford
 * TypeDirectory defines and implements a lookup table that
   contains all variables, subroutines, and their types
 ***********************************************************/
public class TypeDirectory 
{
	public Hashtable<String, String> td = new Hashtable<String, String>(); //type directory
	public TokenQueue buffer = new TokenQueue(); //used for processing declarations
	
	   /***************************************
	    * Constructor method for TypeDirectory
	    * @return an empty TypeDirectory
	    ***************************************/
	   public TypeDirectory()
	   {
		   
	   }
	   
	   /************************************************************
	    * Returns type associated with variable name
	    * @param inKey - variable name to look up
	    * @return associated type, null if not found
	    ************************************************************/
	   public String get(String inKey)
	   {
		   return td.get(inKey);
	   }
	   
	   /************************************************************
	    * Inserts new token into TokenQueue buffer
	    * @param inToken - token to be added to buffer
	    ************************************************************/
	   public void push(Token inToken)
	   {
		   Token t = new Token(inToken);
		   t.setNext(null);
		   buffer.push(t);
	   }
	   
	   /****************************************************************
	    * Flushes buffer into TypeDirectory with correct type
	    * @param type - type of variables to be added to TypeDirectory
	    ****************************************************************/
	   public void flush(String type)
	   {
		   while (buffer.head != null)
		   {
			   Token t = buffer.pop();
			   td.put(t.getText(), type);
		   }
		   buffer.tail = null;
	   }
	   
	   /******************************************
	    * Prints out contents of TypeDirectory
	    * Used for debugging purposes
	    ******************************************/
	   public void print()
	   {
		   td.forEach((k, v) -> {
			   System.out.println("Key: '" + k + "'\tValue: '" + v + "'");
		   });
	   }
}