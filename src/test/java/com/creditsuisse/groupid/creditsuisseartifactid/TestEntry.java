package com.creditsuisse.groupid.creditsuisseartifactid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;



public class TestEntry 
{
	 static String strEventId = null;
	 static long lngEventDuration;
	 static String strEventDuration = null;
	 static String strType = null;	
	 static String strHost = null;
	 static String strAlert = null;
	 static String st;
	 static int linecount=1;
	 static long timeDuration=0;
	 static long tempStartedTimeDuration=0;
	 static Connection con = null;
	 static Statement stmt = null;
	 static int result = 0;
	 static ResultSet resultSet=null;
		
	
public static void main(String args[]) throws IOException, ParseException, AclFormatException, SQLException
{
	
	Server server=new Server();
	startServer(server);
	//1. Take the path to logfile.txt as an input argument
	File file =  new File("logfile.txt");
	//1.1 Read in the file and store the contents in ONE String
	BufferedReader br=new BufferedReader(new FileReader(file));
    //Declare HashMap
	HashMap<Integer,CreateEntryObject> hm = new HashMap<Integer,CreateEntryObject>();
	HashMap<String,Object> hm1 = new HashMap<String,Object>();
	while((st=br.readLine())!=null)
	{
		//Prints each json line by line
		System.out.println(st);
		CreateEntryObject sample = new Gson().fromJson(st, CreateEntryObject.class);
		hm.put(linecount, sample);
		linecount=linecount+1;
	}
	br.close();
	
	 for(final Entry<Integer, CreateEntryObject> e:hm.entrySet())
	  {
		 if(hm1.containsKey(e.getValue().getId()))
		  {
			 List<HashMap> l1=(List<HashMap>) hm1.get(e.getValue().getId());
			 for (int i = 0; i < l1.size(); i++) 
			  {  
				  tempStartedTimeDuration=Long.valueOf((String) l1.get(i).get("Event duration"));
			  }
			 if(e.getValue().getState().equalsIgnoreCase("STARTED"))
			 {
				 timeDuration=tempStartedTimeDuration-Long.parseLong(e.getValue().getTimeStamp()); 
			 }
			 else
			 {
			 timeDuration=Long.parseLong(e.getValue().getTimeStamp())-tempStartedTimeDuration;
			 }  
			 Iterator iter = hm1.entrySet().iterator();
			  if(hm1.containsKey(e.getValue().getId()))
			  {
				  Map.Entry mEntry = (Map.Entry) iter.next();
				  l1.get(0).put("Event duration", timeDuration);
				  if(timeDuration>4)
				  {
					  l1.get(0).put("Alert", "true");
				 }
				  hm1.put(e.getValue().getId(), l1);
			  }
					 
		  }
		  else
		  {
			  hm1.put(e.getValue().getId(), Arrays.asList(new HashMap<String,Object>(){{
			  put("Event id", e.getValue().getId());
			  put("Event duration",e.getValue().getTimeStamp());
			  put("Type",e.getValue().getType());
			  put("Host",e.getValue().getHost());
			  put("Alert","false");
			  put("State",e.getValue().getState());
			  }}));
			  }
	  }
	 for(Entry<String,Object> e:hm1.entrySet())
	 {
		List<HashMap> l1 = (List<HashMap>) e.getValue();
		strEventId=(String) l1.get(0).get("Event id");
		lngEventDuration= (long) l1.get(0).get("Event duration");
		strType=(String) l1.get(0).get("Type");
		strHost=(String) l1.get(0).get("Host");
		strAlert=(String) l1.get(0).get("Alert");
		strEventDuration=Long.toString(lngEventDuration);
		//System.out.println(strEventId + " "+strEventDuration+" "+strType+" "+strHost+" "+strAlert);
		
		//Code to insert the data to DB:
		 insertDataToDB(strEventId,strEventDuration,strType,strHost,strAlert);
	}
	 queryTheDataFromDB();
	 stopServer(server);
	 
	 
}
public static void stopServer(Server server) throws SQLException {
	// TODO Auto-generated method stub
	con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/accounts;filepath=file:C:/files/mydatabase", "SA", "");
	stmt = con.createStatement(); 
	result=stmt.executeUpdate("DROP TABLE eventDetails_tbl");
	server.shutdownCatalogs(1);
}
public static void queryTheDataFromDB() throws IOException, AclFormatException, SQLException
{
	try {
	   con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/accounts;filepath=file:C:/files/mydatabase", "SA", "");
       stmt = con.createStatement();
       resultSet = stmt.executeQuery("SELECT EventId, EventDuration, Type,Host,Alert FROM eventDetails_tbl");
       while(resultSet.next()){
           System.out.println(resultSet.getString("EventId")+" | "+
              resultSet.getString("EventDuration")+" | "+
              resultSet.getString("Type")+" | "+
              resultSet.getString("Host")+" | "+
              resultSet.getString("Alert"));
        }
    } catch (Exception e) {
       e.printStackTrace(System.out);
    }
 }

public static void insertDataToDB(String EventId,String EventDuration, String Type,String Host,String Alert) throws IOException, AclFormatException, SQLException
{
	 try { 
        
        con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/accounts;filepath=file:C:/files/mydatabase", "SA", "");
        stmt = con.createStatement(); 
        result = stmt.executeUpdate("INSERT INTO eventDetails_tbl VALUES ('"+EventId+"','"+EventDuration+"', '"+Type+"','"+Host+"','"+Alert+"')");
        con.commit();
        System.out.println(result+" rows effected"); 
        System.out.println("Rows inserted successfully"); 
     }catch (Exception e) { 
        e.printStackTrace(System.out); 
     } 
     
  } 
public static void startServer(Server server) throws IOException, AclFormatException, SQLException 
{
	// TODO Auto-generated method stub
	HsqlProperties p = new HsqlProperties();
	p.setProperty("server.database.0","file:/opt/db/accounts");
	 p.setProperty("server.dbname.0","accounts");
	 // set up the rest of properties
	 
	// alternative to the above is
	 //server = new Server();
	 server.setProperties(p);
	 server.setLogWriter(null); // can use custom writer
	 server.setErrWriter(null); // can use custom writer
	 server.start();
	 
	 
	 try {
	 //Allowing a Connection to Open or Create a Database
		 con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/accounts;filepath=file:C:/files/mydatabase", "SA", "");
		 stmt = con.createStatement();
     //Below line creates table
	    result = stmt.executeUpdate("CREATE TABLE eventDetails_tbl (EventId VARCHAR(50) NOT NULL, EventDuration INT NOT NULL, Type VARCHAR(20) NOT NULL,Host VARCHAR(20)NOT NULL,Alert VARCHAR(6) NOT NULL,PRIMARY KEY (EventId));");
	    System.out.println("Table Created Successfully");
	 }
	  catch (Exception e) {
	        e.printStackTrace(System.out);
	     }
	 }
}
