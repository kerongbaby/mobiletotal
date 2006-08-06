package com.vicom.mdt.Presenter;

import java.util.Vector;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.vicom.mdt.SystemMidget.IMidget;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.views.IMobileTotalView;
import com.vicom.mdt.MobileDeviceTotal;

public abstract class AbstractPresenter implements IPresenter {
	
    public Composite composite;
	/**
	 * Midget��������
	 *
	 */
	protected Display display;
	
	/**
	 * ���е���ͼ����MobileTotalPlatform���¼������ߡ�
	 *
	 */
	public AbstractPresenter(Composite composite){
		this.composite = composite;
		display = composite.getDisplay();
		MobileDeviceTotal.addListener(this);
	}
	
	
	public Display getDisplay() throws Exception{
		if( display == null ) throw new Exception("Display not Set!");
		return display;
	}
	
	
	protected void setDisplay(Display display){
		this.display = display;
	}
	
	
	public Vector midgetsContains = new Vector();
	
	/**
	 * ��¼�͹���������ͼ��ӵ�е�Midget����ҪĿ����Ϊ����ͼ��Ҫ���µ�ʱ���ܹ�ö����ЩMidget��
	 * @param midget
	 * @return
	 */
	public IMidget addMidget(IMidget midget){
		// ������Midget�Ѿ����ڣ����ٴμ��롣
		if( midgetsContains.contains(midget)) return null;
		midgetsContains.add(midget);
		return midget;
	}
	
	/**
	 * ����Midget������Ҫ�ڱ���ͼ�б��֣���Ҫ���������Ƴ���Midget��
	 * @param midget
	 */
	public void removeMidget(IMidget midget){
		midgetsContains.remove(midget);
	}
	
	
	/**
	 * ������ͼ
	 *
	 */
	public abstract void loadMidgets(IMobileTotalView view);

	public abstract void setFocus();
	
	/**
	 * ÿ��Presenter�ṩ�жϣ��Ƿ�����ص�midget�¼���
	 */
	
	/**
	 * ���¼���Դ���жϣ��Ƿ�ӭ�������Midget���¼���
	 */
	public	abstract	boolean isWelcome(IMidget midget);
	
	/**
	 * ����ע�¼�����Դʱ���ø÷�������
	 * @param e
	 */
	public	abstract	void 	processEventSource(MobileTotalEvent e);
	// ���¼�Ŀ����жϣ��Ƿ��עǰ�����Midget���¼���
	public abstract 	boolean	isInterest(IMidget midget);
	
	/**
	 * ����ע�¼���Ŀ��ʱ���ø÷�������
	 */
	public abstract		void processEventDestination(MobileTotalEvent e);

	/**
	 * ����ͼ��Ҫ����ʱ��ķ����� 
	 */
	public abstract		void refresh();
}
