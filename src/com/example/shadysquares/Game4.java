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
    private ImageView[][] current = new ImageView[5][5];
    private int[][] colorStates = {{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0}};
    private int[] prev = new int[2];
    private long startTime = System.currentTimeMillis();
    private long elapsedTime;
    private Handler handler = new Handler();
    private Button undoBut;
    private Runnable timer;
    private int[][] genCoor; //saves the moves to generate the board
    
    private int gridSize = 0;
    private int moves = 0;
    private int moveCount = 0;
    
    Context context = this;


    @Override
    public void onCreate(Bundle savedInstanceState) {
      	Bundle extras = getIntent().getExtras();
        if (extras != null) {
            moves = extras.getInt("numMoves");
            gridSize = extras.getInt("gridSize");
        }
        super.onCreate(savedInstanceState);
        if (gridSize == 4) {
            super.setContentView(R.layout.activity_game4);   
        }
        else if(gridSize == 3) {
        	super.setContentView(R.layout.activity_game3);
        }
        else if(gridSize == 5) {
        	super.setContentView(R.layout.activity_game5);
        }
        else {
        	finish();
        }
        genCoor = new int[moves][2];

        current[0][0] = (ImageView)(findViewById(R.id.button00));
        current[0][1] = (ImageView)(findViewById(R.id.button01));
        current[0][2] = (ImageView)(findViewById(R.id.button02));
        current[1][0] = (ImageView)(findViewById(R.id.button10));
        current[1][1] = (ImageView)(findViewById(R.id.button11));
        current[1][2] = (ImageView)(findViewById(R.id.button12));
        current[2][0] = (ImageView)(findViewById(R.id.button20));
        current[2][1] = (ImageView)(findViewById(R.id.button21));
        current[2][2] = (ImageView)(findViewById(R.id.button22));
        if (gridSize > 3) {
            current[0][3] = (ImageView)(findViewById(R.id.button03));
            current[1][3] = (ImageView)(findViewById(R.id.button13));
	        current[2][3] = (ImageView)(findViewById(R.id.button23));
	        current[3][0] = (ImageView)(findViewById(R.id.button30));
	        current[3][1] = (ImageView)(findViewById(R.id.button31));
	        current[3][2] = (ImageView)(findViewById(R.id.button32));
	        current[3][3] = (ImageView)(findViewById(R.id.button33));
        }
        if (gridSize > 4) {
        	Log.d("getting", "here");
        	current[0][4] = (ImageView)(findViewById(R.id.button04));
            current[1][4] = (ImageView)(findViewById(R.id.button14));
	        current[2][4] = (ImageView)(findViewById(R.id.button24));
	        current[3][4] = (ImageView)(findViewById(R.id.button34));
	        current[4][0] = (ImageView)(findViewById(R.id.button40));
	        current[4][1] = (ImageView)(findViewById(R.id.button41));
	        current[4][2] = (ImageView)(findViewById(R.id.button42));
	        current[4][3] = (ImageView)(findViewById(R.id.button43));
	        current[4][4] = (ImageView)(findViewById(R.id.button44));
        }
            
        undoBut = (Button) findViewById(R.id.undoBut);
        if (undoBut == null) {
        	Log.d("undoBut", "null");
        }
        undoBut.setEnabled(false);
            
        //Log.d("moves", Integer.toString(moves));
        boolean skip = false;
        for (int i=0; i<moves; i++) {
          	int x = (int) (Math.random()*gridSize);
           	int y = (int) (Math.random()*gridSize);
           	//Log.d("x, y", Integer.toString(x) + " , " + Integer.toString(y));
           	int[] arr = {x, y};
           	for (int j=0; j<i; j++) {
           		if ((x == genCoor[j][0]) && (y == genCoor[j][1])) {
           			skip = true;           			
           			//Log.d("broken", "broken");
           			break;
           		}
           		skip = false;
           	}
           	if (skip) {
           		i--;
           	}
           	else {
	           	clickBox(arr);
	           	clickBox(arr);
	           	genCoor[i][0] = x;
	           	genCoor[i][1] = y;
           	}
        }
            
        /*final TextView eTime = (TextView) findViewById(R.id.elapsedTime);
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
        handler.post(timer);*/
        startTimer();
    }

    public int[] findCoor(ImageView v){
        for(int i = 0; i<gridSize; i++){
            for(int j = 0; j<gridSize; j++){
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
            undoBut.setEnabled(true);
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
            		diag1.setButton(DialogInterface.BUTTON_NEUTRAL, "Go To Leaderboard", 
            				new DialogInterface.OnClickListener() {
            			public void onClick(DialogInterface dialog, int which) {
            				//intent to leaderboard
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
            		/*diag2.setButton(DialogInterface.BUTTON_NEUTRAL, "Keep Trying", 
            				new DialogInterface.OnClickListener() {
            			public void onClick(DialogInterface dialog, int which) {}
            		});*/
            		diag2.setButton(DialogInterface.BUTTON_NEUTRAL, "Retry this Board", 
            				new DialogInterface.OnClickListener() {
            			public void onClick(DialogInterface dialog, int which) {
            				clearBoard();
            				makeMoves(genCoor);
            				startTimer();
            				resetState();
            			}
            		});
            		diag2.show();
            	}
            }
        }
    }
    
    public void clickBox(int[] coor) {
    	int col = 0;
        if (coor[0] + 1 < gridSize) {
            colorStates[coor[0] + 1][coor[1]] = (colorStates[coor[0] + 1][coor[1]] + 1) % 3;
            col = colorStates[coor[0] + 1][coor[1]];
            current[coor[0] + 1][coor[1]].setColorFilter(Color.argb(255, order[col], order[col], order[col]));
        }
        if (coor[0] - 1 >= 0) {
            colorStates[coor[0] - 1][coor[1]] = (colorStates[coor[0] - 1][coor[1]] + 1) % 3;
            col = colorStates[coor[0] - 1][coor[1]];
            current[coor[0] - 1][coor[1]].setColorFilter(Color.argb(255, order[col], order[col], order[col]));
        }
        if (coor[1] + 1 < gridSize) {
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
    	startTime-=1000;
    }
    
    public boolean checkBoard() {
    	for (int i=0; i<gridSize; i++) {
    		for (int j=0; j<gridSize; j++) {
    			if (colorStates[i][j] != 0) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
    
    public void clearBoard() {
    	for (int i=0; i< gridSize; i++) {
    		for (int j=0; j<gridSize; j++) {
    			colorStates[i][j] = 0;
    			current[i][j].setColorFilter(Color.argb(255, order[0], order[0], order[0]));
    		}
    	}
    }
    
    public void makeMoves(int[][] moves) {
    	int coor[] = {0, 0};
    	for (int i=0; i<moves.length; i++) {
    		coor[0] = moves[i][0];
    		coor[1] = moves[i][1];
    		clickBox(coor);
    		clickBox(coor);
    	}
    }
    
    public void startTimer() {
    	startTime = System.currentTimeMillis();
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
    
    public void resetState() {
    	moveCount = 0;
    }

}