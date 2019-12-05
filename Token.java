//Erik Ford
//CSCI 465
//Compiler Project
//last updated 11-14-19

/*************************************************
 * @author Erik Ford
 * Token defines and implements a data structure
   to represent a token/symbol of the Mini Pascal
   grammar. 
 *************************************************/
public class Token 
{
   protected String text; //text found in source file
   protected String symbol; //symbol identifier
   protected Token next; //next Token in TokenQueue
   
   /***********************************************
    * Constructor method of a Token
    * @param inText - text found in source file
    * @param inSymbol - symbol identifier text
    * @return a new Token
    ***********************************************/
   public Token(String inText, String inSymbol) 
   {
      text = inText;
      symbol = inSymbol;
      next = null;
   }
   
   /********************************************
    * Returns text associated with Token
    * @return String containing Token text
    ********************************************/
   public String getText()
   {
	   return text;
   }
   
   /***********************************************
    * Returns symbol identifier text of Token
    * @return String containing symbol identifier
    ***********************************************/
   public String getSymbol()
   {
	   return symbol;
   }
   
   /**************************************************************
    * Assigns Token as next Token in TokenQueue implementation
    * @param inToken - Token to be considered next in sequence
    **************************************************************/
   public void setNext(Token inToken)
   {
	   next = inToken;
   }
   
   /********************************************
    * Returns next Token in TokenQueue
    * @return Token that is next in sequence
    ********************************************/
   public Token getNext()
   {
	   return next;
   }
   
}
