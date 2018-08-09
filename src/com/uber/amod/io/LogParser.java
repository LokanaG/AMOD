package com.uber.amod.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.uber.amod.api.RedisContext;
import com.uber.amod.ui.AmodCLI;
import com.uber.amod.ui.AmodLogCLI;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class LogParser {
	static StatefulRedisConnection<String, String> context = null;
	static RedisCommands<String,String> syncCommands = null;

	   public static void main(String[] args){
			 
		  Map<String,String> ops = new AmodLogCLI(args).parse();
			String host = ops.get("host");
			   if (host == null)
			   {
				   host = "localhost";
			   }
			   String port = ops.get("port");
			   if (port == null)
			   {
				   port = "6379";
			   }
			   String password = ops.get("password");
			   if(password == null)
			   {
				   password = "";
			   }
		  
		  
		  
		   //context =  new RedisContext(password,host,Integer.parseInt(port)).getGonnection();
		   RedisContext factory = new RedisContext("","",0,"");
		   String redisConnect = "redis://" + host + "/1";
		   context = factory.connect(redisConnect);
		   syncCommands  = context.sync();
		  readLog(ops.get("logstream"), ops.get("tag"));
		  // readLog("/home/amod/vehicles.log", "vehicles");
	   }
	public static void readLog(String logFile, String tag)
	{
		
		   JSONParser parser = new JSONParser(); 
		   File file = new File(logFile);
		   FileReader filereader = null;
		   LinkedHashSet<String> users = new LinkedHashSet<String>();
		try {
			filereader = new FileReader(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		   BufferedReader b = new BufferedReader(filereader);
		   String line = "";
		   Double startTime = null;
		   Double endTime= null;
		   try {
			while ((line = b.readLine()) != null)
			   {	    	
				   parser = new JSONParser();
				   Object obj = null;
				try {
					obj = parser.parse(line);
				} catch (ParseException e) {
					//System.out.println(e);
					//System.out.println("parsing failed");
					
				}
				   JSONObject jsonObject = (JSONObject) obj;
				    if (jsonObject != null)
				    {
				   Double ts = new Double("0");
				   long tsLong = 0;
				 //  System.out.println("Class for ts : " + jsonObject.get("ts").getClass());
				  // System.out.println("Class for tsObj : " + ts.getClass());
				   if (jsonObject.get("ts").getClass().isInstance(ts))
				   {
				    ts = (Double) jsonObject.get("ts");
				   // System.out.println("ts " + ts);
				   }
				   else if (jsonObject.get("ts").getClass().isInstance(tsLong))
				   {
					   tsLong = (Long)(jsonObject.get("ts"));
					  // System.out.println("tsLong " + tsLong);
					   ts = Double.valueOf(tsLong);
				   }
				  // System.out.println("TS " + ts);
			       JSONObject msg = (JSONObject) jsonObject.get("msg");
			       String remoteuser = (String) msg.get("remote_user");
			       String appName = (String) msg.get("app");
			       String uri = (String) msg.get("uri");
			       if ((null == startTime) || (ts < startTime))
			       {
			          	startTime = ts;
			       }
			       if ((null == endTime) || (ts > endTime))
			       {
			           	endTime = ts;
			       }
			       //now we update Redis
			       if (remoteuser != null)
			       {
			    	   users.add(remoteuser);
			    	  // System.out.println(tag + " " + remoteuser + " " + jsonObject.get("ts").toString());
			           syncCommands.hset(tag, ts.toString(), remoteuser);	   
			          // System.out.println("startTime " + startTime + " endTime " + endTime + " uniqueUsers " + users.size());
			       }
			   }
			   }
			System.out.println("startTime " + startTime + " endTime " + endTime + " uniqueUsers " + users.size());
			for ( String user : users)
			{
			  syncCommands.hset(tag + "-stat", user, "");
			}
			syncCommands.hset(tag + "-stat", "startTime", startTime.toString());
			syncCommands.hset(tag + "-stat", "endTime", endTime.toString());
			   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		   

	}
	
}
