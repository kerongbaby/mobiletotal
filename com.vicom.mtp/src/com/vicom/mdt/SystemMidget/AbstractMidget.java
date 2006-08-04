package com.vicom.mdt.SystemMidget;

import java.lang.reflect.Field;
import java.util.Hashtable;
import org.eclipse.swt.graphics.Image;
import com.vicom.mdt.MobileDeviceTotoalPlugin;
import com.vicom.mdt.Presenter.AbstractPresenter;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.event.MobileTotaEventPool;

public abstract class AbstractMidget implements IMidget{
	
	/**
	 * Midgetʵ��Ψһʶ���־��
	 */
	protected 	final		String 		identify;
	private 	static 		Hashtable	Midgets = new Hashtable();

	/**
	 * ��ϵͳ�����������Է����仯��ϵͳ�ļ����߻��ȡ����Ϣ����������Ӧ�Ķ�����
	 *
	 */
	protected void informAttributeChanged(){
		// ���������Ҳ�����˱仯����ᱨ��������ļ����ߡ�
		MobileTotalEvent event = new MobileTotalEvent(this,this,MobileTotalEvent.ATTRIBUTECHANGED,null);
		MobileTotaEventPool.putEvent(event);
	}
	
	public abstract boolean isSystemMidget();
	
	
	public String getMidgetIdentify() {
		return identify;
	}
	
	public static Hashtable getMidgets(){
		return Midgets;
	}
	
	
	/**
	 * Midget��������
	 * @param identify
	 */
	public AbstractMidget(String identify){
		this.identify = identify;

		// ����Midget����Midget��������
		Midgets.put(identify,this);

		// ����һ���µ�Midget�Ѿ�������
		MobileTotalEvent event = new MobileTotalEvent(this,baseAttributeGuardMidget.getInstance(),MobileTotalEvent.NEW_MIDGET,null);
		MobileTotaEventPool.putEvent(event);
	}
	
	public void removeMidgetSelf() {
		// ����Midget�Ƴ�Midge��������
		Midgets.remove(identify);

		// ���汾Midget�Ѿ��Ƴ���
		
		MobileTotalEvent event = new MobileTotalEvent(this,baseAttributeGuardMidget.getInstance(),MobileTotalEvent.REMOVE_MIDGET,null);
		MobileTotaEventPool.putEvent(event);
	}

	
	public synchronized Field getFiled(String fieldName) throws Exception {
		return this.getClass().getField(fieldName);
	}
	
	public synchronized void setValue(Field field, Object object) throws Exception {
		field.set(this,object);
	}
	
	
	public synchronized void setValue(String fieldName, Object object) throws Exception {
		setValue( getFiled(fieldName) , object );
	}
	
	

	
	
	public void showInAttributeView(AbstractPresenter presenter) {
	}

	public void disposeInAttributeView() {
	}

	public Image getMidgetIcon(){
        Image img = MobileDeviceTotoalPlugin.getImageDescriptor("icons/mt9000.gif").createImage();
		return img;
	}
}
