package com.vicom.mdt;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class MobileDeviceTotalPerspective implements IPerspectiveFactory {
	
	public IFolderLayout topLeft;
	public IFolderLayout topRight;
	public IFolderLayout bottom;
	
	public void createInitialLayout(IPageLayout layout) {
		createLayout(layout);
		loadClientPresenter(layout);
	}
	
	
	void createLayout(IPageLayout layout){
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(false);
		// Top left: 设备设施的信息视图
		topLeft = layout.createFolder("组织区", IPageLayout.LEFT, 0.25f,	editorArea);
		// Top right: 

		topRight = layout.createFolder("场景区", IPageLayout.RIGHT, 0.75f,editorArea);
		
		// bottom: 
		bottom = layout.createFolder("属性区", IPageLayout.BOTTOM, 0.75f,"场景区");
		
	}

	void loadClientPresenter(IPageLayout layout){
		Configer.loadView(this, layout);
	}
}
