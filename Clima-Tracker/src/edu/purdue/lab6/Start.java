package edu.purdue.lab6;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.astuetz.PagerSlidingTabStrip;


public class Start extends FragmentActivity {

	Activity mAct;
    private final Handler handler = new Handler();
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private int currentColor = 0xFF547CC1;
    static DatabaseHandler database;
    static String zipCode = "0";

    SQLiteDatabase db; 
    
    private String url = "http://api.worldweatheronline.com/free/v1/weather.ashx?key=q7hjybgjdjh3t3qask3zfpmu&q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        mAct = this;
        
        database = new DatabaseHandler(this);
       
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);
        tabs.setIndicatorColor(currentColor);
        

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.add_location){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Enter a location");
            final EditText input = new EditText(this);
            input.setHint("Enter a zipcode or city");
            alert.setView(input);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    zipCode = input.getText().toString();
                    url+=zipCode+"&num_of_days=5&includelocation=yes&format=json";
                    getWeatherInfo();
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Canceled.
                }
            });

            alert.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }

    private Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            handler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            handler.removeCallbacks(what);
        }
    };

    public class MyPagerAdapter extends FragmentPagerAdapter{
        private final String[] TABS = {"Today", "5 Days"};

        public MyPagerAdapter(FragmentManager fm){
            super(fm);
        }

        public CharSequence getPageTitle(int position){
            return TABS[position];
        }

        @Override
        public int getCount() {
            return TABS.length;
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return Today.newInstance(position);
            }else{
                return Next5Days.newInstance(position);
            }
        }
    }
    
    public void getWeatherInfo(){
    	new JSON_Parse(this,getApplicationContext(),"GET",database).execute(url);
    }
}

