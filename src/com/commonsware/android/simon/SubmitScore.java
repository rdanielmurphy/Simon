package com.commonsware.android.simon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class SubmitScore extends Activity {
	GameDataBaseInterface db;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.submitscore);
               
		
		final Button buttonCancel = (Button) findViewById(R.id.btnCancel);	
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});		
		final Button buttonSubmit = (Button) findViewById(R.id.btnSubmit);	
		buttonSubmit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});		
    }
}