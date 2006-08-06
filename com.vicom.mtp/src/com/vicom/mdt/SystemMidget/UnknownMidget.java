package com.vicom.mdt.SystemMidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import com.vicom.mdt.Presenter.AbstractPresenter;
import com.vicom.mdt.event.MobileTotalAttribute;

public class UnknownMidget extends AbstractMidget  {
	long dataupdatetimes = 0;
	public Image image;
	public Integer position;
	
	static long[] timeStamp = {0,0,0,0};
	
	static int selectEmpty(){
		int min = 0;
		for( int looper = 0; looper < timeStamp.length; looper++){
			if( timeStamp[min] > timeStamp[looper]) min = looper;
		}
		return min;
	}
	
	public UnknownMidget(String identify){
		super(identify);
	}
	
	public String getMidgetIdentify(){
		return identify;
	}
	
	public String getStatus(){
		return "UnKnownDevice";
	}
	
	public synchronized void response(MobileTotalAttribute attribute){
		dataupdatetimes++;
		// 更新设备活动状态。
		// FIXME： image需要dispose!!
		try{
			if( (image != null) && ("image".equalsIgnoreCase(attribute.name)) ) image.dispose();
			setValue(attribute.name, attribute.object);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	Canvas scenseCanvas = null;
	int pos;
	public synchronized void showInSceneView(AbstractPresenter presenter){
		if( image == null || image.isDisposed())return;
		if( scenseCanvas == null ){
			scenseCanvas  = new Canvas(presenter.composite,SWT.NONE);
		}
		pos = selectEmpty();
		timeStamp[pos] = System.currentTimeMillis();
		GC gc = new GC(scenseCanvas);
		gc.drawImage(image,0,0,image.getBounds().width,image.getBounds().height,(pos * 330),0,image.getBounds().width,image.getBounds().height);
		String label = identify + " 位置：" + (position == null ? "没有提供" : position.toString()); 
		gc.drawText( label, (pos *330 + 10 ), 10 );
		gc.dispose();
	}

	public boolean isSystemMidget() {
		return false;
	}

	public String UpdateTimes() {
		return Long.toString(dataupdatetimes);
	}

	public void disposeInSceneView() {
		System.out.println("Not implements");
	}
}
