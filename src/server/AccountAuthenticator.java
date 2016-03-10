package server;

import java.sql.*;

public class AccountAuthenticator {
	
	private Connection con;
	
	
	//Creates table if it doesn't exist
	//Adds two dummy users to database
	public AccountAuthenticator()
	{
		if(!tableExists())
		{	
			createTable();
		}
		
		addUser("test","test");
		addUser("user","user");
	}
	
	//Checks if userName/password combo exists in database
	public boolean userExists(String userName, String password)
	{
		connect();
		
		try {
			Statement stmt = con.createStatement();
			String query = "SELECT * FROM USERS " +
						   "WHERE USERNAME = '" + userName + "' " +
						   "AND PASSWORD = '" + password + "'";
			ResultSet rs = stmt.executeQuery(query);			
			
			if(rs.next())
			{
				rs.close();
				stmt.close();
				con.close();
				
				return true;
			}
			
			rs.close();
			stmt.close();
			con.close();
		
		} catch(SQLException ex) {
			
			ex.printStackTrace();
		} 
		
		return false;
	}
	
	//Adds userName/password combo to database
	//Returns true if add was successful, false otherwise
	public boolean addUser(String userName, String password)
	{
		if(userExists(userName,password))
		{
			return false;
		}
		
		connect();
		
		try {
			
			Statement stmt = con.createStatement();
			
			String query = "INSERT INTO USERS (USERNAME,PASSWORD) " +
	                   	   "VALUES ('" + userName + "', '" + password + "');"; 
			
			stmt.executeUpdate(query);
	   
			stmt.close();
			con.close();
			
			return true;
			
		} catch(SQLException ex) {
			
			return false;
		}
	}
	
	//Creates Users table if it doesn't exist
	//Can add more fields if needed
	private void createTable() {
			
		connect();
		
		try {
			Statement stmt = con.createStatement();
			String query = "CREATE TABLE USERS " +
	                 	   "(USERNAME TEXT PRIMARY KEY NOT NULL," +
	                       " PASSWORD TEXT NOT NULL)";
	    
			stmt.executeUpdate(query);
			stmt.close();
			con.close();
	      
		}catch(SQLException ex) {
			
			ex.printStackTrace();
		}
	}
	
	private boolean tableExists() {
		
		connect();
		try {
			Statement stmt = con.createStatement();
			String query = "SELECT name " +
							"FROM sqlite_master " +
							"WHERE type='table' " +
							"AND name='USERS';";
			
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next())
			{
				rs.close();
				stmt.close();
				con.close();
				return true;
			}
			
			rs.close();
			stmt.close();
			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	//Connects to database if it exists
	//Otherwise creates database
	private void connect() {
		
	    try {
	      Class.forName("org.sqlite.JDBC");
	      con = DriverManager.getConnection("jdbc:sqlite:users.db");
	    
	    } catch ( SQLException | ClassNotFoundException ex ) {
	      
	    	ex.printStackTrace();
	    }
	}
}