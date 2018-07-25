package com.uber.amod.api;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.uber.amod.object.AmodObject;
import com.uber.amod.object.AmodPolicy;
import com.uber.amod.object.AmodRegex;
import com.uber.amod.object.AmodRepo;
import com.uber.amod.object.AmodType;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisContext {
public StatefulRedisConnection<String, String> context = null;
	
	public RedisContext(String password, String host, int port)
	{
		//String redisConnect = "redis://" + password + "@" + host + ":" + port + "/";
		
		this.context=connect("redis://127.0.0.1/0");
	}
	
	
	public StatefulRedisConnection<String,String> getGonnection()
	{
		return context;
	}
	
	public StatefulRedisConnection<String, String> connect(String redisConnect)
	{
		System.out.println("Redis Connect String : " + redisConnect);
		RedisClient redisClient = RedisClient
				  .create(redisConnect);
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
				save(amodRegex);
				i++;
			}
			
		}
		if (_object.getType().equals(AmodType.type.REGEX))
		{
			AmodRegex regex = (AmodRegex)_object;
			asyncCommands.hset(regex.getRegexID(), regex.getAttribute(), regex.getRegexString());
		}
		if (_object.getType().equals(AmodType.type.USERREPO))
		{
			AmodRepo repo = (AmodRepo)_object;
			for (String key : repo.getPermissions().keySet())
			{
				asyncCommands.hset(repo.getName(), key, repo.getPermissionValue(key));
			}
		}
	}

	
}
