package com.vicom.mdt.CameraMidget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class GPSData {
	
	// 该数据的获取时间
	private Date	date;
	// 该数据表达的纬度。
	private	double	latitude;
	// 该数据表达的精度。
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
			// 如果数据无效，就退出.
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
		return "时间:"+ format.format(date) + " 北纬:"+latitude + " 东经:" + longitude;
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
