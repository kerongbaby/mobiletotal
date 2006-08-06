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

	// Web服务，主要数据接入通道。
	static public Server webServer=null;

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
		// 启动数据库。
		startupDatabase();

		
		// 启动事件处理线程。
		MobileDeviceTotal.getInstance();
		
		startupCollect();
		startupEmulatorMT();
		preLoadMidget();
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
	 * 在实际环境中，MT不是动态进行管理的，是预先设定好需要管理的设备。这些设备信息往往
	 * 存放在配置中。需要预先调入。
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
		// 载入组织管理视图。
		perspective.topLeft.addView(TableListBrowserView.ID);
		// 取消本视图的关闭功能
		layout.getViewLayout(TableListBrowserView.ID).setCloseable(false);

		// 载入场景试图。
		perspective.topRight.addView(SimplePhotoView.ID);
		layout.getViewLayout(SimplePhotoView.ID).setCloseable(false);

		//载入属性视图。
		perspective.bottom.addView(AttributeView.ID);
		layout.getViewLayout(AttributeView.ID).setCloseable(false);

		//perspective.topRight.addView(BrowserView.ID);
		
		// 视图装载完毕，才能启动其它数据服务，否则会出现事件丢失的情况。
		Configer.startup();

		
	}
	
	
}
