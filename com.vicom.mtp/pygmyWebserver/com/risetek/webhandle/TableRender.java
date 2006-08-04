package com.risetek.webhandle;

import java.util.Enumeration;
import java.util.Vector;
public class TableRender {
	private Vector TableVector = new Vector();
	public Table newTable(String name)
	{
		Table t = new Table(name);
		TableVector.add(t);
		return t;
	}
/// return string-sb send to browser	
	public String Render(int[] widthpercent)
	{
		StringBuffer sb= new StringBuffer();
		int	tableCounter = 0;
		Enumeration e = TableVector.elements();
		Table table;
		
		sb.append("<TABLE width=760 cellspacing=0 cellpadding=0 border=0><TR><TD>");
		//sb.append("<TABLE width=760 cellspacing=1 cellpadding=1 border=1><TR><TD>");
		
		while( e.hasMoreElements())
		{
			if( tableCounter++ > 0)	
				sb.append("<td>&nbsp</td>");
			
			if( TableVector.size() > 1)
			{
				if( (tableCounter % 2) == 1)
					sb.append("<TD align=left>"); 
				else 
					sb.append("<TD align=right>");
			}
			
			table = (Table)e.nextElement();
			sb.append(table.Render(widthpercent));
		}
		sb.append("</TD></TR></TABLE>");
		
		return sb.toString();
		
	}
	
	public class Table
	{
		String	TableName;
		Vector	rowsVector = new Vector();
		int 	maxColumn=0;///column length
		boolean	 white=true;///background colour is white
		
		public Table(String name)
		{
			TableName = name;
		}
		
		public void AddRow(String[] rows)
		{
			if( maxColumn < rows.length) 
					maxColumn = rows.length;
			
			rowsVector.add(rows);
		}
		
		public String Render(int[] widthpercent)
		{
			StringBuffer sb= new StringBuffer();
			sb.append("<table cellspacing=0 class='partsmb' width=100%><tr><td class='modhead' colspan=");
			//sb.append("<table  cellspacing=0 border=1 class='partsmb' width=100%><tr><td class='modhead' colspan=");///////////////////////////
			sb.append(maxColumn);
			sb.append(">");
			sb.append(TableName);
			sb.append("</td></tr>");
			Enumeration e = rowsVector.elements();
			while( e.hasMoreElements())
			{

				sb.append("<tr><td class='line' colspan=");
				sb.append(maxColumn);
				sb.append("></td></tr>");
				
				String[] rows = (String[])e.nextElement();
				sb.append("<tr class='");
				sb.append(white ? "whiterow" : "bluerow");
				white = !white;
				sb.append("'>");
				
				int looper;
				boolean wd=true;
				for(looper = 0; looper < rows.length; looper++)
				{
					if( wd && widthpercent.length == maxColumn)
					{
						sb.append("<td width=");
						sb.append(widthpercent[looper]);
						sb.append("%>");
					}else 
					{
						sb.append("<td>");
					}
					sb.append(rows[looper]);
					sb.append("</td>");
				}
				if( wd && widthpercent.length == maxColumn) wd=false;
				// 多于的列.
				for(;looper< maxColumn; looper++)
				{
					sb.append("<td>&nbsp</td>");
				}
				sb.append("</tr>");
			}
			sb.append("</table>");
			return sb.toString();
		}
	}
}
