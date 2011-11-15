package com.commonsware.android.simon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DialogActivity extends Activity {
	GameDataBaseInterface db;
	List<Map<String,String>> list;
	List<Object> listTemp;	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
        
        db = new GameDataBaseInterface(this);
        list = db.GetScores();
        
        listTemp = new ArrayList<Object>();
        for(Map<String,String> map : list){
        	listTemp.add(map.get(GameDataBase.KEY_USER) + "- " + map.get(GameDataBase.KEY_SCORE));
        }
        
		final ListView list = (ListView) findViewById(R.id.list);
		adapter = new ListItemsAdapter(listTemp);
		list.setAdapter(adapter);
		
		final Button buttonOk = (Button) findViewById(R.id.ok);	
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});			
    }
    
	private class ListItemsAdapter extends ArrayAdapter<Object> {
		public ListItemsAdapter(List<Object> items) {
			super(DialogActivity.this, android.R.layout.simple_list_item_1, items);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			convertView = inflater.inflate(R.layout.score, null);
			
			TextView labelScore = (TextView) convertView.findViewById(R.id.lblScore);
	        TextView labelUser = (TextView) convertView.findViewById(R.id.lblUser);
	        
			labelUser.setText(list.get(position).get(GameDataBase.KEY_USER));
			labelScore.setText(list.get(position).get(GameDataBase.KEY_SCORE));
			
			return convertView;
		}
	}
	 
	private ListItemsAdapter adapter = null; 
}