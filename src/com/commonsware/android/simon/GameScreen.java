package com.commonsware.android.simon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.commonsware.android.simon.SimonView.GameGUIThread;

public class GameScreen extends Activity {
	
	private EditText txtBoxUser;

	/** A handle to the thread that's actually running the animation. */
	private GameGUIThread mGameThread;

	/** A handle to the View in which the game is running. */
	private SimonView mGameView;
	private android.widget.TextView tv;

	private boolean bCanSave = false;

	// @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// turn off the window's title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		registerForContextMenu(new View(this.getApplicationContext()));

		tv = (TextView) findViewById(R.id.lblRound);

		mGameView = (SimonView) findViewById(R.id.game);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(getApplication()).inflate(R.menu.sample, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().equals("Exit Game")) {
			EndGame();
			finish();
		} else if (item.getTitle().toString().equals("New Game")) {
			StartNewGame();
		} else if (item.getTitle().toString().equals("View High Scores")) {
			final Intent i = new Intent(this, DialogActivity.class);
			startActivity(i);
		} else if (item.getTitle().toString().equals("Submit Score")) {			
			 if(bCanSave){ 	 
				Dialog dialog = new Dialog(this);
				dialog.setContentView(R.layout.submitscore);
				dialog.setTitle("Enter Name");
				dialog.show();
				Button buttonOK = (Button) dialog.findViewById(R.id.btnSubmit);
				buttonOK.setOnClickListener(new OKListener(dialog, this));
				Button buttonCancel = (Button) dialog.findViewById(R.id.btnCancel);
				buttonCancel.setOnClickListener(new CancelListener(dialog));
				txtBoxUser = (EditText) dialog.findViewById(R.id.txtBoxName);
			 }
		}
		return true;
	}

	protected class OKListener implements OnClickListener {

		private Dialog dialog;
		private Activity activity;

		public OKListener(Dialog dialog, Activity activity) {
			this.dialog = dialog;
			this.activity = activity;
		}

		public void onClick(View v) {
			GameDataBaseInterface db; 
			db = new GameDataBaseInterface(activity);
			db.AddScore(txtBoxUser.getText().toString(),Integer.toString(mGameView.iRound - 1));			 
			db.Close(); 
			bCanSave = false; 
			EndGame();
			dialog.dismiss();
		}
	}
	
	protected class CancelListener implements OnClickListener {

		private Dialog dialog;

		public CancelListener(Dialog dialog) {
			this.dialog = dialog;
		}

		public void onClick(View v) {
			dialog.dismiss();
		}
	}

	public void StartNewGame() {
		bCanSave = true;
		// mGameView.EndGame();
		mGameView.StartGame();
	}

	public void EndGame() {
		mGameView.StopThreads();
	}
}