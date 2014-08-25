package com.example.shadysquares;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Game4 extends ActionBarActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "com.example.hello3.MESSAGE";
    public boolean show = true;
    
    private int[] order = {190,120,75};
    private ImageView[][] current = new ImageView[4][4];
    private int[][] colorStates = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
    private int[] prev = new int[2];
    private long startTime = System.currentTimeMillis();
    private long elapsedTime;
    private Handler handler = new Handler();
    private Button undoBut;
    private Runnable timer;
    private int[][] genCoor; //saves the moves to generate the board
    
    private int moves = 0;
    private int moveCount = 0;
    
    Context context = this;


    @Override
    public void onCreate(Bundle savedInstanceState) {
      	Bundle extras = getIntent().getExtras();
        if (extras != null) {
            moves = extras.getInt("numMoves");
        }
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_game4);
        genCoor = new int[moves][2];

        current[0][0] = (ImageView)(findViewById(R.id.button00));
        current[0][1] = (ImageView)(findViewById(R.id.button01));
        current[0][2] = (ImageView)(findViewById(R.id.button02));
        current[0][3] = (ImageView)(findViewById(R.id.button03));
        current[1][0] = (ImageView)(findViewById(R.id.button10));
        current[1][1] = (ImageView)(findViewById(R.id.button11));
        current[1][2] = (ImageView)(findViewById(R.id.button12));
        current[1][3] = (ImageView)(findViewById(R.id.button13));
        current[2][0] = (ImageView)(findViewById(R.id.button20));
        current[2][1] = (ImageView)(findViewById(R.id.button21));
        current[2][2] = (ImageView)(findViewById(R.id.button22));
        current[2][3] = (ImageView)(findViewById(R.id.button23));
        current[3][0] = (ImageView)(findViewById(R.id.button30));
        current[3][1] = (ImageView)(findViewById(R.id.button31));
        current[3][2] = (ImageView)(findViewById(R.id.button32));
        current[3][3] = (ImageView)(findViewById(R.id.button33));
            
        undoBut = (Button) findViewById(R.id.undoBut);
            
        //Log.d("moves", Integer.toString(moves));
        for (int i=0; i<moves; i++) {
          	int x = (int) (Math.random()*4);
           	int y = (int) (Math.random()*4);
           	int[] arr = {x, y};
           	clickBox(arr);
           	clickBox(arr);
           	genCoor[i][0] = x;
           	genCoor[i][1] = y;
           	//Log.d("x, y", Integer.toString(x) + " , " + Integer.toString(y));
        }
            
        final TextView eTime = (TextView) findViewById(R.id.elapsedTime);
        timer = new Runnable() {
          	@Override
           	public void run() {
           		elapsedTime = System.currentTimeMillis() - startTime;
           		eTime.setText("" + elapsedTime);
           		handler.postDelayed(this, 30);
           		if (elapsedTime > 1000000) {
           			handler.removeCallbacks(this);
           		}
           	}
        };
        handler.post(timer);
    }

    public int[] findCoor(ImageView v){
        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                if(current[i][j].equals(v)){
                    return new int[] {i,j};
                }
            }
        }

        return new int[] {0,0};
    }

    public void onClick(View view) {
        ((Button) view).setText("*");
        ((Button) view).setEnabled(false);
    }

    public void clickImage(View view){
        if(view instanceof ImageView){
            ((ImageView) view).setColorFilter(Color.argb(255,order[2],order[2],order[2]));
            int[] coor = findCoor((ImageView) view);
            clickBox(coor);
            prev[0] = coor[0];
            prev[1] = coor[1];
            moveCount++;
            if (moveCount == moves) { //once all of the moves are used
            	handler.removeCallbacks(timer);
            	if (checkBoard()) {
            		final AlertDialog diag1 = new AlertDialog.Builder(context).create();
            		diag1.setTitle("");
            		diag1.setMessage("Congratulations! \n You completed the puzzle. \n Your score is: " + elapsedTime);
            		diag1.setButton(DialogInterface.BUTTON_POSITIVE, "Return to Menu", 
            				new DialogInterface.OnClickListener() {
            			public void onClick(DialogInterface dialog, int which) {
            				finish();
            			}
            		});
            		diag1.show();
            	}
            	else {
            		final AlertDialog diag2 = new AlertDialog.Builder(context).create();
            		diag2.setTitle("");
            		diag2.setMessage("You did not complete the puzzle in the alloted moves. \n Please try again!");
            		diag2.setButton(DialogInterface.BUTTON_POSITIVE, "Return to Menu", 
            				new DialogInterface.OnClickListener() {
            			public void onClick(DialogInterface dialog, int which) {
            				finish();
            			}
            		});
            		diag2.setButton(DialogInterface.BUTTON_NEUTRAL, "Keep Trying", 
            				new DialogInterface.OnClickListener() {
            			public void onClick(DialogInterface dialog, int which) {}
            		});
            		diag2.show();
            	}
            }
        }
    }
    
    public void clickBox(int[] coor) {
    	int col = 0;
        if (coor[0] + 1 < 4) {
            colorStates[coor[0] + 1][coor[1]] = (colorStates[coor[0] + 1][coor[1]] + 1) % 3;
            col = colorStates[coor[0] + 1][coor[1]];
            current[coor[0] + 1][coor[1]].setColorFilter(Color.argb(255, order[col], order[col], order[col]));
        }
        if (coor[0] - 1 >= 0) {
            colorStates[coor[0] - 1][coor[1]] = (colorStates[coor[0] - 1][coor[1]] + 1) % 3;
            col = colorStates[coor[0] - 1][coor[1]];
            current[coor[0] - 1][coor[1]].setColorFilter(Color.argb(255, order[col], order[col], order[col]));
        }
        if (coor[1] + 1 < 4) {
            colorStates[coor[0]][coor[1] + 1] = (colorStates[coor[0]][coor[1] + 1] + 1) % 3;
            col = colorStates[coor[0]][coor[1] + 1];
            current[coor[0]][coor[1] + 1].setColorFilter(Color.argb(255, order[col], order[col], order[col]));
        }
        if (coor[1] - 1 >= 0){
            colorStates[coor[0]][coor[1] - 1] = (colorStates[coor[0]][coor[1] - 1] + 1) % 3;
            col = colorStates[coor[0]][coor[1] - 1];
            current[coor[0]][coor[1] - 1].setColorFilter(Color.argb(255, order[col], order[col], order[col]));
        }
        colorStates[coor[0]][coor[1]] = (colorStates[coor[0]][coor[1]] + 2) % 3;
        col = colorStates[coor[0]][coor[1]];
        current[coor[0]][coor[1]].setColorFilter(Color.argb(255, order[col], order[col], order[col]));
        undoBut.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Opening Settings...", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void undo(View v) { //for the button undo, connected in the xml file
    	clickBox(prev);
    	clickBox(prev);
    	undoBut.setEnabled(false);
    	moveCount--;
    }
    
    public boolean checkBoard() {
    	for (int i=0; i<colorStates.length; i++) {
    		for (int j=0; j<colorStates[0].length; j++) {
    			if (colorStates[i][j] != 0) {
    				return false;
    			}
    		}
    	}
    	return true;
    }

}