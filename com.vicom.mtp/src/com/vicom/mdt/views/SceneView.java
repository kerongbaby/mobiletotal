package com.vicom.mdt.views;

import org.eclipse.swt.widgets.Composite;

import com.vicom.mdt.Presenter.ScenePresenter;

public class SceneView extends AbstractMDTView {

	public void invokePresent(Composite composite){
		presenter = new ScenePresenter(composite);
		presenter.loadMidgets(this);
	}

	public void setFocus() {
		presenter.setFocus();
	}
}
