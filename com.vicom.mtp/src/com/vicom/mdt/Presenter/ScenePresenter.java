package com.vicom.mdt.Presenter;

import java.util.Enumeration;

import org.eclipse.swt.widgets.Composite;

import com.vicom.mdt.SystemMidget.IMidget;
import com.vicom.mdt.SystemMidget.baseAttributeGuardMidget;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.views.IMobileTotalView;

public class ScenePresenter extends AbstractPresenter {

	
	public ScenePresenter(Composite composite) {
		super(composite);
	}

	public void loadMidgets(IMobileTotalView view){
		// Nothing to do.
	}
	
	public synchronized void processEventDestination(MobileTotalEvent e){
		IMidget d = e.destMidget;
		if( (d instanceof baseAttributeGuardMidget) && (e.EventType == MobileTotalEvent.REMOVE_MIDGET)){
			removeMidget(e.sourceMidget);
			e.sourceMidget.disposeInSceneView();
		}else {
			d.showInSceneView(this);
		}
	}
	

	public void setFocus(){
		//paintCanvas.setFocus();
	}

	
	/**
	 * ÿ��SensePresenter�ṩ�жϣ��Ƿ�����ص�midget�¼���
	 */
	public boolean isWelcome(IMidget midget) {
		return  false;
	}

	public boolean isInterest(IMidget midget) {
		return (midget.getMidgetIdentify().startsWith("Thread") || (midget.getMidgetIdentify().startsWith("Camera"))) ||
			(midget instanceof baseAttributeGuardMidget); 
	}

	public void processEventSource(MobileTotalEvent e) {
	}
	/**
	 * ������ͼ�ṩ��Midgetչʾ��ƽ̨��Canvas��
	 */
	public Object getPaper() {
		return null;
	}

	public void refresh() {
		Enumeration enumeration = midgetsContains.elements();
		while( enumeration.hasMoreElements() ){
			((IMidget)enumeration.nextElement()).showInSceneView(this);
		}
	}
	
}
