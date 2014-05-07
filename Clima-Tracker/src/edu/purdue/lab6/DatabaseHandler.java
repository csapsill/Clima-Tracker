package edu.purdue.lab6;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{ 
	
	static final int DATABASE_VERSION = 1;
	
	/* DataBase Name*/
	static final String DATABASE_NAME = "WeatherManager";
	
	/* Table Names */
	static final String TABLE_WEATHER = "weather";	
	static final String LOCATION_TABLE = "location";
	
	// Location Table columns
	static final String zipCode = "zipcode";
	static final String cityName = "cityname";
	
	//Weather Table Columns
	static final String day = "day";
	static final String tempLow = "templow";
	static final String tempHigh = "temphigh";
	static final String weatherIconURL = "icon";
	static final String weatherDesc = "description";
	static final String windSpeed = "windspeed";
	static final String windDirection = "winddirection";
	
	static final String CREATE_LOCATION_TABLE = "CREATE TABLE "
			+ LOCATION_TABLE + "(" + zipCode + " INTEGER PRIMARY KEY,"
			+ cityName + " TEXT" + ")";
	
	static final String CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE_WEATHER + "(" +
			day + " TEXT,"+
			tempLow+ " TEXT," +
			tempHigh + " TEXT," +
			weatherIconURL + " TEXT,"+
			weatherDesc + " TEXT," +
			windSpeed + " TEXT," +
			windDirection + " TEXT" + ")";
	
	


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/* TABLE_WEATHER Example 
	 * LOCATION_ID | DAY_ID | TEMPERATURE_ID | WEATHERICON_URL | WEATHER_DESC | WINDSPEED_ID | WINDDIRECTION_ID 
	 * 47906 		05-05-14	66				http://			 Sunny			15				SWW
	 */
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//create the required tables
		db.execSQL(CREATE_LOCATION_TABLE);
		db.execSQL(CREATE_WEATHER_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
		
		onCreate(db);
	}
	
	public long createLocationTable(String[] values){
		if(values.length != 2){
			Log.e("Invalid location data", "The length of values was not correct");
		}
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(zipCode, values[0]);
		cv.put(cityName, values[1]);
		
		
		//insert row
		long location_id = db.insert(LOCATION_TABLE, null, cv);
		
		return location_id;
	}
	
	
	public String getLocationName(long location_id){
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery= "SELECT * FROM " + LOCATION_TABLE + " WHERE "
				+ zipCode + " = " + location_id;
        String locationName = "";
		Cursor c = db.rawQuery(selectQuery, null);
		
		if ( c != null && c.moveToFirst()){
            locationName = c.getString(c.getColumnIndex(cityName));

		}
		c.close();

		return locationName;
		
	}
	
	public List<String> getAllLocationNames(){
		List<String> cityNames = new ArrayList<String>();
		String selectQuery = "SELECT * FROM " + LOCATION_TABLE;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery,null);
		
		if(c.moveToFirst()){
			do {
				String name;
				name = c.getString(c.getColumnIndex(cityName));
				
				cityNames.add(name);
			} while(c.moveToNext());
		}
		
		return cityNames;
	}
	
	public void createWeather(String[] data){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(day, data[0]);
		cv.put(tempLow, data[1]);
		cv.put(tempHigh,data[2]);
		cv.put(weatherIconURL, data[3]);
		cv.put(weatherDesc,data[4]);
		cv.put(windSpeed, data[5]);
		cv.put(windDirection, data[6]);
		
		db.insert(TABLE_WEATHER,null,cv);
		
	}
	
	public String[] getTodayWeather(String today){
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery= "SELECT * FROM " + TABLE_WEATHER + " WHERE "
				+ day + " = " + "?";
		
		Cursor c = db.rawQuery(selectQuery, new String[] {today});
		System.out.println(c.getCount());
		String[] weatherData = new String[6];
		if(c != null && c.getCount() > 0){
			c.moveToFirst();
			weatherData[0] = c.getString(c.getColumnIndex(tempLow));
			weatherData[1] = c.getString(c.getColumnIndex(tempHigh));
			weatherData[2] = c.getString(c.getColumnIndex(weatherIconURL));
			weatherData[3] = c.getString(c.getColumnIndex(weatherDesc));
			weatherData[4] = c.getString(c.getColumnIndex(windSpeed));
			weatherData[5] = c.getString(c.getColumnIndex(windDirection));
		}
		
		
		return weatherData;
	}

}