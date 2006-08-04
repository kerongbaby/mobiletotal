package com.vicom.mdt.Presenter;

import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.vicom.mdt.MobileTotalAttribute;
import com.vicom.mdt.SystemMidget.AbstractMidget;
import com.vicom.mdt.SystemMidget.IMidget;
import com.vicom.mdt.SystemMidget.baseAttributeGuardMidget;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.views.IMobileTotalView;
import com.vicom.mdt.event.MobileTotaEventPool;

public class OrganizePresenter extends AbstractPresenter {

	public OrganizePresenter(Composite composite) {
		super(composite);
	}

	HashMap targetComponent = new HashMap();
	
	private Table	table; 
	private Color	background = new Color(null, 255, 255, 224);
	private Color	foreground = new Color(null, 0, 0, 0);
	private int		columnWidth[] = { 205, 110, 100 };

	public void loadMidgets(IMobileTotalView view){
	    table = new Table(composite, SWT.BORDER  | SWT.SINGLE|SWT.FULL_SELECTION);
		table.setBackground(background);
		table.setForeground(foreground);
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    table.setLayoutData(gd);
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    table.setBackground(background);
	    // TODO: 这些标题头应该来源于配置文件，表格的宽度没有设置。
	    TableColumn column = new TableColumn(table, SWT.RIGHT, 0);
	    column.setText("设备编号");
	    column.setWidth(columnWidth[0]);
	    column = new TableColumn(table, SWT.LEFT, 1);
	    column.setText("设备类型");	    
	    column.setWidth(columnWidth[1]);
	    
	    column = new TableColumn(table, SWT.LEFT, 2);
	    column.setText("设备说明");	    
	    column.setWidth(columnWidth[2]);
	    
	    
	    table.addSelectionListener( new SelectionListener(){

			public void widgetSelected(SelectionEvent e) {
				MobileTotalAttribute attribute  = new MobileTotalAttribute(baseAttributeGuardMidget.ID,"selectedMidget",MobileTotalAttribute.POST,e.item.getData());
				MobileTotalEvent event = new MobileTotalEvent((IMidget)(e.item.getData()),baseAttributeGuardMidget.getInstance(),MobileTotalEvent.ATTRIBUTECHANGED,attribute);
				MobileTotaEventPool.putEvent(event);
//				System.out.println(e.item.getData().toString());
				
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("defSelect "+e.toString());
				
			}} );
	    
		
	}
	
	public void processEventDestination(MobileTotalEvent e){
	}
	
	public void refresh(){
		if( table == null) return;
		if(table.isDisposed()) return;
		
        table.getDisplay().asyncExec(new Runnable() {
            public void run() {
  			  synchronized(table){
  				  if( !table.isDisposed()){
	  			      table.setRedraw(false);
	  				Object[] tb = AbstractMidget.getMidgets().values().toArray();
	  				synchronized(tb){
	  			      int num = tb.length;
	  				  table.setItemCount(num);
	  				  // 更新表格中的内容。
	  			      for( int i=0; i< num ; i++){
	  				      TableItem item = table.getItem(i);
	  				      	item.setData(tb[i]);
	  				        item.setText(0, ((IMidget)tb[i]).getMidgetIdentify() );
	  				        item.setText(1,((IMidget)tb[i]).getStatus());
	  				      }
	  				}
	  				     table.setRedraw(true);
  				  }
  			  }
            }
	  });
		
	}
	
	public void setFocus(){
		table.setFocus();
	}

	public  boolean isWelcome(IMidget midget) {
		return true;
	}

	public boolean isInterest(IMidget midget) {
		return  midget instanceof baseAttributeGuardMidget ;
	}

	/**
	 * 1、新Midget生成后，会发送宣告事件，组织视图处理这个事件。
	 */
	public void processEventSource(MobileTotalEvent e) {
		refresh();
	}

	/**
	 * 组织视图不提供各个Midget的表现平台，对Midget的表现通过统一的办法来实行。
	 */
	public Object getPaper() {
		return null;
	}
	
}
