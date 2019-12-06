import java.util.Hashtable;

//Erik Ford
//CSCI 465
//Compiler Project
//last updated 11-11-19

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
	    * Constructor method for SymbolTable
	    * @return an empty SymbolTable
	    ***************************************/
	   public TypeDirectory()
	   {
		   
	   }
	   
	   /************************************************************
	    * Returns symbol identifier based on token text
	    * @param inKey - token text to lookup in SymbolTable
	    * @return associated symbol identifier, null if not found
	    ************************************************************/
	   public String get(String inKey)
	   {
		   return td.get(inKey);
	   }
	   
	   /************************************************************
	    * Inserts new symbol into SymbolTable
	    * @param inToken - token to be added to SymbolTable
	    ************************************************************/
	   public String put(Token inToken)
	   {
		   return td.put(inToken.getText(), inToken.getSymbol());
	   }
	   
	   /************************************************************
	    * Inserts new token into TokenQueue buffer
	    * @param inToken - token to be added to buffer
	    ************************************************************/
	   public void push(Token inToken)
	   {
		   inToken.setNext(null);
		   buffer.push(inToken);
	   }
	   
	   /************************************************************
	    * Flushes buffer into TypeDirectory with correct type
	    * @param type - type of variables to be added to TypeDirectory
	    ************************************************************/
	   public void flush(String type)
	   {
		   while (buffer.head != null)
		   {
			   Token t = buffer.pop();
			   td.put(t.getText(), type);
		   }
		   buffer.tail = null;
	   }
	   
	   public void print()
	   {
		   td.forEach((k, v) -> {
			   System.out.println("Key: '" + k + "'\tValue: '" + v + "'");
		   });
	   }
	}
