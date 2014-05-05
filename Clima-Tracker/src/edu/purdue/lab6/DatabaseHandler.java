package edu.purdue.lab6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{ 
	
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "WeatherManager.db";
	
	private static final String TABLE_WEATHER = "weather";
	
	//Datatable Columns names
	private static final String LOCATION_ID = "location";
	private static String DAY_ID = "day";
	private static final String TEMPERATURE_ID = "temperature";
	private static final String WEATHERICON_URL = "icon";
	private static final String WEATHER_DESC = "description";
	private static final String WINDSPEED_ID = "windspeed";
	private static final String WINDDIRECTION_ID = "winddirection";
	
	


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/* TABLE_WEATHER Example 
	 * LOCATION_ID | DAY_ID | TEMPERATURE_ID | WEATHERICON_URL | WEATHER_DESC | WINDSPEED_ID | WINDDIRECTION_ID 
	 * 47906 		05-05-14	66				http://			 Sunny			15				SWW
	 */
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_WEATHER_TABLE = "CREATE TABLE" + TABLE_WEATHER + "("
				+ LOCATION_ID + " TEXT," + DAY_ID + " TEXT,"
				+ TEMPERATURE_ID + " TEXT,"  +
				WEATHERICON_URL + " TEXT," + WEATHER_DESC + " TEXT," +
				WINDSPEED_ID + " TEXT,"+ WINDDIRECTION_ID + " TEXT," +")";
		db.execSQL(CREATE_WEATHER_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
		
		onCreate(db);
	}

}
