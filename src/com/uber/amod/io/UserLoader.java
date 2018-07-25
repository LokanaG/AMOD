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
import com.uber.amod.ui.AmodUserCLI;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class UserLoader {

	static StatefulRedisConnection<String, String> context = null;
	static String[] header  = null;
	static RedisCommands<String,String> syncCommands = null;
	public static void main(String[] args)
	{
		
		
		
		   //Map<String,String> ops = new AmodUserCLI(args).parse();
		Map<String,String> ops = new HashMap<String,String>();   
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
		   
		   context =  new RedisContext(password,host,Integer.parseInt(port)).getGonnection();
		   syncCommands  = context.sync();
		   readFile("FlexForce","/home/amod/FlexForce.csv","email_work");
//		   readFile(ops.get("appName"),ops.get("fileName"),ops.get("correlationKey"));
		
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
		//TODO - refactor to use object model
		String[] protoRecord  = parseCSVLine(line);
		Map<String,String> record = new HashMap<String,String>();
		System.out.println("Header length " + header.length + "  Protorecord length " + protoRecord.length);

			if (header != null)
		{
			//System.out.println("Header length " + header.length + "  Protorecord length " + protoRecord.length);
			for (int i = 0 ; i < (protoRecord.length) ; i++)
			{
				String protokey = appName + " : " + header[i];				
				System.out.println("Setting Key " + protokey);
			   record.put(protokey, protoRecord[i]);
			}
		}
		
		for (String key :record.keySet())
		{
			syncCommands.hset(record.get(appName + " : " +correlation), key, record.get(key));
		}
		
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
