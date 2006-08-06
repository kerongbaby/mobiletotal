package com.risetek.webhandle;

import com.vicom.mdt.SystemMidget.MidgetCreator;

import pygmy.core.HttpRequest;
import pygmy.core.HttpResponse;

public class register implements webHandler 
{
	public String handle(HttpRequest request, HttpResponse response){
		String id = request.getQueryData("identify");
		String ipaddress = request.getProperty("UserHostAddress");
		MidgetCreator.getInstance().handle(id,"ipaddress",ipaddress);
		return "OK";
	}
}
