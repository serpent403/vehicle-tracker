package com.testc2dmservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "barge.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE = "bargeDetails";
	public static final String C_ID = BaseColumns._ID;
	public static final String B_ID = "barge_id";
	public static final String B_NAME = "barge_name";
	public static final String B_LAT = "barge_lat";
	public static final String B_LONG = "barge_long";
	public static final String B_TROUBLED = "barge_status";
	public static final String B_TIME = "notif_time";
	public static final String B_MESSAGE = "barge_message";

	Context context;

	// name is the database file name
	// database version number they are constant
	// its a best practice to have a unique identifier

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * String sql = String.format
		 * ("create table %s (%s INT primary key, %s TEXT,%s TEXT)",TABLE,
		 * C_ID,C_NAME,C_PROF);
		 */
		String testsql = String
				.format("create table %s (%s INT primary key,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT)",
						TABLE, C_ID, B_ID, B_NAME, B_LAT, B_LONG, B_TROUBLED,
						B_TIME, B_MESSAGE);

		db.execSQL(testsql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("drop table if exists " + TABLE);
		this.onCreate(db);

	}

}
