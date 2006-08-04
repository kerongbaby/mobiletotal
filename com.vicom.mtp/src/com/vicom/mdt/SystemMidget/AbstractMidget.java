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
	 * Midget实例唯一识别标志。
	 */
	protected 	final		String 		identify;
	private 	static 		Hashtable	Midgets = new Hashtable();

	/**
	 * 向系统报告自身属性发生变化。系统的监视者会获取该消息，并产生响应的动作。
	 *
	 */
	protected void informAttributeChanged(){
		// 自身的属性也发生了变化。这会报告给其他的监视者。
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
	 * Midget构造器。
	 * @param identify
	 */
	public AbstractMidget(String identify){
		this.identify = identify;

		// 将该Midget纳入Midget管理链。
		Midgets.put(identify,this);

		// 宣告一个新的Midget已经创建。
		MobileTotalEvent event = new MobileTotalEvent(this,baseAttributeGuardMidget.getInstance(),MobileTotalEvent.NEW_MIDGET,null);
		MobileTotaEventPool.putEvent(event);
	}
	
	public void removeMidgetSelf() {
		// 将该Midget移除Midge管理链。
		Midgets.remove(identify);

		// 宣告本Midget已经移出。
		
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
