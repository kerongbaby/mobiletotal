package com.vicom.mdt.event;

import java.util.Vector;

public class MobileTotaEventPool {
	
	static Vector events = new Vector();
	static boolean 	waitforEvent = false;
	
	static public void putEvent(MobileTotalEvent event){
		synchronized(events){
			events.add(event);
			if( waitforEvent ){
				waitforEvent = false;
				events.notify();
			}
		}
	}
	
	static public MobileTotalEvent getEvent(){
		synchronized(events){
			if( events.isEmpty()){
				waitforEvent = true;
				try{
					events.wait();
				}catch(InterruptedException ie){
					
				}
			}
			MobileTotalEvent e = (MobileTotalEvent)events.firstElement();
			events.remove(e);
			return e;
		}
	}
}
