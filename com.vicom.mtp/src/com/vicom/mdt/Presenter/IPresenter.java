package com.vicom.mdt.Presenter;

import org.eclipse.swt.widgets.Display;

import com.vicom.mdt.event.MobileTotalEvent;

/**
 * IClientPresenter
 * @author ycwang
 *
 */
public interface IPresenter {
	public Display getDisplay() throws Exception;
	public void processEventDestination(MobileTotalEvent e);
}
