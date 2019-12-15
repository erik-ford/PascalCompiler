//Erik Ford
//CSCI 465
//Compiler Project
//last updated 12-15-19

/***********************************************************
 * @author Erik Ford
 * TokenQueue implements and defines a standard queue
   containing the stream of Tokens.
 ***********************************************************/
public class TokenQueue
{
   public Token head = null; //Token at front of TokenQueue
   public Token tail = null; //Token at end of TokenQueue
   public int size = 0; //number of Tokens in TokenQueue
   
   /*******************************************
    * Constructor method of a TokenQueue.
    * @return Initialized empty TokenQueue.
    *******************************************/
   public TokenQueue()
   {
      head = null;
      tail = null;
   }
   
   /*******************************************
    * Constructor method to copy a TokenQueue.
    * @return Initialized copied TokenQueue.
    *******************************************/
   public TokenQueue(TokenQueue copy)
   {
	   this.head = copy.head;
	   this.tail = copy.tail;
	   this.size = copy.size;
   }
   
   /*************************************************
    * Inserts new Token at the back of TokenQueue
    * @param inToken - Token to be inserted
    *************************************************/
   public void push(Token inToken)
   {
	   if (tail != null)
	   {
		   tail.setNext(inToken);
	   }
	   else
	   {
		   head = inToken;
	   }
	   tail = inToken;
	   size++;
   }
   
   /*****************************************************
    * Removes and returns Token at front of TokenQueue
    * @return Token from the front of TokenQueue
    *****************************************************/
   public Token pop()
   {
	   Token temp = head;
	   head = head.getNext();
	   size--;
	   return temp;
   }
   
   /***************************************************
    * Checks if queue is empty.
    * @return true if empty, false if not
    ***************************************************/
   public boolean isEmpty()
   {
	   if (size <= 0 || head == null)
		   return true;
	   return false;
   }
   
   /***********************************************************
    * Returns Token from front of queue without removing it
    * @return Token at front of TokenQueue
    ***********************************************************/
   public Token peek()
   {
	   return head;
   }
   
   /*****************************************************************
    * Puts recently popped token back at front of TokenQueue
    * @param inToken - Token to be replaced at front of TokenQueue
    *****************************************************************/
   public void unpop(Token inToken)
   {
	   if (head != null)
	   {
		   inToken.setNext(head);
	   }
	   else
	   {
		   tail = inToken;
	   }
	   head = inToken;
	   size++;
   }
}