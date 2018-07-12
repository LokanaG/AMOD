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
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.uber.amod.api.RedisContext;
import com.uber.amod.ui.AmodCLI;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class LogParser {
	static StatefulRedisConnection<String, String> context = null;
	static RedisCommands<String,String> syncCommands = null;

	   public static void main(String[] args){
			 
		   Map<String,String> ops = new AmodCLI(args).parse();
		   
		   JSONParser parser = new JSONParser();
		   Map<String,Integer> stats = new HashMap<String,Integer>();
		   context = RedisContext.connect();
		   syncCommands  = context.sync();
		   readLog(ops.get("i"), ops.get("tag"));
	   }
	public static void readLog(String logFile, String tag)
	{
		
		   JSONParser parser = new JSONParser();   
		   File file = new File(logFile);
		   BufferedReader b = null;
		   String line = "";
		   Long startTime = null;
		   Long endTime= null;
		   try {
			while ((line = b.readLine()) != null)
			   {	    	
				   parser = new JSONParser();
				   Object obj = null;
				try {
					obj = parser.parse(line);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				   JSONObject jsonObject = (JSONObject) obj;
				   Long ts = (Long) jsonObject.get("ts");
			       JSONObject msg = (JSONObject) jsonObject.get("msg");
			       String remoteuser = (String) msg.get("remote_user");
			       String appName = (String) msg.get("app");
			       String uri = (String) msg.get("uri");
			       if ((startTime == null) || (ts < startTime))
			       {
			          	startTime = ts;
			       }
			       if ((endTime == null) || (ts > endTime))
			       {
			           	endTime = ts;
			       }
			       //now we update Redis
			       syncCommands.hset(tag, remoteuser, String.valueOf(ts));
			   }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
