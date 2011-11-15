package com.commonsware.android.simon;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HighScores extends ListActivity {
	GameDataBaseInterface db;
	List<Map<String,String>> list;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		setContentView(R.layout.highscores);

		// get translations from db
		db = new GameDataBaseInterface(this.getApplicationContext());
		list = db.GetScores();
		
		setListAdapter(new IconicAdapter(this));
		}catch(Exception e){
			list.indexOf(e.getMessage());
		}
	}
	
	class IconicAdapter extends ArrayAdapter {
		Activity context;

		@SuppressWarnings("unchecked")
		IconicAdapter(Activity context) {
			super(context, R.layout.score, list);

			this.context = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = context.getLayoutInflater();

				row = inflater.inflate(R.layout.score, null);
			}
			TextView labelScore = (TextView) row
					.findViewById(R.id.lblScore);
			TextView labelUser = (TextView) row
			        .findViewById(R.id.lblUser);

			labelUser.setText(list.get(position).get(GameDataBase.KEY_USER));
			labelScore.setText(list.get(position).get(GameDataBase.KEY_SCORE));

			return (row);
		}
	}
}