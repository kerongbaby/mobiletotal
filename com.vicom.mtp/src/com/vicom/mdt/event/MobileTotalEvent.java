package com.vicom.mdt.event;

import com.vicom.mdt.SystemMidget.IMidget;

public class MobileTotalEvent {
	
	/**
	 * �¼����Ͷ��塣
	 */
	public static final int UNKNOWN				= 0x0;

	// ����������һ���µ�Midget�Ѿ���������������Midget��������Ŀ����ϵͳ��ط���
	public static final int NEW_MIDGET			= 0x1;
	public static final int ATTRIBUTECHANGED	= 0x2;

	// ���汾Midget�Ѿ��Ƴ�����������Midget��������Ŀ����ϵͳ���ӷ���
	public static final int REMOVE_MIDGET		= 0x3;
	public static final int MIDGET_SELECTED		= 0x4;
	
	
	
	/**
	 * �������ܱ��¼���Midget��
	 */
	public IMidget destMidget;
	
	/**
	 * �������¼���Midget��
	 */
	public IMidget sourceMidget;
	
	/**
	 * ���¼������ݵı���������
	 */
	public MobileTotalAttribute attribute;
	
	/**
	 * ���¼������͡�
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
