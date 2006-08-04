package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.vicom.mdt.CameraMidget.CameraMidget;

public class CameraBaseAttributeTable extends Table {
	Color	background = new Color(null, 255, 255, 224);
	Color	foreground = new Color(null, 0, 0, 0);
	int		columnWidth[] = { 150, 300, 300 };
	private	 CameraMidget midget;
	public CameraBaseAttributeTable(Composite parent, CameraMidget midget) {
		super(parent, SWT.BORDER  | SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL);
		this.midget = midget;
		setBackground(background);
		setForeground(foreground);
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    setLayoutData(gd);
	    setHeaderVisible(true);
	    setLinesVisible(true);
	    setBackground(background);

	    TableColumn column = new TableColumn(this, SWT.RIGHT, 0);
	    column.setText("��������");
	    column.setWidth(columnWidth[0]);
	    column = new TableColumn(this, SWT.LEFT, 1);
	    column.setText("����ֵ");	    
	    column.setWidth(columnWidth[1]);
	    
	    column = new TableColumn(this, SWT.LEFT, 2);
	    column.setText("˵��");	    
	    column.setWidth(columnWidth[2]);
	    layout(true);
	}
	
	public void update(){
		if( !isVisible() || this.isDisposed()) return;
		
		setRedraw(false);
		setItemCount(5);
		TableItem item = getItem(0);
		item.setText(0,"�豸����");
		item.setText(1,midget.getMidgetIdentify());
		
		item = getItem(1);
		item.setText(0,"�������¸���ʱ��");
		item.setText(1,midget.getStatus());

		item = getItem(2);
		item.setText(0,"ͼ�����ݸ��´���");
		item.setText(1,Long.toString(midget.imageUpdatetimes));
	
		item = getItem(3);
		item.setText(0,"GPS���ݸ��´���");
		item.setText(1,Long.toString(midget.gpsUpdatetimes));

		item = getItem(4);
		item.setText(0,"����λ��");
		item.setText(1,(midget.gps == null ? "û�л�ȡ����" : midget.gps));
		
		setRedraw(true);
		
	}
}
