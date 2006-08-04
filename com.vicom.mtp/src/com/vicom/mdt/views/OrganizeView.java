package com.vicom.mdt.views;

import org.eclipse.swt.widgets.Composite;

import com.vicom.mdt.Presenter.OrganizePresenterTree;

public class OrganizeView extends AbstractMDTView {
	
	public void invokePresent(Composite composite){
		presenter = new OrganizePresenterTree(composite);
		presenter.loadMidgets(this);
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		presenter.setFocus();
	}
	
}