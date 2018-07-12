package com.uber.amod.object;

public class AmodRegex {

String regex_id;
String attribute;
String regex;

	
	public AmodRegex()
	{
		
	}
	
	public AmodRegex(String regex_id, String attribute, String regex)
	{
		this.regex_id=regex_id;
		this.attribute = attribute;
		this.regex = regex;
	}
	
	public void setAttribute(String attribute)
	{
		this.attribute=attribute;
	}
	
	public void setRegex(String regex)
	{
		this.regex=regex;
	}
	
	public void setRegexID(String regex_id)
	{
		this.regex_id=regex_id;
	}
	
	public String getAttribute()
	{
		return attribute;
	}
	
	public String getRegexID()
	{
		return regex_id;
	}
	
	
}
