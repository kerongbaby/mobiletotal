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
 * ����һ��αĿ�꣬Ŀ������Presenter�ڹ�ע�¼������У��ܹ�׼ȷ�ض�λ�Լ�����Ȥ���¼����������Ӧ�Ĵ���
 * @author ycwang
 *
 */

public class baseAttributeGuardMidget extends AbstractMidget {

	public	AbstractMidget selectedMidget = null;
	
	public static final String ID = "�������Լ���"; 
	
	private static baseAttributeGuardMidget instance = new baseAttributeGuardMidget();
	
	public static baseAttributeGuardMidget getInstance(){
		return instance;
	}
	
	private baseAttributeGuardMidget() {
		super(ID);
	}

	public String getStatus() {
		return "ϵͳ����";
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
	    column.setText("��������");
	    column.setWidth(columnWidth[0]);
	    column = new TableColumn(table, SWT.LEFT, 1);
	    column.setText("����ֵ");	    
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
			item.setText(0,"ϵͳ��������");
			item.setText(1,getMidgetIdentify());
				
			item = attributeTable.getItem(1);
			item.setText(0,"����״̬");
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
		// ϵͳ����,������ʧ�� 
	}
	
	
}
