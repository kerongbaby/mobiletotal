package com.vicom.mdt.SystemMidget;

import org.eclipse.swt.graphics.Image;
import com.vicom.mdt.MobileTotalAttribute;
import com.vicom.mdt.Presenter.AbstractPresenter;

public interface IMidget {
	
	/**
	 * 获取Midget的识别标志。该标志是区分不同Midget实例的唯一标志。该标志被用来获取Midget的实例。对于被管理的设备，也通过报告设备标志
	 * 来对应其在MobileTotalPlatform的Midget实例。
	 * @return
	 */
	public String 	getMidgetIdentify();
	
	/**
	 * 获取Midget的运行状态。
	 * @return
	 */
	public String 	getStatus();
	
	/**
	 * 属性是设备与其在MobileTotalPlatform之间传递数据的交换单元，该方法是Midget对设备表达的属性变更请求的响应。
	 * @param attribute
	 */
	public void 	response(MobileTotalAttribute attribute);
	
	/**
	 * 该Midget在场景视图中的表现方法。
	 * @param presenter
	 */
	public void 	showInSceneView(AbstractPresenter presenter);
	
	/**
	 * 当该Midget不在存在的时候，在场景视图中的处置方法。
	 *
	 */
	public void		disposeInSceneView();

	/**
	 * 该Midget在属性视图中的表现方法。
	 * @param presenter
	 */
	public void 	showInAttributeView(AbstractPresenter presenter);
	
	/**
	 * 当该Midget不是属性视图中的当前选中目标的时候，需要处理该Midget在属性视图中的表现的方法。
	 */
	public void		disposeInAttributeView();

	/**
	 * 该Midget属性被更新的次数。
	 * @return
	 */
	public String 	UpdateTimes();
	

	/**
	 * 当该Midget不在存在的时候，从Midget管理链中移除该Midget的方法和Midget本身资源释放的方法。
	 */
	public void 	removeMidgetSelf();

	/**
	 * 获取该Midget的图标的方法。
	 * @return
	 */
	public Image	getMidgetIcon();
	
}
