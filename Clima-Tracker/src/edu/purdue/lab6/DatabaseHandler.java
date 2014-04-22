package edu.purdue.lab6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "WeatherManager";
	
	private static final String TABLE_WEATHER = "weather";
	
	//Datatable Columns names
	private static final String LOCATION_ID = "location";
	private static final String DAY_ID = "day";
	private static final String TEMPERATURE_ID = "temperature";
	private static final String CONDITIONS_ID = "conditions";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_WEATHER_TABLE = "CREATE TABLE" + TABLE_WEATHER + "("
				+ LOCATION_ID + "INTEGER PRIMARY KEY," + DAY_ID + " TEXT,"
				+ TEMPERATURE_ID + " TEXT," + CONDITIONS_ID + " TEXT" + ")";
		db.execSQL(CREATE_WEATHER_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
		
		onCreate(db);
	}

}
