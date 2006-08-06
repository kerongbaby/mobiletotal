package com.vicom.mdt.Presenter;

import java.util.Vector;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.vicom.mdt.SystemMidget.IMidget;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.views.IMobileTotalView;
import com.vicom.mdt.MobileDeviceTotal;

public abstract class AbstractPresenter implements IPresenter {
	
    public Composite composite;
	/**
	 * Midget容器管理
	 *
	 */
	protected Display display;
	
	/**
	 * 所有的视图都是MobileTotalPlatform的事件消费者。
	 *
	 */
	public AbstractPresenter(Composite composite){
		this.composite = composite;
		display = composite.getDisplay();
		MobileDeviceTotal.addListener(this);
	}
	
	
	public Display getDisplay() throws Exception{
		if( display == null ) throw new Exception("Display not Set!");
		return display;
	}
	
	
	protected void setDisplay(Display display){
		this.display = display;
	}
	
	
	public Vector midgetsContains = new Vector();
	
	/**
	 * 记录和管理本表现视图所拥有的Midget。主要目的是为本视图需要更新的时候能够枚举这些Midget。
	 * @param midget
	 * @return
	 */
	public IMidget addMidget(IMidget midget){
		// 如果这个Midget已经存在，不再次加入。
		if( midgetsContains.contains(midget)) return null;
		midgetsContains.add(midget);
		return midget;
	}
	
	/**
	 * 当该Midget不再需要在本视图中表现，需要冲容器中移除该Midget。
	 * @param midget
	 */
	public void removeMidget(IMidget midget){
		midgetsContains.remove(midget);
	}
	
	
	/**
	 * 载入视图
	 *
	 */
	public abstract void loadMidgets(IMobileTotalView view);

	public abstract void setFocus();
	
	/**
	 * 每个Presenter提供判断，是否处理相关的midget事件。
	 */
	
	/**
	 * 对事件来源的判断，是否欢迎来自这个Midget的事件。
	 */
	public	abstract	boolean isWelcome(IMidget midget);
	
	/**
	 * 当关注事件的来源时，用该方法处理。
	 * @param e
	 */
	public	abstract	void 	processEventSource(MobileTotalEvent e);
	// 对事件目标的判断，是否关注前往这个Midget的事件。
	public abstract 	boolean	isInterest(IMidget midget);
	
	/**
	 * 当关注事件的目标时，用该方法处理。
	 */
	public abstract		void processEventDestination(MobileTotalEvent e);

	/**
	 * 本视图需要更新时候的方法。 
	 */
	public abstract		void refresh();
}
