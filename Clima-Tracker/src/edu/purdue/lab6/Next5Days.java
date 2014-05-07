package edu.purdue.lab6;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Next5Days extends Fragment {
    private SimpleAdapter adapter;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
    ListView listview;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POSITION = "position";

    // TODO: Rename and change types of parameters

    private int position;


    // TODO: Rename and change types and number of parameters
    public static Next5Days newInstance(int position) {
        Next5Days fragment = new Next5Days();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }
    public Next5Days() {
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
        View rootView = inflater.inflate(R.layout.fragment_next_5_days,container,false);
        listview = (ListView) rootView.findViewById(android.R.id.list);

        /*add all data before adapter
         like this:
         HashMap<String,String> data = new HashMap<String, String>;
         item.put("date", string);
         item.put("description", string);
         item.put("high", string);
         item.put("low", string);
         list.add(data);
         */
        List<String[]> dateList = Start.database.getAllWeather();
        int i =0;
        while(i < dateList.size()){
        	String[] weatherData = dateList.get(i);
        	HashMap<String,String> item = new HashMap<String,String>();
        	item.put("date", weatherData[0]);
        	item.put("description",weatherData[3]);
        	item.put("high", "High: "+weatherData[2]+(char) 0x00B0);
        	item.put("low", "Low: "+weatherData[1]+(char) 0x00B0);
        	
        	list.add(item);
        	i++;        	
        }
        adapter = new SimpleAdapter(getActivity().getBaseContext(),list,R.layout.list_item,
            new String[] {"date","description","high","low"}, new int[] {R.id.date, R.id.desc, R.id.ht, R.id.lt});
        listview.setAdapter(adapter);


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
        v.setText("This Week");

        fl.addView(v);
        return fl;*/


        return rootView;
    }
}
