
 
package pygmy.util;

/** 
 * This <code>Parser</code> object is to be used as a simple template 
 * for parsing uncomplicated expressions. This object is used to parse 
 * a <code>String</code>. This provides a few methods that can be used 
 * to store and track the reading of data from a buffer. There are two 
 * abstract methods provided to allow this to be subclassed to create 
 * a <code>Parser</code> for a given <code>String</code>.
 *
 */
public abstract class Parser{ 

   /** 
    * This is the buffer that is being parsed.
    */
   protected char[] buf;

   /** 
    * This represents the current read offset.
    */
   protected int off;   

   /** 
    * This represents the length of the buffer. 
    */
   protected int count;
  
   /** 
    * This is a no argument constructor for the <code>Parser</code>. 
    * This will be invoked by each subclass of this object. It will 
    * set the buffer to a zero length buffer so that when the 
    * <code>ensureCapacity</code> method is used the buf's 
    * length can be checked.
    */
   protected Parser(){
      buf = new char[0];
   }
   
   /** 
    * This is used to parse the <code>String</code> given to it. This 
    * will ensure that the <code>char</code> buffer has enough space 
    * to contain the characters from the <code>String</code>. This 
    * will firstly ensure that the buffer is resized if nessecary. This 
    * second step in this <code>parse</code> method is to initialize 
    * the <code>Parser</code> object so that multiple parse invokations 
    * can be made. The <code>init</code> method will reset this to an 
    * prepared state. Then finally the <code>parse</code> method is 
    * called to parse the <code>char</code> buffer.
    *
    * @param text the <code>String</code> to be parsed with this 
    * <code>Parser</code>
    */    
   public void parse(String text){
      if(text != null){ 
         ensureCapacity(text.length());
         count = text.length();
         text.getChars(0, count, buf,0);
         init(); 
         parse();
      }
   }
   
   /** 
    * This ensure that there is enough space in the buffer to allow 
    * for more <code>char</code>'s to be added. If the buffer is 
    * already larger than min then the buffer will not be expanded 
    * at all.
    *
    * @param min the minimum size needed to accomodate the characters
    */     
   protected void ensureCapacity(int min) {
      if(buf.length < min) {
         int size = buf.length * 2;
         int max = Math.max(min, size);
         char[] temp = new char[max];         
         buf = temp;
      }
   }
   
   /** 
    * This is used to determine if a given ISO-8859-1 character is 
    * a space character. That is a whitespace character this sees 
    * the, space, carrage return and line feed characters as
    * whitespace characters.
    *
    * @param c the character that is being determined by this
    *
    * @return true if the character given it is a space character
    */
   protected boolean space(char c) {
      switch(c){
      case ' ': case '\t':
      case '\n': case '\r':
         return true;
      default:
         return false;
      }
   } 
   
   /** 
    * This is used to determine wheather or not a given character is 
    * a digit character. It assumes iso-8859-1 encoding to compare.
    *
    * @param c the character being determined by this method
    *
    * @return true if the character given is a digit character
    */      
   protected boolean digit(char c){
      return c <= '9' && '0' <= c;
   }
   
   /** 
    * This takes a unicode character and assumes an encoding of 
    * ISO-8859-1. This then checks to see if the given character 
    * is uppercase if it is it converts it into is ISO-8859-1 
    * lowercase char.
    *
    * @param c the <code>char</code> to be converted to lowercase
    *
    * @return the lowercase ISO-8859-1 of the given character
    */
   protected char toLower(char c) {
      if(c >= 'A' && c <= 'Z') {
         return (char)((c - 'A') + 'a');
      }
      return c;
   }


   /** 
    * This will initialize the <code>Parser</code> when it is ready 
    * to parse a new <code>String</code>. This will reset the 
    * <code>Parser</code> to a ready state. The <code>init</code> 
    * method is invoked by the <code>Parser</code> when the 
    * <code>parse</code> method is invoked.
    */
   protected abstract void init();
   
   /** 
    * This is the method that should be implemented to read 
    * the buf. This method should attempt to extract tokens 
    * from the buffer so that thes tokens may some how be 
    * used to determine the semantics. This method is invoked 
    * after the <code>init</code> method is invoked.    
    */
   protected abstract void parse();
}
