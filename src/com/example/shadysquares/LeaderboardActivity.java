package com.example.shadysquares;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
		title.setText("Top Scores: " + size + "x" + size + " board, " + size + " moves");
		
		TextView name, score;	// using same variable for all name/score textviews
		Entry<Float, String> scorePair;
		name = (TextView)findViewById(R.id.name1);		// getting the paired textviews
		score = (TextView)findViewById(R.id.score1);
		scorePair = scores.pollFirstEntry();			// get (and remove) highest score
		name.setText("1. " + scorePair.getValue());		// set text
		score.setText("" + scorePair.getKey());		
		if (scores.size() > 0) {
			name = (TextView)findViewById(R.id.name2);	// repeat
			score = (TextView)findViewById(R.id.score2);
			scorePair = scores.pollFirstEntry();	
			name.setText("1. " + scorePair.getValue());			
			score.setText("" + scorePair.getKey());		
		}
		if (scores.size() > 0) {
			name = (TextView)findViewById(R.id.name3);		
			score = (TextView)findViewById(R.id.score3);
			scorePair = scores.pollFirstEntry();	
			name.setText("1. " + scorePair.getValue());		
			score.setText("" + scorePair.getKey());		
		}
		if (scores.size() > 0) {
			name = (TextView)findViewById(R.id.name4);	
			score = (TextView)findViewById(R.id.score4);
			scorePair = scores.pollFirstEntry();	
			name.setText("1. " + scorePair.getValue());	
			score.setText("" + scorePair.getKey());		
		}
		if (scores.size() > 0) {
			name = (TextView)findViewById(R.id.name5);		
			score = (TextView)findViewById(R.id.score5);
			scorePair = scores.pollFirstEntry();
			name.setText("1. " + scorePair.getValue());
			score.setText("" + scorePair.getKey());		
		}
	}
	
	public void startMenu(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		float time = 0f;
		String name = "";								// init time, init name as blank
		int moves = 3, gridSize = 3;					// same as other activities
		Bundle extras = getIntent().getExtras();		// get finished game stats
        if (extras != null) {
        	time = extras.getFloat("time");
        	name = extras.getString("name");
            moves = extras.getInt("numMoves");
            gridSize = extras.getInt("gridSize");
        }
        
		super.onCreate(savedInstanceState);				// standard onCreate
		setContentView(R.layout.activity_leaderboard);
		
		addScore(time, name, moves, gridSize);
		createLeaderboard(moves, gridSize);				// create the initial leaderboard
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
