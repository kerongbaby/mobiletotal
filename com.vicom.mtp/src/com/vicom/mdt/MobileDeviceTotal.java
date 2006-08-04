package com.vicom.mdt;


import java.util.Iterator;
import java.util.Vector;

import org.eclipse.swt.widgets.Display;

import com.vicom.mdt.Presenter.AbstractPresenter;
import com.vicom.mdt.Presenter.IPresenter;
import com.vicom.mdt.SystemMidget.IMidget;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.event.MobileTotaEventPool;

public class MobileDeviceTotal {
	
	public final static String ID = "MobileDeviceTotal"; 
	
	public final static Vector Listerners = new Vector(); 
	
	private static MobileDeviceTotal mdt = new MobileDeviceTotal();

	private MobileDeviceTotal(){
		(new eventProcess()).start();
	}
	
	public static void addListener(IPresenter listener){
		synchronized(Listerners){
			Listerners.add(listener);
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
					MobileTotalEvent e = MobileTotaEventPool.getEvent();
					IMidget dest =e.destMidget;
					if( dest != null ){
						dest.response(e.attribute);
					}
					fireMidgetChange(e);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	// FIXME: ��Ҫ���õĴ���������header���������⣬�ر���ͬ�����⡣
	void fireMidgetChange(MobileTotalEvent e){
		
		synchronized(Listerners){
			Iterator i = Listerners.iterator();
			while(i.hasNext()){
				AbstractPresenter a = ((AbstractPresenter)i.next());
				try{
				Display display = a.getDisplay();
				if( !display.isDisposed() )
					display.syncExec(new DisplayThread(a,e));
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
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
