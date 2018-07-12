package com.uber.amod.api;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.uber.amod.object.AmodObject;
import com.uber.amod.object.AmodPolicy;
import com.uber.amod.object.AmodRegex;
import com.uber.amod.object.AmodType;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisContext {
public StatefulRedisConnection<String, String> context;
	
	public RedisContext(String password, String host, int port)
	{
		String redisConnect = "redis://" + password + "@" + host + ":" + port + "/";
		this.context=connect(redisConnect);
	}
	
	
	public StatefulRedisConnection<String, String> connect(String redisConnect)
	{
		RedisClient redisClient = RedisClient
				  .create("redisConnect");
				this.context = redisClient.connect();
		return context;
	}
	
	public AmodPolicy getPolicyByName(String policyName) {
		
		//Need to hydrate the object
		RedisAsyncCommands<String, String> asyncCommands = context.async();
	    AmodPolicy policy = null;
		try {
			policy = new AmodPolicy(asyncCommands.hgetall(policyName).get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return policy;
   }
	
	public AmodRegex getPolicyByID(String policyID)
	{
		return null;		
	}
	
	public void save(AmodObject _object)
	{
		//Need to flush the object
		RedisAsyncCommands<String, String> asyncCommands = context.async();
		//Let's just assume a policy object for now
		if (_object.getType().equals(AmodType.type.POLICY))
		{
			AmodPolicy policy = (AmodPolicy)_object;
			int i = 1;
			for (AmodRegex amodRegex : policy.getAmodRegexes())
			{
				asyncCommands.hset(policy.getPolicyName(), String.valueOf(i), amodRegex.getRegexID());
				i++;
			}
			
		}
	}

	
}
