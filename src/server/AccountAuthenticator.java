package server;
import java.sql.*;

public class AccountAuthenticator {
	
	private Connection con;
	
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
	//Note: Will throw an exception if userName already exists so let me know if you 
	//guys want me to do something out of that
	public void addUser(String userName, String password)
	{
		connect();
		
		try {
			
			Statement stmt = con.createStatement();
			
			String query = "INSERT INTO USERS (USERNAME,PASSWORD) " +
	                   	   "VALUES ('" + userName + "', '" + password + "');"; 
			
			stmt.executeUpdate(query);
	   
			stmt.close();
			con.close();
			
		} catch(SQLException ex) {
			
			ex.printStackTrace();
		}
	}
	
	//Creates Users table if it doesn't exist
	//Can add more fields if needed
	public void createTable() {
			
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