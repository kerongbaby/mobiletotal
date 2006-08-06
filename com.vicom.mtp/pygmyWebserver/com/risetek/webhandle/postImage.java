package com.risetek.webhandle;

import com.vicom.mdt.SystemMidget.MidgetCreator;

import pygmy.core.HttpRequest;
import pygmy.core.HttpResponse;

public class postImage implements webHandler 
{
	
	public String handle(HttpRequest request, HttpResponse response){
		String id = request.getQueryData("identify");
		byte[] buffer = request.getPostData();
		if( buffer != null )
			MidgetCreator.getInstance().handle(id,"image",buffer);
		return "POST IMAGE";
	}
}
