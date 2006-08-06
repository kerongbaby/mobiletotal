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
 * 该类管理整个Mobile Total Platform的事件分发机制。
 * 事件的执行针对每一个注册了的Presenter，并利用这些注册的Presenter的Display的线程执行，以避免显示的Widget与Presenter是同一个线程而造成的编程
 * 上的麻烦。
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
	
	// FIXME: 需要更好的处理方法处理header的连接问题，特别是同步问题。
	// 在实践中发现，Listener直接同步会出现锁死。不同步又会出现同时修改的问题。
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
			// 只有事件的来源，没有事件的目标，那么认为这是一个宣告。
			if( (event.sourceMidget != null) && (event.destMidget == null) && presenter.isWelcome(event.sourceMidget)){
				presenter.processEventSource(event);
			}
			
			// 事件存在目标，那么认为这是针对目标的属性进行修改。
			if( event.destMidget != null && presenter.isInterest(event.destMidget)){
				presenter.processEventDestination(event);
			}
		}
	}


}
