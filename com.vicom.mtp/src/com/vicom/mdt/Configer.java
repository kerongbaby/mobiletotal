package com.vicom.mdt;

import java.io.IOException;
import java.sql.Connection;

import org.eclipse.ui.IPageLayout;
import pygmy.core.Server;
import com.vicom.mdt.views.AttributeView;
import com.vicom.mdt.views.PhotoView.SimplePhotoView;
import com.vicom.mdt.views.browser.TableListBrowserView;

public class Configer {
	
	private final static boolean emulatorCamera = true;
	private final static boolean debugdatabase = false;

	// Web������Ҫ���ݽ���ͨ����
	static public Server webServer=null;

	public static Connection getConnection(){
		return DerbyConnect.getConnection();
	}
	
	
	static class ServiceThread extends Thread {
		public void run(){
			// �������ݿ⡣
			startupDatabase();
			// �����¼������̡߳�
			MobileDeviceTotal.getInstance();
			startupCollect();
			startupEmulatorMT();
			preLoadMidget();
		}
	}
	

	
	public synchronized static void startup(){
		(new ServiceThread()).start();
	}
	
	static void startupDatabase(){
		DerbyConnect.startupDerby();
		if(debugdatabase)DerbyConnect.testDerby();
	}

	static void shutdownDatabase(){
		DerbyConnect.shutdownDerby();
	}
	
	public static void stop(){
		stopCollect();
		shutdownDatabase();
	}
	
	/**
	 * ��ʵ�ʻ����У�MT���Ƕ�̬���й���ģ���Ԥ���趨����Ҫ������豸����Щ�豸��Ϣ����
	 * ����������С���ҪԤ�ȵ��롣
	 *
	 */
	private static void preLoadMidget(){
		//IMidget testChannelMidget = null;
		//SocketChannelMidget.getInstance();
		//TestResponseMidget.getInstance();
	}
	private static void startupEmulatorMT(){
		if(emulatorCamera){
			new com.vicom.mdt.test.Camera.TestCameraMidget(1);
			new com.vicom.mdt.test.Camera.TestCameraMidget(2);
		}
	}
	

	private static void startupCollect(){
		// ����HTTP�����ݻ�ۡ�
		try{
			webServer = new Server();
			webServer.start();
		}catch(IOException e){
			e.printStackTrace();
		}
	}	

	private static void stopCollect(){
		if( webServer != null){
			webServer.shutdown();
		}
	}

	public synchronized static void loadView(MobileDeviceTotalPerspective perspective, IPageLayout layout){
		// ������֯������ͼ��
		perspective.topLeft.addView(TableListBrowserView.ID);
		// ȡ������ͼ�Ĺرչ���
		layout.getViewLayout(TableListBrowserView.ID).setCloseable(false);

		// ���볡����ͼ��
		perspective.topRight.addView(SimplePhotoView.ID);
		layout.getViewLayout(SimplePhotoView.ID).setCloseable(false);

		//����������ͼ��
		perspective.bottom.addView(AttributeView.ID);
		layout.getViewLayout(AttributeView.ID).setCloseable(false);
		//perspective.topRight.addView(BrowserView.ID);
		
	}
	
	
}
