package com.risetek.webhandle;

import pygmy.core.HttpRequest;
import pygmy.core.HttpResponse;

public interface webHandler {
	public String handle(HttpRequest request, HttpResponse response);

}
