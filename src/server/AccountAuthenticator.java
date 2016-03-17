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
		
		addUser("test","test","test","test");
		addUser("user","user","user","user");
	}
	
	//Checks if userName/password combo exists in database
	public boolean isValidLogin(String userName, String password)
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
	
	public String getProfileInfo(String userName)
	{
		connect();
		
		try {
			Statement stmt = con.createStatement();
			String query = "SELECT * FROM USERS " +
						   "WHERE USERNAME = '" + userName + "'";
			ResultSet rs = stmt.executeQuery(query);			
			
			if(rs.next())
			{	
				//TODO: might not be merged properly
				String profileInfo = rs.getString(1) + ", " 
					 + rs.getString(3) + ", "
					 + rs.getString(4) + ", " 
					 + rs.getInt(5) + ", "
					 + rs.getInt(6);
				
				rs.close();
				stmt.close();
				con.close();
				
				return profileInfo;
			}
			
			rs.close();
			stmt.close();
			con.close();
		
		} catch(SQLException ex) {
			
			ex.printStackTrace();
		} 
		
		return "User Not Found";

	}
	
	//Checks just if user exists
	public boolean userExists(String userName)
	{
		connect();
		
		try {
			Statement stmt = con.createStatement();
			String query = "SELECT * FROM USERS " +
						   "WHERE USERNAME = '" + userName + "' ";
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
	public boolean addUser(String userName, String password, String gender, String country)
	{
		if(userExists(userName))
		{
			return false;
		}
		
		connect();
		
		try {
			
			Statement stmt = con.createStatement();
			
			String query = "INSERT INTO USERS (USERNAME,PASSWORD,GENDER,COUNTRY) " +
	                   	   "VALUES ('" + userName + "', '" + password + "', '" + gender + "', '" + country + "');";
			
			stmt.executeUpdate(query);
	   
			stmt.close();
			con.close();
			
			return true;
			
		} catch(SQLException ex) {
			
			return false;
		}
	}
	
	public void addWin(String userName)
	{
		connect();
		
		try {
			Statement stmt = con.createStatement();
			String query = "UPDATE users " +
					   "SET WINS = WINS + 1 " +
					   "WHERE USERNAME = '" + userName + "'";
			stmt.executeQuery(query);
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}
	
	public void addLoss(String userName)
	{
		connect();
		
		try {
			Statement stmt = con.createStatement();
			String query = "UPDATE users " +
					   "SET LOSSES = LOSSES + 1 " +
					   "WHERE USERNAME = '" + userName + "'";
			stmt.executeQuery(query);
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
	                       " PASSWORD TEXT NOT NULL," +
	                       " GENDER TEXT NOT NULL," +
	                       " COUNTRY TEXT NOT NULL," +
	                       " WINS INTEGER DEFAULT 0," +
	                       " LOSSES INTEGER DEFAULT 0)";
	    
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