package com.uber.amod.api;

import java.util.Map;

import com.uber.amod.object.AmodPolicy;
import com.uber.amod.object.AmodRegex;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisContext {

	public RedisContext(String password, String host, int port)
	{
		String redisConnect = "redis://" + password + "@" + host + ":" + port + "/";
	}
	public StatefulRedisConnection<String, String> context;
	
	public StatefulRedisConnection<String, String> connect(String redisConnect)
	{
		RedisClient redisClient = RedisClient
				  .create("redisConnect");
				this.context = redisClient.connect();
		return context;
	}
	
	public AmodPolicy getPolicyByName(String policyName) {
		
		
		RedisCommands<String, String> syncCommands = context.sync();
	    AmodPolicy policy = new AmodPolicy(syncCommands.hgetall("policyName"));
	     return policy;
   }
	
	public AmodRegex getPolicyByID(String policyID)
	{
		return null;
		
	}
}
