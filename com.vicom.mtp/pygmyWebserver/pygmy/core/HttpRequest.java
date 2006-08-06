package pygmy.core;

import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.microedition.io.HttpConnection;

/**
 * This holds the http request data a given request.
 */
public class HttpRequest extends Request{
	private static int nextRequestId = 0;
	private Integer requestId = null;
	private String method;
	private String url;
	private String query;
	private String protocol;
	private int major;
	private int minor;
	private HttpHeaders headers;
	private byte[] postData;
	private Hashtable httpVariableMap;
	private String connectionHeader;
	private Connection connection;

	public HttpRequest(Hashtable serverConfig, Connection connection)	throws IOException{
		super(serverConfig);
		init();
		this.connection = connection;
		InternetInputStream stream = new InternetInputStream(connection.getInputStream());
		String startLine = null;
		try{
			startLine = readHttpCommand(stream);
			if (startLine == null){
				throw new EOFException();
			}
			if (protocol.equals("HTTP/1.0")){
				major = 1;
				minor = 0;
			} else if (protocol.equals("HTTP/1.1")){
				major = 1;
				minor = 1;
			} else {
				throw new HttpProtocolException(HttpConnection.HTTP_VERSION,
						"Protocol " + protocol + " not supported.");
			}
			headers = new HttpHeaders(stream);
			readPostData(stream);
		} catch (NoSuchElementException e){
			throw new HttpProtocolException(HttpConnection.HTTP_NOT_FOUND,"Bad request " + startLine);
		} catch (NumberFormatException e){
			throw new HttpProtocolException(
					HttpConnection.HTTP_LENGTH_REQUIRED,
					"Content Length was not a number or not supplied.");
		}
	}

	private void init(){
		method = null;
		url = null;
		query = null;
		protocol = null;
		connectionHeader = null;
		postData = null;
		connectionHeader = "Connection";
		requestId = new Integer(nextRequestId++);
	}

	public Integer getRequestId(){
		return requestId;
	}
/**
 * 获得HTTP报文头的首行
 * @param stream
 * @return
 * @throws IOException
 */
	private String readHttpCommand(InternetInputStream stream)	throws IOException {
		String startLine = null;
		do{
			startLine = stream.readline();
			if (startLine == null)
				return null;
		} while (startLine.trim().length() == 0);//去处首部前的换行，空格

		StringTokenizer tokenizer = new StringTokenizer(startLine);
		method = tokenizer.nextToken();
		parseUrl(tokenizer.nextToken());
		protocol = tokenizer.nextToken();

		return startLine;
	}

	private void readPostData(InternetInputStream stream) throws IOException {
		/*获取当前传递文件的长度*/
		String contenLength="0";
		String transferencoding = getRequestHeader("Transfer-Encoding");
		if( transferencoding == null){
			contenLength = getRequestHeader("Content-Length");
			if (contenLength == null)
				return;
		} else {
			if("chunked".equals(transferencoding)){
				Vector chunkeddatas = new Vector();
				while(true){
					String length = stream.readline();
					if("".equals(length))length = stream.readline();
					int chunkLength = 0;
					try{
						chunkLength = Integer.parseInt(length,16);
					}catch(Exception e){
						e.printStackTrace();
					}
					//如果这个chunk的数据长度为0，表明数据已经传输完成。
					if(chunkLength == 0){
						int total = 0;
						Iterator i = chunkeddatas.iterator();
						// 计算数据的总长度。
						while( i.hasNext() ){
							byte[] t = (byte[])i.next();
							total += t.length;
						}
						// 把各个分割的数据组织成整体。
						postData = new byte[total];
						i = chunkeddatas.iterator();
						total = 0;
						while( i.hasNext() ){
							byte[] t = (byte[])i.next();
							System.arraycopy(t,0,postData,total,t.length);
							total += t.length;
						}	
						chunkeddatas.removeAllElements();
						return;
					}
					
					// 把这个区段的数据记录下来，并放置在Vector中。
					byte[] temp = new byte[chunkLength];
					while(true){
						int len = 0;
						len += stream.read(temp,len,(chunkLength-len));
						if( len == chunkLength ){
							chunkeddatas.add(temp);
							break;
						}
					}
				}
			}
		}

		int postLength = Integer.parseInt(contenLength);
		postData = new byte[postLength];

		int length = -1;
		int offset = stream.read(postData);
		while (offset >= 0 && offset < postData.length) {
			length = stream.read(postData, offset, postData.length - offset);
			if (length == -1) {
				break;
			}
			offset += length;
		}
	}

	private void parseUrl(String aUrl){

		int queryIndex = aUrl.indexOf('?');
		if (queryIndex < 0){
			url = aUrl;
		} else {
			url = aUrl.substring(0, queryIndex);
			if ((queryIndex + 1) < aUrl.length())
				query = aUrl.substring(queryIndex + 1);
		}
	}

	public String getRequestHeader(String key){
		return headers.get(key);
	}

	public String getRequestHeader(String key, String defaultValue){
		String val = getRequestHeader(key);
		return (val == null) ? defaultValue : val;
	}

	public String getMethod(){
		return method;
	}

	public String getUrl(){
		return url;
	}

	public String getQuery(){
		return query;
	}
    public String getQueryData( String key ){
        if ( httpVariableMap == null ){
            httpVariableMap = createQueryMap( query );
            if (postData != null){
                String contentType = headers.get("Content-Type");
                if ("application/x-www-form-urlencoded".equals(contentType)){
                    Hashtable httpPostMap = createQueryMap( new String( postData ) );
                    if (httpPostMap.contains(key)){
                        return (String) httpPostMap.get( key );
					}
                }
            }
        }
        return (String) httpVariableMap.get( key );
    }

    private Hashtable createQueryMap(String query){

    	Hashtable queryMap = new Hashtable();
		if (query == null){
			return queryMap;
		}

		query = query.replace('+', ' ');
		StringTokenizer st = new StringTokenizer(query, "&");
		try{
			while (st.hasMoreTokens()){
				String field = st.nextToken();
				int index = field.indexOf('=');
				if (index < 0){
					queryMap.put(URLDecoder.decode(field, "UTF-8"), "");
				} else {
					queryMap.put(URLDecoder.decode(field.substring(0, index),"UTF-8"), 
								  URLDecoder.decode(field.substring(index + 1),"UTF-8"));
				}
			}
		} catch (UnsupportedEncodingException e){
		}

		return queryMap;
	}

	public String getProtocol(){
		return protocol;
	}

	public byte[] getPostData(){
		return postData;
	}
	
	public boolean isKeepAlive(){
		return false;
	}

	public String getConnectionHeader(){

		return connectionHeader;
	}

	public Connection getConnection(){

		return connection;
	}

	public String getProperty(String key){

		return getProperty(key, null);
	}

	
	public HttpHeaders getHeaders(){
		return headers;
	}
	 

	public boolean isProtocolVersionLessThan(int aMajor, int aMinor){
		return (major <= aMajor && minor < aMinor);
	}

}
