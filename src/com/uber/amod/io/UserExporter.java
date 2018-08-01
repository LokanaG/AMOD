package com.uber.amod.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;

import com.uber.amod.api.RedisContext;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanIterator;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

public class UserExporter {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		
		LinkedHashSet<String> header = new LinkedHashSet<String>();
		header.add("user");
		int counter = 1;
		List<Map<String,String>> users = new ArrayList<Map<String,String>>();
		RedisContext context = new RedisContext("", "localhost", 0);
		StatefulRedisConnection<String, String> connection = context.getGonnection();
		RedisAsyncCommands<String, String> async = connection.async();
		RedisFuture<List<String>> exec = async.keys("*");
    
			try {
				for (String key : exec.get())
				{
					if (!key.contentEquals("key"))
					{
					Map<String,String> user = new HashMap<String,String>();
					System.out.println(counter + " Processing : " + key);
					Map<String, String> values = connection.sync().hgetall(key);
					user.put("user", key);
					for (String value :values.keySet())
					{
				     user.put(value,  values.get(value));
					header.add(value);
					}
					users.add(user);
					}
					counter++;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			
			
		//System.out.println("Header " + header.toArray());
		String headerLine = new String();
		for (String column : header)
		{
			headerLine = headerLine + "|" +column;
		}
		System.out.println("Processing with Header : ");
		System.out.println(headerLine);
		// now we print all the records
	    BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("/home/amod/users.csv"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			writer.write(headerLine);
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	  
		for (Map<String,String> user : users)
		{
			String line = new String();
			Boolean include = false;
			for (String column : header)
			{
				String attribute = user.get(column);
				if (column.contains("AD :") && attribute != null)
				{
					include = true;
				}
				if (attribute == null)
				{
					attribute = "";
				}
				line = line + "|" +attribute;
			}
			try {
				if (include)
				{
				writer.write(line);
				writer.newLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(line);
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Processing Finished");
		
	}

}
