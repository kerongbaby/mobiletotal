package com.vicom.mdt.Presenter;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeAdapter;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import com.vicom.mdt.MobileTotalAttribute;
import com.vicom.mdt.MobileDeviceTotoalPlugin;
import com.vicom.mdt.SystemMidget.AbstractMidget;
import com.vicom.mdt.SystemMidget.IMidget;
import com.vicom.mdt.SystemMidget.baseAttributeGuardMidget;
import com.vicom.mdt.event.MobileTotalEvent;
import com.vicom.mdt.views.IMobileTotalView;
import com.vicom.mdt.event.MobileTotaEventPool;

public class OrganizePresenterTree extends AbstractPresenter {
    public OrganizePresenterTree(Composite composite) {
		super(composite);
	}

	protected static final String TREEITEMDATA_IMAGEEXPANDED = "expanded";
	protected static final String TREEITEMDATA_IMAGECOLLAPSED = "collapsed";
	private Tree tree;
    private TreeItem sysGroup;
    private TreeItem deviceGroup;

	private Color	background = new Color(null, 255, 255, 224);

	public void loadMidgets(IMobileTotalView view){

		Menu menu = new Menu(composite.getShell(), SWT.POP_UP);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 2;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		
		tree = new Tree(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
		tree.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		tree.setBackground(background);
        tree.setHeaderVisible(false);
        tree.setLinesVisible(false);

	    MenuItem it = new MenuItem(menu, SWT.CASCADE);
	    //it.setText("what you want");
    	it.addSelectionListener( new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void widgetSelected(SelectionEvent e) {
				System.out.println(e.widget);
			}
    		
    	});
	    //it.addSelectionListener(yourListener);
	    MenuItem it2 = new MenuItem(menu, SWT.CASCADE);
	    it2.setText("what you want");

        menu.addMenuListener(new MenuAdapter() {
			   public void menuShown(MenuEvent e) {
			    Menu menu = (Menu) e.widget;
			    TreeItem[] selection = tree.getSelection();
			    MenuItem mi = menu.getItems()[0];
			    if( selection[0].getData() instanceof IMidget ){
			    	IMidget midget = ((IMidget)selection[0].getData());
			    	mi.setText(midget.getStatus());
			    }   else {
			    	mi.setText(selection[0].getText());
			    }
			    // -- and fill your popup --
			    }
			  });
        
        
        tree.setMenu(menu);
       
        
        addItems();

	
		tree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				final TreeItem[] selection = tree.getSelection();
				if (selection != null && selection.length != 0) {
					//TreeItem item = selection[0];
					MobileTotalAttribute attribute  = new MobileTotalAttribute(baseAttributeGuardMidget.ID,"selectedMidget",MobileTotalAttribute.POST,e.item.getData());
					MobileTotalEvent event = new MobileTotalEvent((IMidget)(e.item.getData()),baseAttributeGuardMidget.getInstance(),MobileTotalEvent.ATTRIBUTECHANGED,attribute);
					MobileTotaEventPool.putEvent(event);
				}
			}
			public void widgetDefaultSelected(SelectionEvent event) {
				final TreeItem[] selection = tree.getSelection();
				if (selection != null && selection.length != 0) {
					TreeItem item = selection[0];
					item.setExpanded(true);
					//treeExpandItem(item);
				}
			}
		});
		
		tree.addTreeListener(new TreeAdapter() {
			public void treeExpanded(TreeEvent event) {
				final TreeItem item = (TreeItem) event.item;
				final Image image = (Image) item.getData(TREEITEMDATA_IMAGEEXPANDED);
				if (image != null) item.setImage(image);
				//treeExpandItem(item);
			}
			public void treeCollapsed(TreeEvent event) {
				final TreeItem item = (TreeItem) event.item;
				final Image image = (Image) item.getData(TREEITEMDATA_IMAGECOLLAPSED);
				if (image != null) item.setImage(image);
			}
		});	
	
	
	}
	
    private void addItems(){
        Image expanded = MobileDeviceTotoalPlugin.getImageDescriptor("icons/expandOrganize.gif").createImage();
        Image collapsed = MobileDeviceTotoalPlugin.getImageDescriptor("icons/collapsedOrganize.gif").createImage();

        
        deviceGroup = new TreeItem(tree, SWT.NONE);
        deviceGroup.setText(new String[] { "被控设备"});
        
        deviceGroup.setImage(collapsed);
        deviceGroup.setData(TREEITEMDATA_IMAGEEXPANDED,expanded);
        deviceGroup.setData(TREEITEMDATA_IMAGECOLLAPSED,collapsed);
        deviceGroup.setExpanded(true);
        
    	sysGroup = new TreeItem(tree, SWT.NONE);
        sysGroup.setText(new String[] { "系统服务"});
        sysGroup.setImage(collapsed);
        sysGroup.setData(TREEITEMDATA_IMAGEEXPANDED,expanded);
        sysGroup.setData(TREEITEMDATA_IMAGECOLLAPSED,collapsed);
        sysGroup.setExpanded(true);
        
    }	
	
	private boolean isExist(AbstractMidget midget){
		TreeItem[] items = midget.isSystemMidget() ? sysGroup.getItems() : deviceGroup.getItems();
		for( int looper = 0; looper < items.length; looper++){
			if( items[looper].getData() == midget) return true;
		}
		return false;
	}
	
	private void addMidget(AbstractMidget midget){
		if( isExist(midget)) return;
		TreeItem ti = new TreeItem( midget.isSystemMidget() ? sysGroup : deviceGroup, SWT.NONE);
		ti.setText(new String[]{ midget.getMidgetIdentify()});
		Image img = midget.getMidgetIcon();
		if(img != null)	ti.setImage(img);
		ti.setData(midget);
	}
	

	private void removeAllTreeItem(){
		deviceGroup.removeAll();
		sysGroup.removeAll();
	}
	
	public void reGroupMidget(){
		removeAllTreeItem();
		Hashtable midgets = AbstractMidget.getMidgets();
		Enumeration e = midgets.elements();
		while( e.hasMoreElements()){
			AbstractMidget amidget =(AbstractMidget) e.nextElement();
			addMidget(amidget);
		}
		
	}
	
	public void refresh(){
		if( tree == null || tree.isDisposed()) return;
		tree.setRedraw(false);
		reGroupMidget();
		tree.setRedraw(true);
	}
	
	
	public void setFocus(){
		tree.setFocus();
	}

	public  boolean isWelcome(IMidget midget) {
		return false;
	}

	public boolean isInterest(IMidget midget) {
		return  midget instanceof baseAttributeGuardMidget ;
	}

	public void processEventDestination(MobileTotalEvent e){
		if( (e.EventType == MobileTotalEvent.NEW_MIDGET) || (e.EventType == MobileTotalEvent.REMOVE_MIDGET))
			refresh();
	}
	
	/**
	 * 1、新Midget生成后，会发送宣告事件，组织视图处理这个事件。
	 */
	public void processEventSource(MobileTotalEvent e) {
		//refresh();
	}

	/**
	 * 组织视图不提供各个Midget的表现平台，对Midget的表现通过统一的办法来实行。
	 */
	public Object getPaper() {
		return null;
	}
	
}
