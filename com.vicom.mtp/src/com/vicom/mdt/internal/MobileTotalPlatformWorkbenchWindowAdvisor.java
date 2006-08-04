package com.vicom.mdt.internal;

//import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.IWorkbenchPreferenceConstants;


public class MobileTotalPlatformWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public MobileTotalPlatformWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new MobileTotalPlatfoemActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowCoolBar(true);
	    configurer.setShowPerspectiveBar(true);
	    configurer.setShowStatusLine(true);
	    configurer.setShowProgressIndicator(true);
	    // By default dock the perspective bar on the top-right side
	    PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR, IWorkbenchPreferenceConstants.TOP_RIGHT);
	    // Show the curvy view tabs
	    PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		//configurer.setTitle("无线终端管理");
	}

}
