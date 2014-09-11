package com.example.shadysquares;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map.Entry;
import java.util.TreeMap;

public class LeaderboardActivity extends ActionBarActivity {
	float time = 0f;
	//String name = "";								// init time, init name as blank
	int moves = 3, gridSize = 3;					// same as other activities

	private void addScore(float time, String name, int moves, int size) {
		TreeMap<Float, String> currentScores = new TreeMap<Float, String>();
		try {
			// the following is why java i/o deserves to go die alone in a fiery hole
			BufferedReader scoreFile = new BufferedReader(new InputStreamReader(openFileInput("ldrboard_" + moves + "_" + size + ".txt")));

			String inString;											// temp string to get each line
			while ((inString = scoreFile.readLine()) != null) {			// read every line
				String[] scorePair = inString.split(" ");				// get the name and number of moves
				currentScores.put(Float.parseFloat(scorePair[0]), scorePair[1]);	// add to treemap, time being the key
			}
			scoreFile.close();

			currentScores.put(time, name);				// take only the top 5 scores
			if (currentScores.keySet().size() > 5) {
				currentScores.pollLastEntry();
			}

			// if you thought it couldn't get longer
			BufferedWriter newFile = new BufferedWriter(
					new OutputStreamWriter(openFileOutput("ldrboard_" + moves + "_" + size + ".txt", Context.MODE_PRIVATE)));
			Entry<Float, String> score;					// put them back in the file
			while ((score = currentScores.pollFirstEntry()) != null) {
				newFile.write(score.getKey() + " " + score.getValue() + "\n");
			}
			newFile.close();
		} catch (FileNotFoundException e) { 
			try {
				// hiii again
				BufferedWriter scoreFile = new BufferedWriter(
						new OutputStreamWriter(openFileOutput("ldrboard_" + moves + "_" + size + ".txt", Context.MODE_PRIVATE)));
				scoreFile.write(time + " " + name + "\n");
				scoreFile.close();
			} catch (IOException ie) {
				// oh well...(opa GridWorld style)
			}
		} catch (IOException ie) {
			// oh well...(opa GridWorld style)
		}
	}

	private TreeMap<Float, String> getScores(int moves, int size) {
		TreeMap<Float, String> returnMap = new TreeMap<Float, String>();	// create map to hold scores (5 max)
		try {
			// init the file to read scores from
			BufferedReader scoreFile = new BufferedReader(new InputStreamReader(openFileInput("ldrboard_" + moves + "_" + size + ".txt")));

			String inString;											// temp string to get each line
			while ((inString = scoreFile.readLine()) != null) {			// read every line
				String[] scorePair = inString.split(" ");				// get the name and number of moves
				returnMap.put(Float.parseFloat(scorePair[0]), scorePair[1]);	// add to treemap, time being the key
			}
			scoreFile.close();
		} catch (IOException ie) {
			// oh well...(opa GridWorld style)
		}

		return returnMap;
	}

	private void createLeaderboard(int moves, int size) {
		TreeMap<Float, String> scores = getScores(moves, size);		// get the paired scores for the current board
		TextView title = (TextView)findViewById(R.id.title);		// set the title of the current board
		title.setText(size + "x" + size + " board, " + moves + " moves");

		TextView name, score;	// using same variable for all name/score textviews
		Entry<Float, String> scorePair;
		name = (TextView)findViewById(R.id.name1);		// getting the paired textviews
		score = (TextView)findViewById(R.id.score1);
		scorePair = scores.pollFirstEntry();			// get (and remove) highest score
		name.setText("1. " + scorePair.getValue());		// set text
		score.setText(scorePair.getKey() + "s");		
		if (scores.size() > 0) {
			name = (TextView)findViewById(R.id.name2);	// repeat
			score = (TextView)findViewById(R.id.score2);
			scorePair = scores.pollFirstEntry();	
			name.setText("2. " + scorePair.getValue());			
			score.setText(scorePair.getKey() + "s");	
		}
		if (scores.size() > 0) {
			name = (TextView)findViewById(R.id.name3);		
			score = (TextView)findViewById(R.id.score3);
			scorePair = scores.pollFirstEntry();	
			name.setText("3. " + scorePair.getValue());		
			score.setText(scorePair.getKey() + "s");	
		}
		if (scores.size() > 0) {
			name = (TextView)findViewById(R.id.name4);	
			score = (TextView)findViewById(R.id.score4);
			scorePair = scores.pollFirstEntry();	
			name.setText("4. " + scorePair.getValue());	
			score.setText(scorePair.getKey() + "s");	
		}
		if (scores.size() > 0) {
			name = (TextView)findViewById(R.id.name5);		
			score = (TextView)findViewById(R.id.score5);
			scorePair = scores.pollFirstEntry();
			name.setText("5. " + scorePair.getValue());
			score.setText(scorePair.getKey() + "s");	
		}
	}

	public void startMenu(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
	}
	
	private void getName() {
		// copied from http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("New High Score!");
		alert.setMessage("You got a new high score! Enter your name:");

		// Set an EditText view to g1et user input 
		final EditText input = new EditText(this);
		InputFilter[] maxFilter = {new InputFilter.LengthFilter(10)}; 
		input.setFilters(maxFilter);
		alert.setView(input);

		final LeaderboardActivity self = this;
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				self.finalizeBoard(input.getText().toString());
			}
		});
		
		final AlertDialog dialog = alert.create();
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
		// copied again from http://stackoverflow.com/questions/20682865/disable-button-when-edit-text-fields-empty
		//TextWatcher
	    final TextWatcher textWatcher = new TextWatcher() {
	        @Override
	        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
	        {

	        }

	        @Override
	        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
	            if (input.getText().toString().equals("")) {
	            	dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
	            } else {
	            	dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
	            }
	        }

	        @Override
	        public void afterTextChanged(Editable editable) {
	        }
	    };
	    input.addTextChangedListener(textWatcher);
	}

	private void finalizeBoard(String name) {
		addScore(time, name, moves, gridSize);
		createLeaderboard(moves, gridSize);				// create the initial leaderboard
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				// standard onCreate
		setContentView(R.layout.activity_leaderboard);

		Bundle extras = getIntent().getExtras();		// get finished game stats

		if (extras != null) {
			moves = extras.getInt("numMoves");			// extract extras
			gridSize = extras.getInt("gridSize");
			if (getIntent().hasExtra("time")) {			// take and add score only if it exists
				time = extras.getFloat("time");
				if (getScores(moves, gridSize).size() > 0) {
					if (getScores(moves, gridSize).lastKey() > time || getScores(moves, gridSize).size() < 5) {	// ask for name if necessary
						getName();
					} else {
						createLeaderboard(moves, gridSize);
					}
				} else {
					getName();
				}
			} else {
				createLeaderboard(moves, gridSize);				// create the initial leaderboard
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leaderboard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
