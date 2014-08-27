package com.example.shadysquares;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;

public class MainActivity extends ActionBarActivity {
	
	Context context = this;

	int gridSize = 0;
	int moves = 0;
	int lowBound = 3; //lower bound of acceptable moves
	int upBound = 6; //upper bound of acceptable moves
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        NumberPicker np = (NumberPicker) findViewById(R.id.numMoves);
		np.setMaxValue(6);
        np.setMinValue(3);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener( new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    moves = newVal;
                }
            });
        
        final Button button = (Button) findViewById(R.id.proceed);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (R.id.proceed == v.getId()) {
                	if (gridSize != 0) {
                		//EditText et = (EditText) findViewById(R.id.numMoves);
                		
                		//if (!et.getText().toString().equals("")) {
                			//moves = Integer.parseInt(et.getText().toString());
	                		if (moves == 0) {
	                			final AlertDialog diag1 = new AlertDialog.Builder(context).create();
	                    		diag1.setTitle("");
	                    		diag1.setMessage("Please type a number of moves");
	                    		diag1.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", 
	                    				new DialogInterface.OnClickListener() {
	                    			public void onClick(DialogInterface dialog, int which) {}
	                    		});
	                    		diag1.show();
	                		}
	                		if (moves >= lowBound && moves <= upBound) {
	                			/*switch(gridSize) {
	                			case 3:
	                				
	                				break;
	                			case 4:*/
	                				Intent startGame = new Intent(context, Game4.class);
	                				startGame.putExtra("gridSize", gridSize);
		                			startGame.putExtra("numMoves", moves);
		                			startActivity(startGame);
	                				/*break;
	                			case 5:
	                				
	                				break;
	                			default:
	                				break;
	                			}*/
	                			//Intent startGame = new Intent(context, GameActivity.class);
	                			//startGame.putExtra("numMoves", moves);
	                			//startActivity(startGame);
	                		}
	                		else {
	                			final AlertDialog diag2 = new AlertDialog.Builder(context).create();
	                    		diag2.setTitle("");
	                    		diag2.setMessage("Please input a number of moves between " + lowBound + " and " + upBound);
	                    		diag2.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", 
	                    				new DialogInterface.OnClickListener() {
	                    			public void onClick(DialogInterface dialog, int which) {}
	                    		});
	                    		diag2.show();
	                		}
                		} /* else {
                			final AlertDialog diag4 = new AlertDialog.Builder(context).create();
                    		diag4.setTitle("");
                    		diag4.setMessage("Please input a number of moves");
                    		diag4.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", 
                    				new DialogInterface.OnClickListener() {
                    			public void onClick(DialogInterface dialog, int which) {}
                    		});
                    		diag4.show();
                		}*/
                	}
                	else {
                		final AlertDialog diag3 = new AlertDialog.Builder(context).create();
                		diag3.setTitle("");
                		diag3.setMessage("Please select a grid size");
                		diag3.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", 
                				new DialogInterface.OnClickListener() {
                			public void onClick(DialogInterface dialog, int which) {}
                		});
                		diag3.show();
                	}                
            }
        });
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void onRadioButtonClicked(View view) {
    	boolean checked = ((RadioButton) view).isChecked();
    	//EditText et = (EditText) findViewById(R.id.numMoves);
    	switch(view.getId()) {
    	case R.id.size3:
    		if(checked)
    		gridSize=3;
    		lowBound = 3;
    		upBound = 6;
    		break;
    	case R.id.size4:
    		if (checked)
    		gridSize=4;
    		lowBound = 3;
    		upBound = 6;
    		break;
    	case R.id.size5:
    		if(checked)
    		gridSize=5;
    		lowBound = 3;
    		upBound = 6;
    		break;
    	}
    	//et.setHint(lowBound + " to " + upBound);
    }

}
