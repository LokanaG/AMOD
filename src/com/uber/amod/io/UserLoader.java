package com.uber.amod.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.simple.parser.JSONParser;

import com.uber.amod.api.RedisContext;
import com.uber.amod.object.AmodRepo;
import com.uber.amod.ui.AmodUserCLI;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class UserLoader {

	static StatefulRedisConnection<String, String> context = null;
	static RedisContext redisContext = null;
	static String[] header  = null;
	static RedisCommands<String,String> syncCommands = null;

	
	public static void main(String[] args)
	{
		Map<String,String> ops = new AmodUserCLI(args).parse();  
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
	       
		   
		   redisContext =  new RedisContext(password,host,Integer.parseInt(port), "0");
	       context = redisContext.getGonnection();
		   syncCommands  = context.sync();
		   readFile(ops.get("appName"),ops.get("fileName"),ops.get("correlationKey"));
		
	}
	
	private static void readFile(String appName, String fileName, String correlation)
	{
		//let's strip the headers first
		File file = new File(fileName);
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		int headercount = 0;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				 if (headercount ==0) 
				 {
					 //then update the header
					 System.out.println("Processing Header " + line);
					 headercount++;
					 processHeader(line);
				 } else
				 {
					 //process a user record
					 System.out.println("Processing Record " + line);
					 processUser(line, correlation, appName);
				 }

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void processHeader(String line)
	{
		header = line.split(",");
	}
	
	private static void processUser(String line, String correlation, String appName)
	{
		String[] protoRecord  = parseCSVLine(line);
		Map<String,String> record = new HashMap<String,String>();
		if (header != null)
		  {
			for (int i = 0 ; i < (protoRecord.length) ; i++)
			   {
				 String protokey = appName + " : " + header[i];				
			     record.put(protokey, protoRecord[i]);
			   }
		    }
		System.out.println(record);
		AmodRepo userRecord = new AmodRepo();
		userRecord.setPermissions(record);
		userRecord.setName(record.get(appName + " : " +correlation));
		redisContext.save(userRecord);
		//for (String key :record.keySet())
		//{	
		//	syncCommands.hset(record.get(appName + " : " +correlation), key, record.get(key));
		//}
		
	}
	
    private static String[] parseCSVLine(String line) {
        // Create a pattern to match breaks
        Pattern p = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))");
        // Split input with the pattern
        String[] fields = p.split(line);
        for (int i = 0; i < fields.length; i++) {
            // Get rid of residual double quotes
            fields[i] = fields[i].replace("\"", "");
        }
        return fields;
    }
}
