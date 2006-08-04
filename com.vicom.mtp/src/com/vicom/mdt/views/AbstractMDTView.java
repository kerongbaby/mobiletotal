package com.vicom.mdt.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.vicom.mdt.Presenter.AbstractPresenter;

public abstract class AbstractMDTView extends ViewPart implements IMobileTotalView {
	
	public AbstractPresenter presenter=null;
	
	public void createPartControl(Composite parent){
		invokePresent(parent);
	}
	
	public abstract void invokePresent(Composite composite);
	
}
