package com.vicom.mdt.SystemMidget;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.eclipse.swt.graphics.Image;
import com.vicom.mdt.Presenter.AbstractPresenter;
import com.vicom.mdt.event.MobileTotalAttribute;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.event.MobileTotaEventPool;

public class SocketChannelMidget  extends  AbstractMidget {
	private static SocketChannelMidget instance = new SocketChannelMidget();
    protected ServerSocket socket;
    long	handeTimes = 0;
	public static SocketChannelMidget getInstance(){
		return instance;
	}
	
	private SocketChannelMidget(){
		super("SocketChannelMidget");
		try{
			socket = new ServerSocket(7777);
			(new SocketServerThread()).start();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	public String getMidgetIdentify() {
		return "SocketChannelMidget";
	}

	public String getStatus() {
		return "Online";
	}

	
	public void handle(String identify,String name, Object param) {
		handeTimes++;
		IMidget midget = (IMidget)getMidgets().get(identify);
		if( midget == null ){
			// 这是一个模拟的过程。实际上需要向MD发起一个询问，当设备报告clazz的时候，才利用Creator产生Midget。
			MidgetCreator.getInstance().create(identify,"CameraMidget");
			midget = (IMidget)getMidgets().get(identify);
		} 
		
		
		if( midget != null) {
			MobileTotalAttribute attribute = new MobileTotalAttribute(identify,name,MobileTotalAttribute.POST,param);
			MobileTotalEvent event = new MobileTotalEvent(this,midget,MobileTotalEvent.ATTRIBUTECHANGED,attribute);
			MobileTotaEventPool.putEvent(event);
		}
	}
	
	class SocketServerThread extends Thread{

		public SocketServerThread(){
			super("SocketServerThread");
		}
		public void run(){
			
			while(true){
				try{
					Socket client = socket.accept();
					client.setSoTimeout(2000);
					(new ClientThread(client)).start();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
				
			}
		}
	}

	
	class ClientThread extends Thread {
		
		private final int MAX_IMG_BUF_LENGTH = 1024 * 100;
		private byte[] buf = new byte[MAX_IMG_BUF_LENGTH];
		private InputStream in;
		private OutputStream out;
		int total = 0;
		boolean flag = false;
		int len = 0;
		String identify;
		
		
		private Socket client;
		public ClientThread(Socket client){
			super("ClientThread:"+client.getInetAddress());
			this.client = client;
			this.identify = "Camera-"+client.getInetAddress().toString();
		}
		
		public void run() {
			int i = 0;
			System.out.println("Connect from " + client.getInetAddress());
			try {
				in = client.getInputStream();
				out = client.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (true) {
				try {

					out.write(0);

					if ((MAX_IMG_BUF_LENGTH - total) <= 0) {
						System.out.println("Fatal Error, no buffer!");
						flag = false;
						len = 0;
						total = 0;
						continue;
					}
					int le = in.read(buf, total, (MAX_IMG_BUF_LENGTH - total));
					total += le;
					//System.out.println("->" + le);
				} catch (java.net.SocketTimeoutException tou) {
					// skip!
					//System.out.print(".");
					continue;
				} catch (IOException ioe) {
					System.out.println("Closed???");
					break;
				}

				if (total > 2) {
					/* 收到数据，解析之 */
					if (buf[0] != -1 || buf[1] != -40) {
						System.out.println("Dirty");
						// 不是以0xFFD8开头的，丢弃
						// for (int j = 0; j < total; j++)buf[j] = 0;
						flag = false;
						len = 0;
						total = 0;
					} else {
						// 正常，搜寻下一个0xFFD8,
						for (i = 2; i < total; i += 2) {
							// if((buf[i]==-1)&&(buf[i+1]==-40)){
							// 寻找结束标志。
							if ((buf[i] == -1) && (buf[i + 1] == -39)) {
								break;
							}
						}
						if (i < total) {
							// 找到了，置标志位flag以便处理上一帧，此时i下标就是上一帧长度
							flag = true;
						} else {
							flag = false;
						}
					}
				}

				if (flag) {
					if (buf[0] == -1 && buf[1] == -40) {
						i += 2;
						System.out.println("a frame:" + i + "/" + total);

						InputStream in = new ByteArrayInputStream(buf, 0, i);
						Image img = new Image(null, in);
						handle(identify,"image", img);
						System.arraycopy(buf, i, buf, 0, total - i);
						total = total - i;
						len = 0;
						flag = false;
					}
				}
			}
			System.out.println("End of Channel!");
			IMidget midget = (IMidget)getMidgets().get(identify);
			midget.removeMidgetSelf();
		}
	}

	public void initAttributes() {
		// TODO Auto-generated method stub
		
	}

	public void response(MobileTotalAttribute attribute) {
		// TODO Auto-generated method stub
		
	}

	public void showInSceneView(AbstractPresenter presenter) {
		// TODO Auto-generated method stub
		
	}

	public boolean isSystemMidget() {
		return true;
	}

	public String UpdateTimes() {
		return Long.toString(handeTimes);
	}

	public void disposeInSceneView() {
		// 系统服务,不能消失。 
	}

	
}
