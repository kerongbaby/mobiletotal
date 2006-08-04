package com.vicom.mdt.CameraMidget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;
import com.vicom.mdt.Configer;

public class DatabaseManage {
	
	private static 	Statement	statement = ConnectToDatabase();
	private Timestamp date = new Timestamp(0);

	private synchronized static Statement ConnectToDatabase(){
		try{
			Connection conn = Configer.getConnection();
			Statement s = conn.createStatement();
			try{
				//s.execute("drop table GPSDBS");
				//s.execute("create table GPSDBS(identify varchar(20) NOT NULL, datetime DATE, latitude DOUBLE , longitude DOUBLE)");
				
				s.execute("create table IMGS(identify varchar(20) NOT NULL, logtime TIMESTAMP, img BLOB)");
			}catch(SQLException se){
				se.printStackTrace();
				//可能这个表已经建好了。
			}

			
			try{
				//s.execute("drop table GPSDBS");
				//s.execute("create table GPSDBS(identify varchar(20) NOT NULL, datetime DATE, latitude DOUBLE , longitude DOUBLE)");
				
				s.execute("create table GPSDBS(id INT generated always as identity, identify varchar(20) NOT NULL, datetime TIMESTAMP, latitude DOUBLE , longitude DOUBLE)");
			}catch(SQLException se){
				//se.printStackTrace();
				//可能这个表已经建好了。
			}
			return s;
		}catch(SQLException sqe){
			sqe.printStackTrace();
			return null;
		}
	}
	
	
	public synchronized void storeImage(CameraMidget midget, byte[] img){
		if( statement == null ) return;
		String sql = "insert into IMGS(identify, logtime ,img) values ('" + midget.getMidgetIdentify() + "',CURRENT_TIMESTAMP,?)"; 
		  try {
			PreparedStatement pstmt = statement.getConnection().prepareStatement(sql);
			pstmt.setBytes(1, img);
		    pstmt.executeUpdate();
		    pstmt.close();
		  } catch (SQLException sqlExcept) {
			  sqlExcept.printStackTrace();
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
	}

	public synchronized void logGPS(CameraMidget midget, GPSData data){
		if( statement == null ) return;
		//Date date = new Date( data.getDate().getTime() );
		date.setTime(data.getDate().getTime());
//		 inserts a date value
		String sql_insertValue ="INSERT INTO GPSDBS (identify,datetime,latitude,longitude) VALUES ('" + midget.getMidgetIdentify() + "',?,?,?)" ;
		try{
			PreparedStatement pstmt = statement.getConnection().prepareStatement( sql_insertValue );
			pstmt.setTimestamp( 1, date );
			pstmt.setDouble(2,data.getLatitude());
			pstmt.setDouble(3,data.getLongitude());
			pstmt.executeUpdate();
			pstmt.close();		
		}catch(SQLException sqe){
			sqe.printStackTrace();
		}
		
	}

	public synchronized Vector loadGPSData(String identify){
		Vector datas = new Vector();
		int maxDatas = 0;
//		 read data
//		String sql_readData ="SELECT TOP 10 datetime,latitude,longitude FROM GPSDBS where identify = '"+ identify + "'";
		String sql_readData ="SELECT datetime,latitude,longitude FROM GPSDBS where identify = '"+ identify + "' order by id desc";
		try{
			ResultSet rset = statement.executeQuery( sql_readData );
			while( rset.next()){
				if( maxDatas++ > 10 ) break;
				datas.add(new GPSData(rset.getTimestamp(1),rset.getDouble(2),rset.getDouble(3)));
				
				Timestamp dVal = rset.getTimestamp(1);
				System.out.println(dVal.getTime());
				
				double latitude = rset.getDouble(2);
				System.out.println(latitude);

				double longitude = rset.getDouble(3);
				System.out.println(longitude);
			}
			rset.close();		
		}catch(SQLException sqe){
			sqe.printStackTrace();
		}
		return datas;
	}
	
	
}
