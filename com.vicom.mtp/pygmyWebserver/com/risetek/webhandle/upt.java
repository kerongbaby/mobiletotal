package com.risetek.webhandle;

import com.vicom.mdt.SystemMidget.MidgetCreator;

import pygmy.core.HttpRequest;
import pygmy.core.HttpResponse;
import pygmy.core.InternetInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

public class upt implements webHandler 
{
	
	public String handle(HttpRequest request, HttpResponse response){
		String id = request.getQueryData("identify");
		String strBoundary;
		strBoundary = request.getRequestHeader("Content-Type");
	      int pos=strBoundary.indexOf('=');
	      strBoundary=strBoundary.substring(pos+1);
	      strBoundary="--"+ strBoundary;
	      InternetInputStream stLine = new InternetInputStream(new ByteArrayInputStream (request.getPostData()));
	      int state=0;
	      ByteArrayOutputStream buffer=new ByteArrayOutputStream();
	      String name=null;
	      String value=null;
	      String filename=null;
	      final int bufSize= 512;
	      try{
			String startLine;
			byte[] startBin = new byte[bufSize];
			boolean needAddNewline = false;
			int i; 
			i = stLine.readlineLen(startBin,bufSize);  
			while ( 0 != i ){
				startLine = new String(startBin,0,i);
				if (startLine.startsWith(strBoundary))
				{
					state=0;
					if (null!=name){
						if(buffer.size()>2){
							break;
						}
					}
				}else if (startLine.startsWith("Content-Disposition: form-data") && state==0){
					StringTokenizer tokenizer=new StringTokenizer(startLine,";=\"");
					while(tokenizer.hasMoreTokens())
					{
						String token=tokenizer.nextToken();
						if(token.trim().startsWith("name"))
						{
							name=tokenizer.nextToken();
							state=2;
						}
						else if(token.trim().startsWith("filename"))
						{
							filename=tokenizer.nextToken();
							StringTokenizer ftokenizer=new StringTokenizer(filename,"\\");
							filename=ftokenizer.nextToken();
							while(ftokenizer.hasMoreTokens())
								filename=ftokenizer.nextToken();
							state=1;
							break;
						}
					}
				}else if (startLine.startsWith("Content-Type") && state==1){
					pos=startLine.indexOf(":");
				}else if (state==1){
					state=3;  //data will begin
				} else if (startLine.equals("\r\n")&&state==2){
					state=4;
				} else if (state==3){
					if (needAddNewline){
						buffer.write("\r\n".getBytes());
						needAddNewline = false;
					}
					if( i>=2 && startBin[i-2]=='\r' && startBin[i-1]=='\n'){
						needAddNewline = true; //add last newLine
						buffer.write(startBin,0,i-2);
					}else{
						buffer.write(startBin,0,i);
					}
				}else if (state==4){
					value=value==null?startLine:value+startLine;
				}
		    	i = stLine.readlineLen(startBin,bufSize);
				
			}//end while
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		MidgetCreator.getInstance().handle(id,"image",buffer.toByteArray());
		return "POST IMAGE";
	}
}
