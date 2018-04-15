package org.aashay.spit.SRDMS;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class StockService {

	MySql mysql=new MySql();
	
	public ArrayList<Stock> getAllFromDatabase()
	{
		ArrayList<Stock> list=new ArrayList<>();
		try
		{
			Statement stmt=mysql.connectToDatabase();
			ResultSet rs=stmt.executeQuery("select * from stocks natural join price");
			while(rs.next())
				list.add(new Stock(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getTimestamp(4).toString()));
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return list;
	}
	public ArrayList<Stock> getStockByIsin(String isin)
	{
		ArrayList<Stock> list=new ArrayList<>();
		isin=isin.toUpperCase();
		try
		{
			Statement stmt=mysql.connectToDatabase();
			ResultSet resultSet=stmt.executeQuery("select * from stocks natural join price where ISIN='"+isin+"'");
			while(resultSet.next())
				list.add(new Stock(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getTimestamp(4).toString()));
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return list;
	}
	public ArrayList<Stock> getStockByDate(String isin,String begin,String end)
	{
		ArrayList<Stock> list=new ArrayList<>();
		try
		{
			Statement stmt=mysql.connectToDatabase();
			ResultSet rs=stmt.executeQuery("select * from stocks natural join price where ISIN='"+isin+"' and date(Time) between '"+begin+"' and '"+end+"'");
			while(rs.next())
				list.add(new Stock(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getTimestamp(4).toString()));
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return list;
	}
	public ArrayList<Stock> getAllByDate(String begin,String end)
	{
		ArrayList<Stock> list=new ArrayList<>();
		try
		{
			Statement stmt=mysql.connectToDatabase();
			ResultSet rs=stmt.executeQuery("select * from stocks natural join price where date(Time) between '"+begin+"' and '"+end+"'");
			while(rs.next())
				list.add(new Stock(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getTimestamp(4).toString()));
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return list;
	}
	public ArrayList<Stock> getEarlierStock(String isin,int days)
	{
		ArrayList<Stock> list=new ArrayList<>();
		try
		{
			Timestamp current=new Timestamp(System.currentTimeMillis());
			Date date=new Date(current.getTime());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, -days);
			Timestamp past=new Timestamp(cal.getTime().getTime());
			Statement stmt=mysql.connectToDatabase();
			ResultSet rs=stmt.executeQuery("select * from stocks natural join price where ISIN='"+isin+"' and date(Time) between '"+past.toString().substring(0, 10)+"' and '"+current.toString().substring(0, 10)+"'");
			while(rs.next())
				list.add(new Stock(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getTimestamp(4).toString()));
			if(list.isEmpty())
				list.add(getLatestPrice(stmt,isin));
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return list;
	}
	public ArrayList<Stock> getEarlierStock(int days)
	{
		ArrayList<Stock> list=new ArrayList<>();
		try
		{
			Timestamp current=new Timestamp(System.currentTimeMillis());
			Date date=new Date(current.getTime());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, -days);
			Timestamp past=new Timestamp(cal.getTime().getTime());
			Statement stmt=mysql.connectToDatabase();
			ResultSet rs=stmt.executeQuery("select * from stocks natural join price where date(Time) between '"+past.toString().substring(0, 10)+"' and '"+current.toString().substring(0, 10)+"'");
			while(rs.next())
				list.add(new Stock(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getTimestamp(4).toString()));
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return list;
	}
	public ArrayList<Stock> getLatest(String isin)
	{
		ArrayList<Stock> list=new ArrayList<>();
		try
		{
			Statement stmt=mysql.connectToDatabase();
			ResultSet rs=stmt.executeQuery("select stock_name,ISIN,price,max(Time) as Time from stocks natural join price group by ISIN having ISIN ='"+isin+"'");
			if(rs.next())
				list.add(new Stock(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getTimestamp(4).toString()));
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return list;
	}
	public ArrayList<Stock> getAllLatest()
	{
		ArrayList<Stock> list=new ArrayList<>();
		try
		{
			Statement stmt=mysql.connectToDatabase();
			ResultSet rs=stmt.executeQuery("select stock_name,ISIN,Price,max(Time) as Time from stocks natural join price group by ISIN");
			while(rs.next())
				list.add(new Stock(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getTimestamp(4).toString()));
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return list;
	}
	public Stock getLatestPrice(Statement stmt, String isin) {
		try
		{
			ResultSet rs=stmt.executeQuery("select stock_name,ISIN,Price,max(Time) as Time from stocks natural join price group by ISIN having ISIN ='"+isin+"'");
			if(rs.next())
				return new Stock(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getTimestamp(4).toString());
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
	public int postToDatabase(Stock stock)
	{
		try
		{
			Statement stmt=mysql.connectToDatabase();
			checkIsin(stmt,stock);
			return stmt.executeUpdate("Insert into price values('"+stock.getStockName()+"',"+stock.getPrice()+",'"+stock.getTimestamp()+"')");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return -1;
	}
	public void checkIsin(Statement stmt, Stock stock) throws SQLException 
	{	
		try
		{
			ResultSet rs=stmt.executeQuery("select * from stocks");
			HashMap<String,String> isinMap=new HashMap<>();
			while(rs.next())
				isinMap.put(rs.getString(1),rs.getString(2));
			if(!isinMap.containsKey(stock.getIsin().toUpperCase()) && !isinMap.containsValue(stock.getStockName()))
				stmt.executeUpdate("Insert into stocks values('"+stock.getIsin()+"','"+stock.getStockName()+"')");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	public int offsetToDays(String offset) {
		//Add functions to get stock's price 1D 1W 1M 3M 6M 1Y 2Y 5Y MAX
		switch(offset)
		{
			case "1D":
				return 1;
			case "1W":
				return 7;
			case "1M":
				return 30;
			case "3M":
				return (int)(Math.round(30.4375*3));
			case "6M":
				return (int)(Math.round(30.4375*6));
			case "1Y":
				return 365;
			case "2Y":
				return (int)(Math.round(2*365.25));
			case "5Y":
				return (int)(Math.round(5*365.25));
			default:
				return 0;
		}
	}
}
