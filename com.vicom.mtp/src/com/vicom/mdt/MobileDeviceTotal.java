package com.vicom.mdt;


import java.util.Iterator;
import java.util.Vector;

import org.eclipse.swt.widgets.Display;

import com.vicom.mdt.Presenter.AbstractPresenter;
import com.vicom.mdt.Presenter.IPresenter;
import com.vicom.mdt.SystemMidget.IMidget;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.event.MobileTotaEventPool;

/**
 * �����������Mobile Total Platform���¼��ַ����ơ�
 * �¼���ִ�����ÿһ��ע���˵�Presenter����������Щע���Presenter��Display���߳�ִ�У��Ա�����ʾ��Widget��Presenter��ͬһ���̶߳���ɵı��
 * �ϵ��鷳��
 * @author ycwang
 *
 */
public class MobileDeviceTotal {
	
	public final static String ID = "MobileDeviceTotal"; 
	
	public final static Vector Listeners = new Vector(); 
	
	private static MobileDeviceTotal mdt = new MobileDeviceTotal();

	private MobileDeviceTotal(){
		(new eventProcess()).start();
	}
	
	public synchronized static void addListener(IPresenter listener){
		synchronized(Listeners){
			Listeners.add(listener);
		}
	}
	
	public static MobileDeviceTotal getInstance(){
		return mdt;
	}

	class eventProcess extends Thread {
		public eventProcess(){
			super("MainEventThread");
		}
		public void run(){
			while(true){
				try{
					MobileTotalEvent event = MobileTotaEventPool.getEvent();
					IMidget dest =event.destMidget;
					if( dest != null ){
						dest.response(event.attribute);
					}
					fireMidgetChange(event);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	// FIXME: ��Ҫ���õĴ���������header���������⣬�ر���ͬ�����⡣
	// ��ʵ���з��֣�Listenerֱ��ͬ���������������ͬ���ֻ����ͬʱ�޸ĵ����⡣
	synchronized void fireMidgetChange(MobileTotalEvent mtevent){
		Vector snapshot;
		synchronized(Listeners){
			snapshot = (Vector)Listeners.clone();
		}
		Iterator listeners = snapshot.iterator();
		while(listeners.hasNext()){
			AbstractPresenter presenter = ((AbstractPresenter)listeners.next());
			try{
			Display display = presenter.getDisplay();
			if( !display.isDisposed() )
				display.syncExec(new DisplayThread(presenter,mtevent));
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		/*
		Iterator listeners = Listeners.iterator();
		while(listeners.hasNext()){
			AbstractPresenter presenter = ((AbstractPresenter)listeners.next());
			try{
			Display display = presenter.getDisplay();
			if( !display.isDisposed() )
				display.syncExec(new DisplayThread(presenter,mtevent));
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		*/
		
	}
	class DisplayThread extends Thread {
		AbstractPresenter presenter;
		MobileTotalEvent event;
		public DisplayThread(AbstractPresenter presenter, MobileTotalEvent event){
			this.presenter = presenter;
			this.event = event;
		}
		public void run(){
			// ֻ���¼�����Դ��û���¼���Ŀ�꣬��ô��Ϊ����һ�����档
			if( (event.sourceMidget != null) && (event.destMidget == null) && presenter.isWelcome(event.sourceMidget)){
				presenter.processEventSource(event);
			}
			
			// �¼�����Ŀ�꣬��ô��Ϊ�������Ŀ������Խ����޸ġ�
			if( event.destMidget != null && presenter.isInterest(event.destMidget)){
				presenter.processEventDestination(event);
			}
		}
	}


}
