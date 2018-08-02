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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.uber.amod.api.RedisContext;
import com.uber.amod.ui.AmodExportCLI;
import com.uber.amod.ui.AmodUserCLI;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanIterator;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

public class UserExporter {

	public static void main(String[] args) throws InterruptedException, ExecutionException  {
		
		Map<String,String> ops = new AmodExportCLI(args).parse();  
		   String host = ops.get("host");
		   if (host == null)
		   {
			   host = "localhost";
		   }
		   
		   Integer port = null;
		   if (ops.get("port") != null)
		   {
			   Integer.valueOf(ops.get("port"));
		   }
		   if (port == null)
		   {
			   port = 6379;
		   }
		   String password = ops.get("password");
		   if(password == null)
		   {
			   password = "";
		   }
		   String attributeOps = ops.get("attribute");
		   String fileName = ops.get("fileName");
		   String regex = ops.get("regex");
		   String logStream = ops.get("logStream");
		   
		LinkedHashSet<String> header = new LinkedHashSet<String>();
		header.add("user");
		int counter = 1;
		List<Map<String,String>> users = new ArrayList<Map<String,String>>();
		RedisContext context = new RedisContext(password, host, port, "0");
		StatefulRedisConnection<String, String> connection = context.getGonnection();
		RedisAsyncCommands<String, String> async = connection.async();
		RedisFuture<List<String>> exec = async.keys("*");
    
			try {
				for (String key : exec.get())
				{
					if (!key.contentEquals("key"))
					{
					Map<String,String> user = new HashMap<String,String>();
					//System.out.println(counter + " Processing : " + key);
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

			if (logStream!= null)
			{
				header.add(logStream);
			}
			
			
		//System.out.println("Header " + header.toArray());
		String headerLine = new String();
		for (String column : header)
		{
			if (headerLine.isEmpty())
			{
				headerLine = column;
			}
			else 
			{
			headerLine = headerLine + "|" +column;
			}
		}
		System.out.println("Processing with Header : ");
		System.out.println(headerLine);
		// now we print all the records
	    BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fileName));
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
	     
	    StatefulRedisConnection<String, String> logcontext = null;
		RedisCommands<String,String> syncCommands = null;
	    RedisContext factory = new RedisContext("","",0,"");
		   logcontext = factory.connect("redis://127.0.0.1/1");
		   syncCommands  = logcontext.sync();
		  
	    Map<String, String> simLog = syncCommands.hgetall(logStream + "-stat");
	    int records = 1;
	    System.out.println("value of simLog " + simLog);

		for (Map<String,String> user : users)
		{
			String userName = user.get("user");
			//if (simLog.isEmpty() || simLog.containsKey(userName))
			{
			
			String line = new String();
			int columncount = 0;
			Boolean include = false;
			for (String column : header)
			{
				String attribute = user.get(column);
				if (attribute != null && attribute.contains("|"))
				{
					attribute = attribute.replaceAll("\\|", "");
					System.out.println("Replacing pipe for " + column + " : " + attribute);
				}
				if (column.contains(attributeOps) && attribute != null)
				{ 
				  Pattern r = Pattern.compile(regex);
				  Matcher m = r.matcher(attribute);
				  if (m.find( )) 
				  {
					include = true;
			      }
				}
				if (column.contentEquals(logStream) && simLog.containsKey(userName))
				{
					attribute = "true";
				}
				if (column.contentEquals(logStream) && !simLog.containsKey(userName))
				{
					attribute = "false";
				}
				if (attribute == null || attribute.isEmpty())
				{
					attribute = "";
				}
				attribute = "\"" + attribute + "\"";
				if (!line.isEmpty())
				{
					attribute = "|" + attribute ;
				} 
				
				line = line + attribute;
				columncount++;
				
			}
			if (columncount != header.size())
			{
				System.out.println("Column Count : " + columncount + " Header Count : " + header.size());
			}
			try {
				if (include)
				{
				writer.write(line);
				records++;
				writer.newLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(line);
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Processing Finished " + records + " records written");
		
	}

}
