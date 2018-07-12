package com.uber.amod.object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmodRepo extends AmodObject{
	
String name;
Map<String,String> permissions = new HashMap<String,String>();

	public AmodRepo()
	{
		_type=AmodType.type.USERREPO;
	}

	public void setName(String name)
	{
		this.name=name;
	}
	
	public void setPermission(String attribute, String value)
	{
		permissions.put(attribute, value);
	}
	public void setPermissions(Map<String,String> permissions)
	{
		this.permissions=permissions;
	}
	public String getPermissionValue(String attribute)
	{
		return permissions.get(attribute);
	}
	public Map<String,String> getPermissions()
	{
		return permissions;
	}
	public String getName()
	{
		return name;
	}


	

	
	
	
}
