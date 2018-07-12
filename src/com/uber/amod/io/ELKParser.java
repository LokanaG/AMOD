package com.uber.amod.io;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import com.uber.amod.ui.AmodCLI;

import org.json.simple.parser.JSONParser;

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


public class ELKParser {

	   public static void main(String[] args){
		 
		   Map<String,String> ops = new AmodCLI(args).parse();
		   
		   JSONParser parser = new JSONParser();
		   Map<String,Integer> stats = new HashMap<String,Integer>();
		   
		   File file = new File(ops.get("i"));
		   int lines = 0;
		   int uniqueusers = 0;
		   BufferedReader b = null;
		try {
			b = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		   String line = "";
		   try {
			while ((line = b.readLine()) != null)
			   {
				lines++;
			    try {
			    	parser = new JSONParser();
			        Object obj = parser.parse(line);

			        JSONObject jsonObject = (JSONObject) obj;
			       // System.out.println(jsonObject);
                    JSONObject msg = (JSONObject) jsonObject.get("msg");
                    String remoteuser = (String) msg.get("remote_user");
                    String appName = (String) msg.get("app");
                    String eventName = remoteuser + "," + appName;
                   // System.out.println("Remote User "+ remoteuser);
                    if (stats.get(eventName) != null)
                    {
                    	//System.out.println("Found Duplicate Event : " + eventName);
                    	int hits = stats.get(eventName);
                    	hits++;
                    	stats.put(eventName, hits);
                    }
                    else 
                    {
                    	stats.put(eventName, 1);
                    	
                    } 

			        // loop array
			        //JSONArray msg = (JSONArray) jsonObject.get("msg");
			        //Iterator<String> iterator = msg.iterator();
			        //while (iterator.hasNext()) {
			        //    System.out.println(iterator.next());
			        //}

			    } catch (ParseException e) {
			        e.printStackTrace();
			    }
			   }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   FileWriter writer = null;
           try {
			writer = new FileWriter(ops.get("f"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           PrintWriter printer = new PrintWriter(writer);
		   //Now we print the summary stats for each user
		   Set<String> users = stats.keySet();
		   Iterator i = users.iterator();
		   while (i.hasNext())
		   {
			   String user = (String)i.next();
			   uniqueusers++;
			   printer.println(user + "," + stats.get(user));
		   }
		   printer.close();
		   try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   System.out.println("Processed input file : "+ ops.get("i"));
		   System.out.println("Created output file : "+ ops.get("f"));
		   System.out.println("Log entries processed : "+ lines);
		   System.out.println("Users discovered : "+uniqueusers);
	    }
	   
}

