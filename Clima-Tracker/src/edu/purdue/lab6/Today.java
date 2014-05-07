package edu.purdue.lab6;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;



public class Today extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POSITION = "position";

    static TextView cityName;
    static TextView temp;
    static TextView wind;
    static TextView hightemp;
    static TextView lowtemp;
    static TextView description;
    static TextView humidity;
    static ImageView weatherIcon;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static String dateString;
    // TODO: Rename and change types of parameters

    private int position;


    // TODO: Rename and change types and number of parameters
    public static Today newInstance(int position) {
        Today fragment = new Today();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }
    public Today() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_today,container,false);
        
        Date date = new Date();
        
        dateString = (dateFormat.format(date));
        long zip = Long.parseLong(Start.zipCode);
        System.out.println(zip);

        cityName = (TextView) rootView.findViewById(R.id.cityName);
        String city = Start.database.getFirstName();
        cityName.setText(city);
       
        String[] weatherData = Start.database.getTodayWeather(dateString);
        temp = (TextView) rootView.findViewById(R.id.temp);
        temp.setText(weatherData[1]+"\u2109");
        hightemp = (TextView) rootView.findViewById(R.id.htemp);
        hightemp.setText("High: "+weatherData[1]+"\u2109");
        lowtemp = (TextView) rootView.findViewById(R.id.ltemp);
        lowtemp.setText("Low: "+weatherData[0]+"\u2109");
        description = (TextView) rootView.findViewById(R.id.description);
        description.setText(weatherData[3]);
        //humidity = (TextView) rootView.findViewById(R.id.humidity);
        wind = (TextView) rootView.findViewById(R.id.wind);
        wind.setText("Wind: "+weatherData[4] + " mph " + weatherData[5]);

        weatherIcon = (ImageView) rootView.findViewById(R.id.image);
        String iconURL = weatherData[2];
        if(weatherData[2] != null){//download task
        	loadImage(weatherData[2]);
        	//new DownloadImageTask(weatherIcon).execute(iconURL);
        }
        else{// default image
        	
        }
        return rootView;
    }
    
    private static class DownloadImageTask extends AsyncTask<String,Void,Bitmap>{
    	ImageView bmImage;
    	
    	public DownloadImageTask(ImageView bmImage){
    		this.bmImage = bmImage;
    	}
    	
		@Override
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try{
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			}
			catch(Exception e){
				Log.e("Error writting Bitmap...", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}
		protected void onPostExecute(Bitmap result){
			bmImage.setImageBitmap(result);
		}
    	
    }
    
    public static void updateUI(){

        long zip = Long.parseLong(Start.zipCode);
        System.out.println(zip);
        String city = Start.database.getLocationName(zip);

        cityName.setText(city);
       
        String[] weatherData = Start.database.getTodayWeather(dateString);
       
        temp.setText(weatherData[1]+"\u2109");
        
        hightemp.setText("High: "+weatherData[1]+"\u2109");
        
        lowtemp.setText("Low: "+weatherData[0]+"\u2109");       
        description.setText(weatherData[3]);

        wind.setText("Wind: "+weatherData[4] + " mph " + weatherData[5]);
        
        loadImage(weatherData[2]);




    }
    
    public static void loadImage(String urlIcon){
    	   String iconURL;
           if(urlIcon != null){//download task
           	new DownloadImageTask(weatherIcon).execute(urlIcon);
           }
           else{// default image
           	
           }
    }
}
