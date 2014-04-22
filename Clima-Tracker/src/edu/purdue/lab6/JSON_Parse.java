package edu.purdue.lab6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

public class JSON_Parse {
	
	
	String HttpURL;
	JSONObject jObject;
	
	// constructor
	JSON_Parse(String url){
		HttpURL = url;
	}
	
	public JSONObject parse(){
		DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
		HttpPost httppost = new HttpPost(HttpURL);
		
		httppost.setHeader("Content-type","application/json");
		
		InputStream inputStream = null;
		String result = null;
		
		try{
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			
			inputStream = entity.getContent();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"),8);
			StringBuilder sb = new StringBuilder();
			
			String line = null;
			while((line = reader.readLine()) != null){
				sb.append(line + "\n");
			}
			result = sb.toString();
		} catch(Exception e){
			
		}
		finally {
			try{
				if(inputStream != null) {
					inputStream.close();
				}
			}catch (Exception e){
				
			}
		}
		
		
		try {
			jObject = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jObject;
				
	}

}
