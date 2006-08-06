package com.vicom.mdt.event;

public class MobileTotalAttribute {
	
	public static final int GET 		= 0x1;
	public static final int POST		= 0x2;
	
	public MobileTotalAttribute(String Identify,String name, int type, Object object){
		this.identify = Identify;
		this.name = name;
		this.type = type;
		this.object = object;
	}
	public String 		identify;
	public String 		name;
	public int 			type;
	public Object 		object;

}
