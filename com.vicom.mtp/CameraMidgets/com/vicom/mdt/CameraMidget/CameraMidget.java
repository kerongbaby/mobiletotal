package com.vicom.mdt.CameraMidget;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import com.vicom.mdt.MobileTotalAttribute;
import com.vicom.mdt.MobileDeviceTotoalPlugin;
import com.vicom.mdt.Presenter.AbstractPresenter;
import com.vicom.mdt.SystemMidget.AbstractMidget;
import com.vicom.mdt.SystemMidget.IMidget;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.LightweightSystem;

public class CameraMidget extends AbstractMidget  {
	private final int MAXCACHEDIMAGES = 4;

	public Integer	position;
	public String	gps;
	private	long	lastActivetime	= 0;
	public long		lastShowTime	= 0;
	public long		gpsUpdatetimes  = 0;
	public long		imageUpdatetimes= 0;
	long			dataupdatetimes	= 0;
	public ImageHistory	imagehistory;
	CameraEyes 		eyeView;
	Vector			gpsHistory;
	static private DatabaseManage	database = new DatabaseManage();
	
	public Image getMidgetIcon(){
        Image img = MobileDeviceTotoalPlugin.getImageDescriptor("icons/camera.gif").createImage();
		return img;
	}

	public CameraMidget(String identify){
		super(identify);
		imagehistory = new ImageHistory();
		gpsHistory = database.loadGPSData(identify);
		(new timeoutWatchDog(this)).start();
	}
	
	private class timeoutWatchDog extends Thread{
		IMidget midget;
		public timeoutWatchDog(IMidget midget){
			this.midget = midget;
		}
		public void run(){
			try{
				Thread.sleep(600*1000);	// 600秒
			}catch(InterruptedException ie){
				// skip
			}
			midget.removeMidgetSelf();
		}
	}
	
	public void removeMidgetSelf(){
		super.removeMidgetSelf();
	}
	
	public String getMidgetIdentify(){
		return identify;	//+"(车载监控)";
	}
	
	public String getStatus(){
		if( lastActivetime == 0 )return "还未获得设备数据";
		Date date = new Date(lastActivetime);
		return DateFormat.getDateTimeInstance().format(date);
	}
	
	public synchronized void response(MobileTotalAttribute attribute){

		lastActivetime = System.currentTimeMillis();
		dataupdatetimes++;
		if("image".equalsIgnoreCase(attribute.name)){
			try{
				InputStream in = new ByteArrayInputStream((byte[])attribute.object);
				Image img = new Image(null,in);
				imagehistory.push(img);
				imageUpdatetimes++;
				database.storeImage(this,(byte[])attribute.object);
			}catch(Exception e){
				// something wrong!
			}
			return;
		} else if( "gps".equalsIgnoreCase(attribute.name)){
			GPSData gpsdata = GPSData.createGPSData((String)attribute.object);
			gps = gpsdata.toString();// processGPSData((String)attribute.object);
			gpsUpdatetimes++;
			database.logGPS(this,gpsdata);
		}
		
	}

	
	static Figure figure = null;

	public synchronized void showInSceneView(AbstractPresenter presenter){
		
		if( figure == null ){
			Composite composite = presenter.composite;
			// paint canvas
			Canvas paintCanvas = new Canvas(composite, SWT.BORDER );
			composite.layout(true);
			
			LightweightSystem lws = new LightweightSystem(paintCanvas);
			figure = new Figure();
			figure.setFocusTraversable(false);
			figure.setRequestFocusEnabled(true);
			FlowLayout layout = new FlowLayout();
			figure.setLayoutManager(layout);
		    lws.setContents(figure);
		}
		
		if( figure == null) return;
		if( eyeView == null){
			eyeView = new CameraEyes(getMidgetIdentify());
			figure.add(eyeView);
		}
		
		Image img = imagehistory.getLatest();
		if(img != null){
			eyeView.setIcon(imagehistory.getLatest());
			}

		eyeView.setGPSInfo((gps == null ? "" : (" 位置：" + gps)));
	}
	
	public class ImageHistory {
		private Image nullImage = new Image(null,320,240);
		Image[] cachedImages = new Image[MAXCACHEDIMAGES];
		int current = 0;
		
		public ImageHistory(){
			for( int looper =0 ; looper < MAXCACHEDIMAGES ; looper ++){
				cachedImages[looper] = nullImage;
			}
		}
		
		public synchronized Image getLatest(){
			return cachedImages[current];
		}
		
		public synchronized Image getImageIndex(int index){
			return cachedImages[index];
		}
		
		public  synchronized  void push(Image image){
			if( cachedImages[current] != nullImage ){ 
				current = ++current % MAXCACHEDIMAGES;
			}
			if( cachedImages[current] != nullImage )
				cachedImages[current].dispose();

			cachedImages[current] = image;
		}
		
		public void disposeAll(){
			for( int looper= 0 ; looper < MAXCACHEDIMAGES; looper++){
				if( !cachedImages[looper].isDisposed() )
					cachedImages[looper].dispose();
			}
		}
	}

	public boolean isSystemMidget() {
		return false;
	}

	public String UpdateTimes() {
		return Long.toString(dataupdatetimes);
	}
	
    private	Attributes attributs;
	public void showInAttributeView(AbstractPresenter presenter) {
		
		if( attributs == null ){
			attributs = new Attributes(presenter,this);
		} 
		attributs.update();
	}

	public void disposeInAttributeView() {
		attributs.dispose();
		attributs = null;
	}

	public void disposeInSceneView() {
		if( figure != null && eyeView != null) figure.remove(eyeView);
	}
	
	
    
	
	
}
