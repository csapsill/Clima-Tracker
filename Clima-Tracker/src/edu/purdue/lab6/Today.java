package edu.purdue.lab6;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    TextView cityName;
    TextView temp;
    TextView wind;
    ImageView weatherIcon;
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
        
        
        cityName = (TextView) rootView.findViewById(R.id.cityName);
        
        //temp = (TextView)getView().findViewById(R.id.temp);
        
        //wind = (TextView)getView().findViewById(R.id.wind);
        
        //weatherIcon= (ImageView)getView().findViewById(R.id.dummy);
        
        String iconURL = "";
        if(!iconURL.equals("")){//download task
        	new DownloadImageTask(weatherIcon).execute(iconURL);
        }
        else{// default image
        	
        }
        // Inflate the layout for this fragment
        /*LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        FrameLayout fl = new FrameLayout(getActivity());
        fl.setLayoutParams(params);
        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                .getDisplayMetrics());

        TextView v = new TextView(getActivity());
        params.setMargins(margin, margin, margin, margin);
        v.setLayoutParams(params);
        v.setLayoutParams(params);
        v.setGravity(Gravity.CENTER);
        v.setText("Today");

        fl.addView(v);*/
        return rootView;
    }
    
    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap>{
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
}
