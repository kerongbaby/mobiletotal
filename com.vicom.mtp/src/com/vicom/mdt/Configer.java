package com.vicom.mdt;

import java.io.IOException;
import java.sql.Connection;

import org.eclipse.ui.IPageLayout;
import com.vicom.mdt.SystemMidget.IMidget;
import pygmy.core.Server;
import com.vicom.mdt.views.AttributeView;
import com.vicom.mdt.views.PhotoView.SimplePhotoView;
import com.vicom.mdt.views.browser.TableListBrowserView;

public class Configer {
	
	private final static boolean debug = false; 

	public static Connection getConnection(){
		return DerbyConnect.getConnection();
	}
	
	public static void startup(){
		/*
		 * ����ģ��ʵ����
		 * ����ServerPresenterʵ����
		 * ����HMI baseClientPresenter����װ����Ҫ��ClientPresenter����Views
		 * ����M2MI baseClientPresenter��
		 * װ�ز�����Ҫ��TargetObjectģ��ʵ����
		 */

		
		// �����¼������̡߳�
		MobileDeviceTotal.getInstance();
		
		// �������ݿ⡣
		startupDatabase();

		
		startupCollect();
		startupEmulatorMT();
		preLoadMidget();
		loadLogPresenter();
		loadChannelPresenter();
	}
	
	static void startupDatabase(){
		DerbyConnect.startupDerby();
		if(debug)DerbyConnect.testDerby();
	}

	static void shutdownDatabase(){
		DerbyConnect.shutdownDerby();
	}
	
	public static void stop(){
		stopCollect();
		shutdownDatabase();
	}
	
	private static void loadChannelPresenter(){
		//new ChannelPresenter();
	}
	private static void loadLogPresenter(){
		//new LogPresenter();
	}
	
	static IMidget testChannelMidget = null;
	
	/**
	 * ��ʵ�ʻ����У�MT���Ƕ�̬���й���ģ���Ԥ���趨����Ҫ������豸����Щ�豸��Ϣ����
	 * ����������С���ҪԤ�ȵ��롣
	 *
	 */
	private static void preLoadMidget(){
		//SocketChannelMidget.getInstance();
		//TestResponseMidget.getInstance();
	}
	private static void startupEmulatorMT(){
		if(debug){
		/*
		new com.vicom.mdt.test.DemoMobileDevice("mobileCamera-1");
		new com.vicom.mdt.test.DemoMobileDevice("mobileCamera-2");
		new com.vicom.mdt.test.DemoMobileDevice("mobileCamera-3");
		new com.vicom.mdt.test.DemoMobileDevice("mobileCamera-4");
		new com.vicom.mdt.test.DemoMobileDevice("mobileCamera-5");
		new com.vicom.mdt.test.DemoMobileDevice("mobileCamera-6");
		*/
		}
	}
	
	static private Server webServer=null;
	
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

	public static void loadView(MobileDeviceTotalPerspective perspective, IPageLayout layout){
		perspective.topLeft.addView(TableListBrowserView.ID);
		
		// ȡ������ͼ�Ĺرչ���
		layout.getViewLayout(TableListBrowserView.ID).setCloseable(false);
		perspective.topRight.addView(SimplePhotoView.ID);
		layout.getViewLayout(SimplePhotoView.ID).setCloseable(false);
		perspective.bottom.addView(AttributeView.ID);
		layout.getViewLayout(AttributeView.ID).setCloseable(false);
		//perspective.topRight.addView(BrowserView.ID);
	}
	
	
}
