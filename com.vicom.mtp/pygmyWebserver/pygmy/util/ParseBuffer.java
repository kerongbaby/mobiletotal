
package pygmy.util;

/** 
 * This is primarily used to replace the <code>StringBuffer</code> 
 * class, as a way for the <code>Parser</code> to store the char's
 * for a specific region within the parse data that constitutes a 
 * desired value. The methods are not synchronized so it enables 
 * the <code>char</code>'s to be taken quicker than the 
 * <code>StringBuffer</code> class.
 *
 */
public class ParseBuffer{   
   /** 
    * This is used to quicken <code>toString</code>.
    */
   private String cache;
   /** 
    * The <code>char</code>'s this buffer accumulated.
    */
   private char[] buf;
   /** 
    * This is the number of <code>char</code>'s stored.
    */
   private int count;
   /** 
    * Constructor for <code>ParseBuffer</code>. The default 
    * <code>ParseBuffer</code> stores 16 <code>char</code>'s 
    * before a <code>resize</code> is needed to accomodate
    * extra characters. 
    */
   public ParseBuffer(){
      this(16);
   }
   
   /** 
    * This creates a <code>ParseBuffer</code> with a specific 
    * default size. The buffer will be created the with the 
    * length specified. The <code>ParseBuffer</code> can grow 
    * to accomodate a collection of <code>char</code>'s larger 
    * the the size spacified.
    *
    * @param size initial size of this <code>ParseBuffer</code>
    */
   public ParseBuffer(int size){
      buf = new char[size];
   }
   
   /** 
    * This will add a <code>char</code> to the end of the buffer.
    * The buffer will not overflow with repeated uses of the 
    * <code>append</code>, it uses an <code>ensureCapacity</code>
    * method which will allow the buffer to dynamically grow in 
    * size to accomodate more <code>char</code>'s.
    *
    * @param c the <code>char</code> to be appended
    */
   
   public void append(char c){
      ensureCapacity(count+ 1);
      buf[count++] = c;
   }

   /** 
    * This ensure that there is enough space in the buffer to 
    * allow for more <code>char</code>'s to be added. If
    * the buffer is already larger than min then the buffer 
    * will not be expanded at all.
    *
    * @param min the minimum size needed
    */     
   protected void ensureCapacity(int min) {
      if(buf.length < min) {
         int size = buf.length * 2;
         int max = Math.max(min, size);
         char[] temp = new char[max];         
         System.arraycopy(buf, 0, temp, 0, count); 
         buf = temp;
      }
   }  
   
   /** 
    * This will empty the <code>ParseBuffer</code> so that the
    * <code>toString</code> paramater will return <code>null</code>. 
    * This is used so that the same <code>ParseBuffer</code> can be 
    * recycled for different tokens.
    */
   public void clear(){
      cache = null;
      count = 0;
   }
  
   /** 
    * This will return the number of bytes that have been appended 
    * to the <code>ParseBuffer</code>. This will return zero after 
    * the clear method has been invoked.
    *
    * @return the number of <code>char</code>'s within the buffer
    */
   public int length(){
      return count;
   }

   /** 
    * This will return the characters that have been appended to the 
    * <code>ParseBuffer</code> as a <code>String</code> object.
    * If the <code>String</code> object has been created before then
    * a cached <code>String</code> object will be returned. This
    * method will return <code>null</code> after clear is invoked.
    *
    * @return the <code>char</code>'s appended as a <code>String</code>
    */
   public String toString(){
      if(count <= 0) {
         return null;
      }
      if(cache != null) {
         return cache;
      }
      cache = new String(buf,0,count);
      return cache;
   }
}   
