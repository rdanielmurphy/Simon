package com.commonsware.android.simon;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class GameDataBaseInterface {
	private GameDataBase db;

	public GameDataBaseInterface(Context context) {
		try {
			db = new GameDataBase(context);
			db.open();
		} catch (Exception e) {
			new AlertDialog.Builder(context).setTitle("ERROR").setMessage(
					e.getMessage()).setNeutralButton("Close",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dlg, int sumthin) {
						}
					}).show();
		}
	}

	public List<Map<String,String>> GetScores() {
		return db.getEntries();
	}

	public void DeleteScore(String user, String score) {
		db.DeleteScore(user, score);
	}

	public void AddScore(String user, String score) {
		if(db.IsAHighScore(score)){
			db.AddScore(user, score);
		}
	}
	
	public boolean IsHighScore(String score) {
		return db.IsAHighScore(score);
	}
	
	public int GetNumberOfScores(){
		return db.NumberOfEntries();
	}
	
	public void Close(){
		db.close();
	}
}