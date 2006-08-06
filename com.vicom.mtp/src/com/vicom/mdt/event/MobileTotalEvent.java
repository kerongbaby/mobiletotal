package com.vicom.mdt.event;

import com.vicom.mdt.SystemMidget.IMidget;

public class MobileTotalEvent {
	
	/**
	 * 事件类型定义。
	 */
	public static final int UNKNOWN				= 0x0;

	// 该类型宣告一个新的Midget已经创建。宣告者是Midget自身，宣告目标是系统监控服务。
	public static final int NEW_MIDGET			= 0x1;
	public static final int ATTRIBUTECHANGED	= 0x2;

	// 宣告本Midget已经移出。宣告者是Midget自身，宣告目标是系统监视服务。
	public static final int REMOVE_MIDGET		= 0x3;
	public static final int MIDGET_SELECTED		= 0x4;
	
	
	
	/**
	 * 期望接受本事件的Midget。
	 */
	public IMidget destMidget;
	
	/**
	 * 产生本事件的Midget。
	 */
	public IMidget sourceMidget;
	
	/**
	 * 本事件所传递的变量描述。
	 */
	public MobileTotalAttribute attribute;
	
	/**
	 * 本事件的类型。
	 */
	public int	EventType;
	
	public MobileTotalEvent(IMidget source, IMidget dest, int type){
		this(source,dest,type,null);
	}
	


	public MobileTotalEvent(IMidget source, IMidget dest, int type, MobileTotalAttribute attribute){
		this.sourceMidget = source;
		this.attribute = attribute;
		this.destMidget = dest;
		this.EventType = type;
	}
}
