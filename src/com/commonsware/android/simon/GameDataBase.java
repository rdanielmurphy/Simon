package com.commonsware.android.simon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class GameDataBase {

	private static final String DATABASE_NAME = "Simon.db";
	private static final String GAME_TABLE = "HighScores";
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_USER = "user";
	public static final String KEY_SCORE = "score";

	private static final int MAX_SCORES = 10;
	
	private static final String DATABASE_CREATE = "create table "
			+ GAME_TABLE + " (" + KEY_USER + " text not null, " 
			+ KEY_SCORE
			+ " text not null);";

	private SQLiteDatabase db;
	private final Context context;
	private MyDbHelper myDbHelper;

	// constructor create the wrapper to open and close the db
	public GameDataBase(Context cxt) {
		context = cxt;
		myDbHelper = new MyDbHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public GameDataBase open() throws SQLException {

		db = myDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
		//db.delete(GAME_TABLE, null, null);
	}

	public List<Map<String,String>> getEntries() {
		List<Map<String,String>> entries = new ArrayList<Map<String,String>>();
		
		Cursor c = db.query(GAME_TABLE, null, null,
				null, null, null, KEY_SCORE + " desc");

		int iNumOfEntries = c.getCount();
		c.moveToFirst();
		for (int i = 0; i < iNumOfEntries; i++) {
			Map<String,String> entry = new HashMap<String,String>();
			int columnUser = c.getColumnIndex(KEY_USER);
			int columnScore= c.getColumnIndex(KEY_SCORE);
			
			entry.put(KEY_USER,c.getString(columnUser));
			entry.put(KEY_SCORE,c.getString(columnScore));

			entries.add(entry);
			
			c.moveToNext();
		}
		return entries;
	}

	public int NumberOfEntries() {
		Cursor c = db.query(GAME_TABLE, null, null,
				null, null, null, null);

		return c.getCount();
	}
	
	public void AddScore(String user, String score) {		
		ContentValues cv = new ContentValues();		
		cv.put(KEY_USER, user);
		cv.put(KEY_SCORE, score);
		db.insert(GAME_TABLE, null, cv);	
		
		List<Map<String,String>> enties = getEntries();
		for(int i = MAX_SCORES; i < enties.size(); i++)
			DeleteScore(enties.get(i).get(KEY_USER), enties.get(i).get(KEY_SCORE));
	}
	
	public boolean IsAHighScore(String score) {
		if(NumberOfEntries() < MAX_SCORES)
			return true;

		List<Map<String,String>> entries = getEntries();
		int min = Integer.valueOf(entries.get(0).get(KEY_SCORE));
		for(Map<String,String> entry : entries){
			int curr = Integer.valueOf(entry.get(KEY_SCORE));
			if(curr < min)
				min = curr;
		}
		if(Integer.valueOf(score) > min)
			return true;
		
		return false;
	}
	
	public void DeleteScore(String user, String score) {
		String deletequery[] = {user, score};
		String whereClause = KEY_USER + "=? AND " + KEY_SCORE + "=?";
		db.delete(GAME_TABLE,whereClause, deletequery);
	}

	private static class MyDbHelper extends SQLiteOpenHelper {
		public MyDbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		// Only gets called if the database does not exist on the phone
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE);
		}

		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			// Drop old one
			_db.execSQL("DROP TABLE IF EXISTS " + GAME_TABLE);
			// Create new one
			onCreate(_db);
		}
		
		public void onOpen(SQLiteDatabase db){
			super.onOpen(db);
		}
	}
}