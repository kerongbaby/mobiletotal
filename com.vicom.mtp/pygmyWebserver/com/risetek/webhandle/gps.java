package com.risetek.webhandle;

import com.vicom.mdt.SystemMidget.MidgetCreator;
import pygmy.core.HttpRequest;
import pygmy.core.HttpResponse;


public class gps implements webHandler {

	public String handle(HttpRequest request, HttpResponse response){
		String id = request.getQueryData("identify");
		String gps = request.getQueryData("value");
		if( gps != null){
			if( id != null){
				try{
					MidgetCreator.getInstance().handle(id,"gps",gps);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return "OK";
	}
	

}
