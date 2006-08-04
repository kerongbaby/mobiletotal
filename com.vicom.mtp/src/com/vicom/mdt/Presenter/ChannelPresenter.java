package com.vicom.mdt.Presenter;

import org.eclipse.swt.widgets.Composite;

import com.vicom.mdt.SystemMidget.IMidget;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.views.IMobileTotalView;

/**
 * 这个类向MD发送消息。
 * @author ycwang
 *
 */

public class ChannelPresenter extends AbstractPresenter {

	public ChannelPresenter(Composite composite) {
		super(composite);
	}

	public void loadMidgets(IMobileTotalView view) {
		// TODO Auto-generated method stub
		
	}

	public void processEventDestination(MobileTotalEvent e) {
		System.out.println("Channel process");
		
		// TODO Auto-generated method stub
		
	}

	
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}


	public boolean isWelcome(IMidget midget) {
		// TODO Auto-generated method stub
		return true;
	}

	public void MidgetChanged() {
		// TODO Auto-generated method stub
		
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
