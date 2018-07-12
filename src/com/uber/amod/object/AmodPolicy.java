package com.uber.amod.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class AmodPolicy {
String policy;
List<AmodRegex> amodRegexes = new ArrayList<AmodRegex>();
int order;
String regex_id;
	
	public AmodPolicy(Map<String,String> policyPrototype)
	{
		policy = policyPrototype.get("policy");
		int i = 1;
		while (policyPrototype.get(String.valueOf(i)) != null)
		{
			AmodRegex amodRegex = new AmodRegex();
			
		}
	}
	
	public AmodPolicy()
	{
		
	}
	
	public void addAmodRegex(AmodRegex amodRegex)
	{
		
	}
	
	public void setAmodRegex(List<AmodRegex> amodRegexes)
	{
		
	}
	
	public void remAmodRegex(AmodRegex amodRegex)
	{
		
	}
	
	public void remAmodRegex(String regex_id)
	{
		
	}
	
}
