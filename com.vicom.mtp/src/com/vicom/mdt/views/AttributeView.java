package com.vicom.mdt.views;

import org.eclipse.swt.widgets.Composite;

import com.vicom.mdt.Presenter.AttributePresenter;


public class AttributeView extends AbstractMDTView {
    public static final String ID =
        "com.vicom.mdt.views.AttributeView"; //$NON-NLS-1$


    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#setFocus()
     */
    public void setFocus() {
    	presenter.setFocus();
    }
    
    /**
     * Cleans up all resources created by this ViewPart.
     */
    public void dispose() {
        super.dispose();
    }

	public void invokePresent(Composite composite) {
		presenter = new AttributePresenter(composite);
		presenter.loadMidgets(this);
	}
    
}
