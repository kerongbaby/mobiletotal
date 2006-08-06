package com.vicom.mdt.SystemMidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import com.vicom.mdt.Presenter.AbstractPresenter;
import com.vicom.mdt.event.MobileTotalAttribute;

/**
 * 这是一个伪目标，目的是让Presenter在关注事件流动中，能够准确地定位自己感兴趣的事件，并完成相应的处理。
 * @author ycwang
 *
 */

public class baseAttributeGuardMidget extends AbstractMidget {

	public	AbstractMidget selectedMidget = null;
	
	public static final String ID = "基本属性监视"; 
	
	private static baseAttributeGuardMidget instance = new baseAttributeGuardMidget();
	
	public static baseAttributeGuardMidget getInstance(){
		return instance;
	}
	
	private baseAttributeGuardMidget() {
		super(ID);
	}

	public String getStatus() {
		return "系统服务";
	}

	public void response(MobileTotalAttribute attribute) {
		
		if( attribute != null && attribute.name != null && attribute.object != null){
			try{
				setValue(attribute.name, attribute.object);
			}catch(Exception e){
				System.out.println("NoSuchField:" + attribute.name);
			}
		}
	}

	public void showInSceneView(AbstractPresenter presenter) {
	}


	public boolean isSystemMidget() {
		return true;
	}

	public String UpdateTimes() {
		return null;
	}

	
	private Table createBaseAttributeTable(Composite composite){
		Table	table;
		Color	background = new Color(null, 255, 255, 224);
		Color	foreground = new Color(null, 0, 0, 0);
		int		columnWidth[] = { 100, 300 };

		table = new Table(composite, SWT.BORDER  | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL);
		table.setBackground(background);
		table.setForeground(foreground);
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    table.setLayoutData(gd);
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    table.setBackground(background);
	    TableColumn column = new TableColumn(table, SWT.RIGHT, 0);
	    column.setText("属性名称");
	    column.setWidth(columnWidth[0]);
	    column = new TableColumn(table, SWT.LEFT, 1);
	    column.setText("属性值");	    
	    column.setWidth(columnWidth[1]);
		return table;
	}
	
	private Table attributeTable;
	public synchronized void showInAttributeView(AbstractPresenter presenter) {
		if( attributeTable == null ){
			attributeTable = createBaseAttributeTable(presenter.composite);
			presenter.composite.layout(true);
		}

		if( attributeTable != null){
			attributeTable.setRedraw(false);
			attributeTable.setItemCount(2);
			TableItem item = attributeTable.getItem(0);
			item.setText(0,"系统服务名称");
			item.setText(1,getMidgetIdentify());
				
			item = attributeTable.getItem(1);
			item.setText(0,"运行状态");
			item.setText(1,getStatus());
			attributeTable.setRedraw(true);
		}
	}

	public synchronized void disposeInAttributeView() {
		if(attributeTable !=null){
			attributeTable.dispose();
			attributeTable = null;
		}
	}

	public void disposeInSceneView() {
		// 系统服务,不能消失。 
	}
	
	
}
