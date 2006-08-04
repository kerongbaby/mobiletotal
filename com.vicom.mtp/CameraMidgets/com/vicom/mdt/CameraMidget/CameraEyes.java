package com.vicom.mdt.CameraMidget;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.FrameBorder;

public class CameraEyes extends Panel {
	private Label 	iconLabel;
	private Label	GPSInfo;
	private Border	border;
	public CameraEyes(String Identify) {
		super();
		setFocusTraversable(true);
		setRequestFocusEnabled(true);
		border = new FrameBorder(Identify);
		setBorder(border);
		setLayoutManager(new FlowLayout(false));
		iconLabel = new Label(Identify);
		iconLabel.setBackgroundColor(new Color(null,0,0,128));
		Image blankImg = new Image(null,320,240);
		iconLabel.setIcon(blankImg);
		iconLabel.setTextPlacement(PositionConstants.NORTH);
		this.add(iconLabel);
		GPSInfo = new Label("");
		this.add(GPSInfo);
	}

	public void setGPSInfo(String gpsinfo){
		GPSInfo.setText(gpsinfo);
	}
	
	public void setIcon(Image icon){
		iconLabel.setIcon(icon);
	}
}


