package com.vicom.mdt.Presenter;

import org.eclipse.swt.widgets.Composite;

import com.vicom.mdt.SystemMidget.IMidget;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.views.IMobileTotalView;

/**
 * ����಻ͬ��������Presenter����û����Ҫȥ���ֵ�������
 * ���������¼������ܹ���ʾ��Щ�¼��͸��¼��������ı仯�����ԡ�
 * ���������������ݼ�¼����Ҫ��
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
