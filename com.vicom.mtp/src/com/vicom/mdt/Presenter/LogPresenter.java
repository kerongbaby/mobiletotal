package com.vicom.mdt.Presenter;

import org.eclipse.swt.widgets.Composite;

import com.vicom.mdt.SystemMidget.IMidget;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.views.IMobileTotalView;

/**
 * 这个类不同于其他的Presenter，它没有需要去表现的容器。
 * 这个类接受事件，并能够显示这些事件和该事件所关联的变化的属性。
 * 这个类可以用于数据记录的需要。
 * @author ycwang
 *
 */

public class LogPresenter extends AbstractPresenter {
	public LogPresenter(Composite composite) {
		super(composite);
	}

	public void setFocus(){
		
	}

	public void loadMidgets(IMobileTotalView view) {
		
	}

	
	
	public void processEventDestination(MobileTotalEvent e) {
		System.out.println(e.sourceMidget.toString() +" ask " + e.destMidget.toString() + "->" + e.attribute.name);
	}


	public boolean isWelcome(IMidget midget) {
		return true;
	}

	public boolean isInterest(IMidget midget) {
		// TODO Auto-generated method stub
		return false;
	}

	public void processEventSource(MobileTotalEvent e) {
		// TODO Auto-generated method stub
		
	}

	public Object getPaper() {
		// TODO Auto-generated method stub
		return null;
	}

	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}
