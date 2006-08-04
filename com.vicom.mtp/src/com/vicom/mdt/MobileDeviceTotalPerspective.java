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
		// Top left: �豸��ʩ����Ϣ��ͼ
		topLeft = layout.createFolder("��֯��", IPageLayout.LEFT, 0.25f,	editorArea);
		// Top right: 

		topRight = layout.createFolder("������", IPageLayout.RIGHT, 0.75f,editorArea);
		
		// bottom: 
		bottom = layout.createFolder("������", IPageLayout.BOTTOM, 0.75f,"������");
		
	}

	void loadClientPresenter(IPageLayout layout){
		Configer.loadView(this, layout);
	}
}
