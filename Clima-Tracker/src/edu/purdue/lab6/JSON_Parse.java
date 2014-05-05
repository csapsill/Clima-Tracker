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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
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
	
	ProgressDialog progDial;
	// constructor
	JSON_Parse(Activity act,List<NameValuePair> list,Context context, String method){
		this.mAct = act;
		this.list = list;
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
		progDial.show();
		
	}
	

	@Override
	protected JSONObject doInBackground(String... params) {
		//HttpResponse httpResponse = null;
		try{
			if(method == "POST"){
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(params[0]);
				httpPost.setEntity(new UrlEncodedFormEntity(list));

				HttpResponse httpResponse = httpClient.execute(httpPost);
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
			/*
			else if(method == "GET"){
				
				HttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(list, "utf-8");
				Dataurl+= "?" +paramString;
				HttpGet httpGet = new HttpGet(Dataurl);
				
				httpResponse = httpClient.execute(httpGet);
				StatusLine statusLine = httpResponse.getStatusLine();
				if()
				
				HttpEntity httpEntity = httpResponse.getEntity();
				inputStream = httpEntity.getContent();
			}*/
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
		
		
		
		progDial.dismiss();
	}
}
