package com.risetek.webhandle;

import java.util.Hashtable;

import pygmy.core.HttpRequest;
import pygmy.core.HttpResponse;


public class hbeat implements webHandler {

	static final int maxColumn = 3;	/// line per page
	static Hashtable userlist = new Hashtable();
	static UptHandleLisener handler;
	static Object mutex = new Object();
	
	public static void setLisener(UptHandleLisener lisener){
		synchronized(mutex){
			handler = lisener;
		}
	}
	
	public static void removelLisener(){
		synchronized(mutex){
			handler = null;
		}
	}
	
	
	public String handle(HttpRequest request, HttpResponse response){
		try{
		System.out.println("HeartBeat---->"+request.getQueryData("label")+" Length:"+request.getPostData().length);
		if(handler != null){
			synchronized(mutex){
				handler.diplay(request.getPostData());
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}


		return "OK";
	}
}
