package com.vicom.mdt.CameraMidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.CameraBaseAttributeTable;
import org.eclipse.swt.widgets.CameraHistoryImagesGroup;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.vicom.mdt.Presenter.AbstractPresenter;

public class Attributes {
	CameraMidget		midget;
    private TabFolder	AttributeTableFolder;
	private TabItem		attributeListItem;
	private TabItem		HistoryImagesItem;
	
	public Attributes(AbstractPresenter presenter, CameraMidget midget){
		this.midget = midget;

		// 构造 TableFolder.
		AttributeTableFolder = new TabFolder(presenter.composite, SWT.NONE);

		attributeListItem = createTableItem(AttributeTableFolder, "基本属性", new CameraBaseAttributeTable(AttributeTableFolder, midget));
		HistoryImagesItem = createTableItem(AttributeTableFolder, "历史图像", new CameraHistoryImagesGroup(AttributeTableFolder, midget));


		// 当选择特定的TabItem的时候,更新改Item的数据。
		AttributeTableFolder.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {
				update();
			}
	    });
		
		
		
		AttributeTableFolder.layout(true);
		presenter.composite.layout(true);
		
	}
	public void update(){
		if( attributeListItem != null){
			CameraBaseAttributeTable ct = (CameraBaseAttributeTable)attributeListItem.getControl();
			ct.update();
		}
		if( HistoryImagesItem != null ){
			CameraHistoryImagesGroup ct = (CameraHistoryImagesGroup)HistoryImagesItem.getControl();
			ct.update();
		}
	}
	
	public void dispose(){
		if(attributeListItem !=null){
			attributeListItem.dispose();
			attributeListItem = null;
		}
		if( AttributeTableFolder != null ){
			AttributeTableFolder.dispose();
			AttributeTableFolder = null;
		}
	}

    private TabItem createTableItem(TabFolder tabfoler, String name, Control control){
    	TabItem ti = new TabItem(tabfoler, SWT.NONE);
		ti.setText(name);
		ti.setControl(control);
		return ti;
    }
	
	
}
