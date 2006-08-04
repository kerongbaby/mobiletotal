package com.vicom.mdt.Presenter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.vicom.mdt.SystemMidget.IMidget;
import com.vicom.mdt.SystemMidget.baseAttributeGuardMidget;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.views.IMobileTotalView;
import org.eclipse.swt.layout.FillLayout;

public class AttributePresenter extends AbstractPresenter {

    public AttributePresenter(Composite composite) {
		super(composite);
	}

	private	IMidget	currentMidget = null;
    
    public TabItem createTableItem(TabFolder tabfoler, String name, Control control){
    	TabItem ti = new TabItem(tabfoler, SWT.NONE);
		ti.setText(name);
		ti.setControl(control);
		return ti;
    }
    
    
	public void loadMidgets(IMobileTotalView view){
    	composite.setLayout(new FillLayout());
	}

	public void processEventDestination(MobileTotalEvent e){
		IMidget dest = e.destMidget;
		if( dest instanceof baseAttributeGuardMidget){
			if( e.EventType ==  MobileTotalEvent.REMOVE_MIDGET ){
				if( currentMidget == e.sourceMidget ){
					currentMidget.disposeInAttributeView();
					currentMidget = null;
				}
			}else{
				IMidget midget = ((baseAttributeGuardMidget)e.destMidget).selectedMidget;
				if( midget == null ) return;
				if( currentMidget != midget && currentMidget != null){
					// �ı�����ʾĿ�꣬�����ͷ���ǰĿ�����ʾ��
					currentMidget.disposeInAttributeView();
				}
				currentMidget = midget;
			}
		}
		if( currentMidget == null )return;
		// ��ʾ��ǰĿ�������
		currentMidget.showInAttributeView(this);
		
	}
	
	public void setFocus(){
	}

	public  boolean isWelcome(IMidget midget) {
		return false;
	}

	public boolean isInterest(IMidget midget) {
		return  (midget instanceof baseAttributeGuardMidget) || (midget == currentMidget);
	}

	public void processEventSource(MobileTotalEvent e) {
	}

	public Object getPaper() {
		return null;
	}
	
	public void refresh(){
		if( currentMidget == null )return;
		// ��ʾ���еĻ������ԣ�
		currentMidget.showInAttributeView(this);
	}

	
}
