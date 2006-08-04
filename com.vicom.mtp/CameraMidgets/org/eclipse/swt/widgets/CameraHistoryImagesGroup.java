package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import com.vicom.mdt.CameraMidget.CameraMidget;

public class CameraHistoryImagesGroup extends Group {
	
	private final static int MAXCACHEDIMAGES = 4;
	
	private Label[] cahcedImageLabel = new Label[MAXCACHEDIMAGES];
	
	private	CameraMidget midget;
	public CameraHistoryImagesGroup(Composite parent, CameraMidget midget) {
		super(parent, SWT.BORDER);
		setText("最近"+MAXCACHEDIMAGES+"图片");
		this.midget = midget;
		
		GridLayout gridLayout= new GridLayout ();
		setLayout(gridLayout);
		gridLayout.numColumns = 4;
		
		gridLayout.makeColumnsEqualWidth = true;

		//control.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL));
		
		for( int looper = 0; looper < MAXCACHEDIMAGES; looper++){
			cahcedImageLabel[looper] = new Label(this, SWT.BORDER);
			cahcedImageLabel[looper].setImage(midget.imagehistory.getImageIndex(looper));
		}
		
		this.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				System.out.println("Image:"+e.toString());
				
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		layout(true);
		//Composite control = new Composite(composite, SWT.BORDER );
		/*
		FillLayout layout = new FillLayout();
		layout.marginHeight =140;
		layout.marginWidth  =160;
		control.setLayout(layout );
		*/
		//Label control = new Label(composite,SWT.BORDER);
		/*
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		
		(new Label(control, SWT.BORDER)).setText("Demop1");
		(new Label(control, SWT.NONE)).setText("Demop2");
		
		
		*/
		/*
		Button buttonLeft = new Button(control, SWT.LEFT);
		buttonLeft.setText("前面");

		Button buttonRight = new Button(control, SWT.RIGHT);
		buttonRight.setText("后面");
		control.layout(true);
		//composite.layout(true);
		 */
	}
	public void update(){
		if( !isVisible() || this.isDisposed() ) return;
		for( int looper = 0; looper < MAXCACHEDIMAGES; looper++){
			Image cachedImage = midget.imagehistory.getImageIndex(looper);
			if( cachedImage == null ) continue;
			if( cahcedImageLabel[looper] == null ){
				cahcedImageLabel[looper].setImage(cachedImage);
			} else if( cachedImage != cahcedImageLabel[looper].getImage()){
				cahcedImageLabel[looper].setImage(cachedImage);
			}
		}
		
	}

}
