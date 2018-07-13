package com.uber.amod.simulator;

import java.util.Map;

import com.uber.amod.api.RedisContext;
import com.uber.amod.object.AmodRepo;
import com.uber.amod.object.AmodStat;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class PolicyEngine {
RedisContext context;

	public PolicyEngine(RedisContext context)
	{
		this.context=context;
	}
	
	public AmodStat evaluate(AmodRepo user)
	{
		
		return new AmodStat();
	}

	

	
}
