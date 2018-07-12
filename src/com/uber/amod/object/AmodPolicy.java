package com.uber.amod.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class AmodPolicy extends AmodObject{
String policy;
List<AmodRegex> amodRegexes = new ArrayList<AmodRegex>();
int order;
String regex_id;
	
	public AmodPolicy(Map<String, String> rawPolicy)
	{
		new AmodPolicy();
		policy = rawPolicy.get("policy");
		int i = 1;
		while (rawPolicy.get(String.valueOf(i)) != null)
		{
			AmodRegex amodRegex = new AmodRegex();
			amodRegex.setRegexID(rawPolicy.get(String.valueOf(i)));
			addAmodRegex(amodRegex);
		}
	}
	
	public AmodPolicy()
	{
		_type = AmodType.type.POLICY;
	}
	
	public String getPolicyName()
	{
		return policy;
	}
	public void addAmodRegex(AmodRegex amodRegex)
	{
		amodRegexes.add(amodRegex);
	}
	
	public void setAmodRegex(List<AmodRegex> amodRegexes)
	{
		this.amodRegexes=amodRegexes;
	}
	
	public void remAmodRegex(AmodRegex amodRegex)
	{
		
	}
	
	public void remAmodRegex(String regex_id)
	{
		
	}
	
	public List<AmodRegex> getAmodRegexes()
	{
		return amodRegexes;
	}
	
}
