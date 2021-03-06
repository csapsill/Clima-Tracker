package edu.purdue.lab6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class JSON_Parse extends AsyncTask<String,String,JSONObject>{
	
	static InputStream inputStream;
	static JSONObject jObj = null;
	static String json ="";
	Activity mAct;
	Context mContext;
	List<NameValuePair> list;
	String method;
	JSONObject jObject;
	
	DatabaseHandler db;
	
	ProgressDialog progDial;
	// constructor
	JSON_Parse(Activity act,Context context, String method, DatabaseHandler db){
		this.mAct = act;
		//this.list = list;
		this.db = db;
		this.method = method;
		this.mContext = context;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progDial = new ProgressDialog(mContext);
		progDial.setIndeterminate(false);
		progDial.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDial.setCancelable(true);
		progDial = ProgressDialog.show(mAct,"","Loading...", true);
		
	}
	

	@Override
	protected JSONObject doInBackground(String... params) {
		//HttpResponse httpResponse = null;
		HttpResponse httpResponse;
		try{
			if(method == "POST"){
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(params[0]);
				httpPost.setEntity(new UrlEncodedFormEntity(list));

				httpResponse = httpClient.execute(httpPost);
				StatusLine statusLine = httpResponse.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if(statusCode == 200){
					HttpEntity httpEntity = httpResponse.getEntity();
					inputStream = httpEntity.getContent();
				}
				else{
					Log.e("LOG", "Failed to download result..");
				}

				Header contentencoding = httpResponse.getFirstHeader("Content-Encoding");
				if(contentencoding != null && contentencoding.getValue().equalsIgnoreCase("gzip")){
					inputStream = new GZIPInputStream(inputStream);
				}

				BufferedReader mBufferedReader = new BufferedReader( new InputStreamReader(inputStream, "iso-8859-1"),8);
				StringBuilder stringBuilder = new StringBuilder();
				String line = null;
				while((line = mBufferedReader.readLine()) != null ){
					stringBuilder.append(line +"\n");
				}
				inputStream.close();
				json = stringBuilder.toString();

				jObj = new JSONObject(json);

			}
			else if(method == "GET"){
				
				HttpClient httpClient = new DefaultHttpClient();
				//String paramString = URLEncodedUtils.format(list, "utf-8");
				//params[0]+= "?" +paramString;
				HttpGet httpGet = new HttpGet(params[0]);
				
				httpResponse = httpClient.execute(httpGet);
				StatusLine statusLine = httpResponse.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if(statusCode == 200){
				
					HttpEntity httpEntity = httpResponse.getEntity();
					inputStream = httpEntity.getContent();
				}
				Header contentencoding = httpResponse.getFirstHeader("Content-Encoding");
				if(contentencoding != null && contentencoding.getValue().equalsIgnoreCase("gzip")){
					inputStream = new GZIPInputStream(inputStream);
				}

				BufferedReader mBufferedReader = new BufferedReader( new InputStreamReader(inputStream, "iso-8859-1"),8);
				StringBuilder stringBuilder = new StringBuilder();
				String line = null;
				while((line = mBufferedReader.readLine()) != null ){
					stringBuilder.append(line +"\n");
				}
				inputStream.close();
				json = stringBuilder.toString();

				jObj = new JSONObject(json);
			}
			else{
				Log.e("Request Alert","Wrong Method Name");
			}
		}
		catch(UnsupportedEncodingException e){
			Log.e("UnsupportEncodingException", e.getMessage().toString());
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocalException", e.getMessage().toString());
		} catch (IOException e) {
			Log.e("IOException",e.getMessage().toString());
		} catch (JSONException e) {
			Log.e("JSONException", e.getMessage().toString());
		}

		return jObj;

	}
	
	@Override
	protected void onPostExecute(JSONObject obj){
		ContentValues cv = new ContentValues();
		String[] locationData = new String[2];
		String[] weatherData = new String[8];
		try {
			JSONObject jData = obj.getJSONObject("data");
			
			/* get location*/
			JSONArray jRegion = jData.getJSONArray("request");
			for(int n = 0; n < jRegion.length();n++){
				JSONObject jZip = jRegion.getJSONObject(n);
				
				String zip = jZip.getString("query");
				locationData[0] = zip;
			}
			
			/* Get City Name*/
			JSONArray jArea = jData.getJSONArray("nearest_area");
			for(int c = 0; c < jArea.length(); c++){
				JSONObject elem = jArea.getJSONObject(c);
				JSONArray cityName = elem.getJSONArray("areaName");
				for(int b = 0; b < cityName.length();b++){
					JSONObject jCity = cityName.getJSONObject(b);
					String city = jCity.getString("value");
					locationData[1] = city;
				}
			}
			
			db.createLocationTable(locationData);
			
			/* Get weather information for a day */
			JSONArray jArray = jData.getJSONArray("weather");
			for(int i = 0; i< jArray.length();i++){
				JSONObject oneObject = jArray.getJSONObject(i);
				
				String date = oneObject.getString("date");
				System.out.println(date);
				weatherData[0] = date;
				String tempLow = oneObject.getString("tempMinF");
				String tempHigh = oneObject.getString("tempMaxF");
				weatherData[1] = tempLow;
				weatherData[2] = tempHigh;
				/* Get Weather icon URLs*/
				JSONArray jURL = oneObject.getJSONArray("weatherIconUrl");
				for(int k = 0; k< jURL.length(); k++ ){
					JSONObject urlObject = jURL.getJSONObject(k);
					String weatherImageUrl = urlObject.getString("value");
					weatherData[3] = weatherImageUrl;
				}
				/* Get Weather Description*/
				JSONArray jDesc = oneObject.getJSONArray("weatherDesc");
				for(int j = 0; j < jDesc.length(); j++){
					JSONObject descObject = jDesc.getJSONObject(j);
					
					String weatherDesc = descObject.getString("value");		
					weatherData[4] = weatherDesc;
				}
				
				String windDirection = oneObject.getString("winddirection");
				String windSpeed = oneObject.getString("windspeedMiles");
				weatherData[5] = windSpeed;
				weatherData[6] = windDirection;
				
				db.createWeather(weatherData);
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Today.updateUI();
		Next5Days list = new Next5Days();
		list.updateList();
		progDial.dismiss();
	}
}