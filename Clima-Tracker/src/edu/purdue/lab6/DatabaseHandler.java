package edu.purdue.lab6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{ 
	
	static final int DATABASE_VERSION = 1;
	
	static final String DATABASE_NAME = "WeatherManager";
	
	static final String TABLE_WEATHER = "weather";
	
	//Datatable Columns names
	static final String location = "location";
	static final String day = "day";
	static final String temperature = "temperature";
	static final String weatherIconURL = "icon";
	static final String weatherDesc = "description";
	static final String windSpeed = "windspeed";
	static final String windDirection = "winddirection";
	
	


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/* TABLE_WEATHER Example 
	 * LOCATION_ID | DAY_ID | TEMPERATURE_ID | WEATHERICON_URL | WEATHER_DESC | WINDSPEED_ID | WINDDIRECTION_ID 
	 * 47906 		05-05-14	66				http://			 Sunny			15				SWW
	 */
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE_WEATHER + "("
				+ location + " TEXT," +
				day + " TEXT,"+
				temperature + " TEXT," +
				weatherIconURL + " TEXT,"+
				weatherDesc + " TEXT," +
				windSpeed + " TEXT," +
				windDirection + " TEXT" + ")";
		db.execSQL(CREATE_WEATHER_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
		
		onCreate(db);
	}

}
