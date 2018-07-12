package com.uber.amod.elk;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;


public class ELKWriter {

	private TransportClient client =null;
	public void update(String user, String result)
	{
		XContentBuilder builder = null;
		try {
			builder = XContentFactory.jsonBuilder()
					.startObject()
			        .field("user", user)
			        .field("postDate", new Date())
			        .field("result", result)
			    .endObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String json = Strings.toString(builder);
		IndexResponse response = connect().prepareIndex("amod","authz")
				.setSource(json, XContentType.JSON)
				.get();
		
	}
	@SuppressWarnings("resource")
	private TransportClient connect()
	{
		try {
			client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new TransportAddress(InetAddress.getByName("host1"), 9300));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return client;
	}
	public void terminate()
	{
		client.close();
	}
	
	public void updateAll(Map<String,String> objects)
	{
		//assuming the set order in the mapping is guaranteed
		//if not we need to use a list with an embedded K/V pair
		for (String user :objects.keySet())
		{
			String result  = objects.get(user);
			update(user,result);
		}
	}
}
	

