package com.vicom.mdt.SystemMidget;

import org.eclipse.swt.graphics.Image;
import com.vicom.mdt.MobileTotalAttribute;
import com.vicom.mdt.Presenter.AbstractPresenter;

public interface IMidget {
	
	/**
	 * ��ȡMidget��ʶ���־���ñ�־�����ֲ�ͬMidgetʵ����Ψһ��־���ñ�־��������ȡMidget��ʵ�������ڱ�������豸��Ҳͨ�������豸��־
	 * ����Ӧ����MobileTotalPlatform��Midgetʵ����
	 * @return
	 */
	public String 	getMidgetIdentify();
	
	/**
	 * ��ȡMidget������״̬��
	 * @return
	 */
	public String 	getStatus();
	
	/**
	 * �������豸������MobileTotalPlatform֮�䴫�����ݵĽ�����Ԫ���÷�����Midget���豸�������Ա���������Ӧ��
	 * @param attribute
	 */
	public void 	response(MobileTotalAttribute attribute);
	
	/**
	 * ��Midget�ڳ�����ͼ�еı��ַ�����
	 * @param presenter
	 */
	public void 	showInSceneView(AbstractPresenter presenter);
	
	/**
	 * ����Midget���ڴ��ڵ�ʱ���ڳ�����ͼ�еĴ��÷�����
	 *
	 */
	public void		disposeInSceneView();

	/**
	 * ��Midget��������ͼ�еı��ַ�����
	 * @param presenter
	 */
	public void 	showInAttributeView(AbstractPresenter presenter);
	
	/**
	 * ����Midget����������ͼ�еĵ�ǰѡ��Ŀ���ʱ����Ҫ�����Midget��������ͼ�еı��ֵķ�����
	 */
	public void		disposeInAttributeView();

	/**
	 * ��Midget���Ա����µĴ�����
	 * @return
	 */
	public String 	UpdateTimes();
	

	/**
	 * ����Midget���ڴ��ڵ�ʱ�򣬴�Midget���������Ƴ���Midget�ķ�����Midget������Դ�ͷŵķ�����
	 */
	public void 	removeMidgetSelf();

	/**
	 * ��ȡ��Midget��ͼ��ķ�����
	 * @return
	 */
	public Image	getMidgetIcon();
	
}
