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
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.event.MobileTotaEventPool;

/**
 * MidgetCreator类用于生产Midget。
 * 当MT报告属性变化事件的时候，MT首先确定该MD是否有对应的Midget，如果没有，那么MT会要求本MidgetCreator进行
 * 进一步的处理，直到生成了该MD的对应Midget，否则MT不会处理MD报告的属性变化事件。
 */

public class MidgetCreator extends AbstractMidget {

	static MidgetCreator instance =  new MidgetCreator();
	static final String ID = "动态组件生成管理";
	long	midgetCreateTimes = 0;
	static public MidgetCreator getInstance(){
		return instance;
	}
	
	private MidgetCreator() {
		super(ID);
	}

	public String getStatus() {
		return "系统服务";
	}

	private static int handleTimes = 0;
	public void handle(String identify,String name, Object param) {
		IMidget midget = (IMidget)getMidgets().get(identify);
		if( midget == null ){
			midgetCreateTimes++;
			// 这是一个模拟的过程。实际上需要向MD发起一个询问，当设备报告clazz的时候，才利用Creator产生Midget。
			create(identify,"CameraMidget");
			midget = (IMidget)getMidgets().get(identify);
		}
		
		if(midget != null ){
			handleTimes++;
			MobileTotalAttribute attribute = new MobileTotalAttribute(identify,name,MobileTotalAttribute.POST,param);
			MobileTotalEvent event = new MobileTotalEvent(this,midget,MobileTotalEvent.ATTRIBUTECHANGED,attribute);
			MobileTotaEventPool.putEvent(event);

			// 自身的属性也发生了变化。这会报告给其他的监视者。
			event = new MobileTotalEvent(this,this,MobileTotalEvent.ATTRIBUTECHANGED,attribute);
			MobileTotaEventPool.putEvent(event);
			
		}
	}
	
	
	
	IMidget	create(String identify, String clazz){
		IMidget produce = null;
		String className = "com.vicom.mdt."+clazz+"."+clazz;
		try{
			Class c = Class.forName(className);
			Class[] paramClass = {String.class};
			Object[] param = {identify};
			produce = (IMidget)c.getConstructor(paramClass).newInstance(param);

			// 自身的属性也发生了变化。这会报告给其他的监视者。
			informAttributeChanged();
		
		
		}catch(Exception e){
			produce =	new UnknownMidget(identify);
		}
		return produce;
	}
	
	public void response(MobileTotalAttribute attribute) {
		//System.out.println(attribute.identify);
	}

	public void showInSceneView(AbstractPresenter presenter) {
		// Nothing to do.
	}


	public boolean isSystemMidget() {
		return true;
	}

	public String UpdateTimes() {
		return Long.toBinaryString(midgetCreateTimes);
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
			attributeTable.setItemCount(4);
			TableItem item = attributeTable.getItem(0);
			item.setText(0,"系统服务名称");
			item.setText(1,getMidgetIdentify());
				
			item = attributeTable.getItem(1);
			item.setText(0,"运行状态");
			item.setText(1,getStatus());

			item = attributeTable.getItem(2);
			item.setText(0,"动态组件生成次数");
			item.setText(1,Long.toString(midgetCreateTimes));
			
			item = attributeTable.getItem(3);
			item.setText(0,"变量处理次数");
			item.setText(1,Long.toString(handleTimes));
			
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
