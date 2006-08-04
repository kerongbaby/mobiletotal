package com.vicom.mdt.CameraMidget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class GPSData {
	
	// �����ݵĻ�ȡʱ��
	private Date	date;
	// �����ݱ���γ�ȡ�
	private	double	latitude;
	// �����ݱ��ľ��ȡ�
	private double	longitude;
	
	static private	DateFormat format = new SimpleDateFormat("hhmmss");
	
	public GPSData(Date date, double latitude, double longitude){
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	
	public static GPSData createGPSData(String rawGpsData){
		StringTokenizer st = new StringTokenizer(rawGpsData,",");
		try{
			String header = st.nextToken();
			String utc = st.nextToken();
			String valid = st.nextToken();
			// ���������Ч�����˳�.
			if( (!"$GPRMC".equals(header)) || "V".equals(valid)) return null;
			Date da = format.parse(utc);

			String latitude = st.nextToken();
			st.nextToken();
			String longitude = st.nextToken();
			st.nextToken();
			return new GPSData(da,Double.parseDouble(latitude),Double.parseDouble(longitude));
		
		}catch(Exception e){
			System.out.println("No more excepted token!!!");
			e.printStackTrace();
			return null;
		}
		
	}
		
	
	
	public String toString(){
		return "ʱ��:"+ format.format(date) + " ��γ:"+latitude + " ����:" + longitude;
	}


	public Date getDate() {
		return date;
	}


	public double getLatitude() {
		return latitude;
	}


	public double getLongitude() {
		return longitude;
	}
	
}
