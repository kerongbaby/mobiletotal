package com.vicom.mdt.test.Camera;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import com.vicom.mdt.Configer;
import com.vicom.mdt.Presenter.AbstractPresenter;
import com.vicom.mdt.SystemMidget.AbstractMidget;
import com.vicom.mdt.event.MobileTotalAttribute;

/**
 * 该类模拟车载终端向 Mobile Total Platform 发送数据的行为。
 * @author ycwang
 *
 */

public class TestCameraMidget extends AbstractMidget {
	long	handleTimes = 0;
	private	String remoteIdentify;
	public TestCameraMidget(int id){
		super("emulator"+id);
		remoteIdentify = "Camera-emulator"+id;
		(new emulatorMT9000()).start();
	}

	public String getStatus() {
		return "系统服务";
	}


	public void response(MobileTotalAttribute attribute) {
	}

	public void showInSceneView(AbstractPresenter presenter) {
	}

	public boolean isSystemMidget() {
		return true;
	}

	public String UpdateTimes() {
		return Long.toString(handleTimes);
	}
    
	private Table createBaseAttributeTable(Composite composite){
		Table	table;
		Color	background = new Color(null, 255, 255, 224);
		Color	foreground = new Color(null, 0, 0, 0);
		int		columnWidth[] = { 100, 300 };

		table = new Table(composite, SWT.BORDER  | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL);
		table.setBackground(background);
		table.setForeground(foreground);
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    table.setLayoutData(gd);
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    table.setBackground(background);
	    TableColumn column = new TableColumn(table, SWT.RIGHT, 0);
	    column.setText("属性名称");
	    column.setWidth(columnWidth[0]);
	    column = new TableColumn(table, SWT.LEFT, 1);
	    column.setText("属性值");	    
	    column.setWidth(columnWidth[1]);
		return table;
	}
	
	private Table attributeTable;
	public synchronized void showInAttributeView(AbstractPresenter presenter) {
		if( attributeTable == null ){
			attributeTable = createBaseAttributeTable(presenter.composite);
			presenter.composite.layout(true);
		}

		if( attributeTable != null){
			attributeTable.setRedraw(false);
			attributeTable.setItemCount(2);
			TableItem item = attributeTable.getItem(0);
			item.setText(0,"系统服务名称");
			item.setText(1,getMidgetIdentify());
				
			item = attributeTable.getItem(1);
			item.setText(0,"运行状态");
			item.setText(1,getStatus());

			
			attributeTable.setRedraw(true);
		}
	}

	public synchronized void disposeInAttributeView() {
		if(attributeTable !=null){
			attributeTable.dispose();
			attributeTable = null;
		}
	}


	public void disposeInSceneView() {
		// nothing 
	}
			
	class emulatorMT9000 extends Thread{
		String baseUrl;
		Random r;
		int p = 0;
		boolean registed = false;
		public void triger(String id){
			try{
				URL url = new URL(baseUrl + "/postPosition?identify="+remoteIdentify);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.getResponseMessage();
			}catch(Exception e){
				//e.printStackTrace();
			}
		}
		
		public void postImages(String id){
			try{
				URL url = new URL(baseUrl+"/postImage?identify="+remoteIdentify);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				InputStream in = this.getClass().getResourceAsStream("/icons/"+id+".jpg");
				byte[] onebyte = new byte[in.available()];
				while(in.available() > 0){
					int len = in.read(onebyte);
					out.write(onebyte, 0, len);
				}

				out.flush();
				out.close();

				connection.getResponseMessage();
			}catch(Exception e){
				//e.printStackTrace();
			}
		}
				
		
		
		public emulatorMT9000(){
			super("demoEventThread");
        	int port = Integer.parseInt( Configer.webServer.getHandlerProperty( Configer.webServer.getProperty("endpoints"), "port"));
        	baseUrl = "http://localhost:"+port+"/risetek";
			
			r = new Random();
		}
		public void run(){
			while(true){
				if( !registed ){
					// 首先注册本设备。
					try{
						URL url = new URL(baseUrl + "/register?identify="+remoteIdentify);
						HttpURLConnection connection = (HttpURLConnection)url.openConnection();
						if( connection.getResponseCode() == 200 ){
							registed = true;
						}
					}catch(Exception e){
						//e.printStackTrace();
					}
				}
				p = r.nextInt(3);
				// 让底层报告位置。
				triger(identify);
				// 发送相片
				postImages(identify);
				
				try{
					int sleep = r.nextInt(20000);
					Thread.sleep(500+sleep);
				}catch(InterruptedException ie){
					ie.printStackTrace();
				}
			}
		}
	}



}
