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
import com.vicom.mdt.MobileDeviceTotoalPlugin;
import com.vicom.mdt.SystemMidget.AbstractMidget;
import com.vicom.mdt.SystemMidget.IMidget;
import com.vicom.mdt.SystemMidget.baseAttributeGuardMidget;
import com.vicom.mdt.event.MobileTotalAttribute;
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

		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 2;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		
		tree = new Tree(composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
		tree.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		tree.setBackground(background);
        tree.setHeaderVisible(false);
        tree.setLinesVisible(false);

        // Ϊ��֯�������ӻ��������˵���

        Menu menu = new Menu(tree);        
	    MenuItem menuitem = new MenuItem(menu, SWT.CASCADE);
    	menuitem.addSelectionListener( new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("widgetDefaultSelected");
			}
			public void widgetSelected(SelectionEvent e) {
				System.out.println(e.widget);
			}
    	});

    	/*
    	//it.addSelectionListener(yourListener);
	    MenuItem it2 = new MenuItem(menu, SWT.CASCADE);
	    it2.setText("what you want");
		*/
    	
        menu.addMenuListener(new MenuAdapter() {
			   public void menuShown(MenuEvent menuevent) {

				   Menu menu = (Menu) menuevent.widget;

				   MenuItem[] items = menu.getItems();
		            for (int i = 0; i < items.length; i++) {
		               ((MenuItem) items[i]).dispose();
		            }
		            // Add menu items for current selection
		            MenuItem newItem = new MenuItem(menu, SWT.NONE);
		            newItem.setText("Menu for " + tree.getSelection()[0].getText());				   
				   
				   
				   TreeItem selection = tree.getSelection()[0];
				   MenuItem mi = menu.getItems()[0];
				    if( selection.getData() instanceof IMidget ){
				    	IMidget midget = ((IMidget)selection.getData());
				    	mi.setText(midget.getStatus());
				    }   else {
				    	//mi.setText(selection[0].getText());
				    	//mi.setText("Noting!");
				    }
			    }
			  });
        
        tree.setMenu(menu);
        addLevelOneItems();
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
		
		// ���µĴ��봦��չ�����ս����ͼ����ʾ��
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
	
		// ����ͼװ����ϣ�������ʾ���е�Midget��
		refresh();
	}
	
    private Image expandedIcon = MobileDeviceTotoalPlugin.getImageDescriptor("icons/expandOrganize.gif").createImage();
    private Image collapsedIcon = MobileDeviceTotoalPlugin.getImageDescriptor("icons/collapsedOrganize.gif").createImage();
    private void addLevelOneItems(){
        deviceGroup = new TreeItem(tree, SWT.NONE);
        deviceGroup.setText(new String[] { "�����豸"});
        
        deviceGroup.setImage(collapsedIcon);
        deviceGroup.setData(TREEITEMDATA_IMAGEEXPANDED,expandedIcon);
        deviceGroup.setData(TREEITEMDATA_IMAGECOLLAPSED,collapsedIcon);
        deviceGroup.setExpanded(true);
        
    	sysGroup = new TreeItem(tree, SWT.NONE);
        sysGroup.setText(new String[] { "ϵͳ����"});
        sysGroup.setImage(collapsedIcon);
        sysGroup.setData(TREEITEMDATA_IMAGEEXPANDED,expandedIcon);
        sysGroup.setData(TREEITEMDATA_IMAGECOLLAPSED,collapsedIcon);
        sysGroup.setExpanded(true);
        
    }	
	
	public void setFocus(){
		tree.setFocus();
	}

	public  boolean isWelcome(IMidget midget) {
		return false;
	}

	
	public boolean isInterest(IMidget midget) {

		// Midget�����ɺͳ������ᷢ�������¼������¼���Ŀ����ϵͳ������
		// Organize��Ŀ����ϵͳ��������¼����д���
		
		return  midget instanceof baseAttributeGuardMidget ;
	}

	private Hashtable midgetTreeItemtable = new Hashtable();
	

	/**
	 * 1����Midget���ɺ󣬻ᷢ�������¼�����֯��ͼ��������¼���
	 */
	
	public synchronized void processEventDestination(MobileTotalEvent event){
		if( event.EventType == MobileTotalEvent.NEW_MIDGET ){
			// ���µ�Midget���֣�����Midget�������������������Midget����
			createTreeItemForMidget(event.sourceMidget);
		}else if (event.EventType == MobileTotalEvent.REMOVE_MIDGET){
			// һ��Midget���ٴ��ڣ���Midget���������Ƴ���Midget����������Midget����
			removeTreeItemForMidget(event.sourceMidget);
		}
	}
	
	
	private void createTreeItemForMidget(IMidget midget){
		// ���µ�Midget���֣�����Midget�������������������Midget����
		if( midgetTreeItemtable.contains(midget) ) return;

		// Ϊ��Midget�����Ӧ��TreeItem��
		TreeItem ti = new TreeItem( midget.isSystemMidget() ? sysGroup : deviceGroup, SWT.NONE);
		ti.setText(new String[]{ midget.getMidgetIdentify()});
		Image img = midget.getMidgetIcon();
		if(img != null)	ti.setImage(img);
		ti.setData(midget);
	}
	
	private void removeTreeItemForMidget(IMidget midget){
		if( !midgetTreeItemtable.contains(midget) ) return;
		TreeItem[] allitems = midget.isSystemMidget() ?  sysGroup.getItems() : deviceGroup.getItems();
		for( int looper = 0; looper < allitems.length ; looper++){
			if( allitems[looper].getData() == midget ) allitems[looper].dispose();
		}
	}
	
	public void processEventSource(MobileTotalEvent e) {
		// Nothing to do.
	}

	/**
	 * ��֯��ͼ���ṩ����Midget�ı���ƽ̨����Midget�ı���ͨ��ͳһ�İ취��ʵ�С�
	 */
	public Object getPaper() {
		return null;
	}

	// ����ͼ�����ÿ������ڷ������������ô�����Ѿ����ڵ��豸����Ҫ��һ���������̡�
	public void refresh() {
		Enumeration midgets = AbstractMidget.getMidgets().elements();
		while( midgets.hasMoreElements()){
			createTreeItemForMidget((IMidget)midgets.nextElement());
		}
	}
	
}
