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
		 * 构造模型实例。
		 * 构造ServerPresenter实例。
		 * 构造HMI baseClientPresenter，并装载需要的ClientPresenter及其Views
		 * 构造M2MI baseClientPresenter。
		 * 装载测试需要的TargetObject模拟实例。
		 */

		
		// 启动事件处理线程。
		MobileDeviceTotal.getInstance();
		
		// 启动数据库。
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
	 * 在实际环境中，MT不是动态进行管理的，是预先设定好需要管理的设备。这些设备信息往往
	 * 存放在配置中。需要预先调入。
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
		// 基于HTTP的数据汇聚。
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
		
		// 取消本视图的关闭功能
		layout.getViewLayout(TableListBrowserView.ID).setCloseable(false);
		perspective.topRight.addView(SimplePhotoView.ID);
		layout.getViewLayout(SimplePhotoView.ID).setCloseable(false);
		perspective.bottom.addView(AttributeView.ID);
		layout.getViewLayout(AttributeView.ID).setCloseable(false);
		//perspective.topRight.addView(BrowserView.ID);
	}
	
	
}
