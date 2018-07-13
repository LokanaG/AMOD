package com.uber.amod.object;

import java.util.ArrayList;
import java.util.List;

public class AmodStat extends AmodObject{
Boolean success = null;
List<String> reasons = new ArrayList<String>();
String policy;

	public AmodStat()
	{
		_type=AmodType.type.STAT;
	}
	
	public void setSucess(Boolean success)
	{
		this.success=success;
	}
	
	public Boolean isSuccess()
	{
		return success;
	}
	
	public List<String> getReasons()
	{
		return reasons;
	}
	
	public void setReasons(List<String> reasons)
	{
		this.reasons=reasons;
	}
	
	public void addReason(String reason)
	{
		reasons.add(reason);
	}
	
	public String getPolicyRef()
	{
		return policy;
	}
	
	public void setPolicyRef(String policy)
	{
		this.policy=policy;
	}
		
}
