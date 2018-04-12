package org.aashay.spit.SRDMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySql {
	
	public Statement connectToDatabase()
	{
		//System.out.println("Hello world");
		try
		{
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/SRDMS","root","root");    
			Statement stmt=con.createStatement(); 
			//System.out.println(stmt);
			return stmt;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
}
