package com.AndriodBootCamp.mytodoapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditItemActivity extends ActionBarActivity {

	private TextView tvEditLabel, tvPriority, tvDueTime;
	private EditText etEditItem, etPriority, etDueTime;	
	
	private int itemPos;
	private Todo itemToEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		tvEditLabel = (TextView) findViewById(R.id.tvEditLabel);
		tvPriority = (TextView) findViewById(R.id.tvPriority);
		tvDueTime = (TextView) findViewById(R.id.tvDueTime);
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		etPriority = (EditText) findViewById(R.id.etPriority);
		etDueTime = (EditText) findViewById(R.id.etDueTime);
		
		Typeface typeET = Typeface.createFromAsset(getAssets(),"fonts/calligraffiti.ttf");
		etEditItem.setTypeface(typeET);
		etPriority.setTypeface(typeET);
		etDueTime.setTypeface(typeET);
		
		Typeface typeTV = Typeface.createFromAsset(getAssets(),"fonts/devroye.ttf");
		tvEditLabel.setTypeface(typeTV);
		tvPriority.setTypeface(typeTV);
		tvDueTime.setTypeface(typeTV);
		
		//get data passed in from Intent bundle
		itemToEdit = getIntent().getParcelableExtra("itemToEdit");
		itemPos = getIntent().getIntExtra("itemPos", 0);
		String todoText = itemToEdit.getItem();
		etEditItem.setText(todoText);
		//Be sure the user's cursor in the text field is at the end of the current text
		//value and is in focus by default.
		etEditItem.setSelection(todoText.length());
		
		String priority = Integer.toString(itemToEdit.getPriority());
		etPriority.setText(priority);
		etPriority.setSelection(priority.length());
		
		String dueTime = TodoUtils.GetDueTimeStringForDisplay(itemToEdit.getDueDate());
		etDueTime.setText(dueTime);
		etDueTime.setSelection(dueTime.length());
		
		final Activity context = this;
		
		etDueTime.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
				  Calendar mcurrentTime = Calendar.getInstance();
			      int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
			      int minute = mcurrentTime.get(Calendar.MINUTE);
			      TimePickerDialog mTimePicker;
			      mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
			          @Override
			          public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {				        	  
			        	  String ampm = selectedHour < 12 ? "AM" : "PM";
			        	  etDueTime.setText( selectedHour%12 + ":" + selectedMinute + " " + ampm);
			          }
			      }, hour, minute, true);
			      mTimePicker.setTitle("Select todo item Due Time");
			      mTimePicker.show();
	            }
	          });
		
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/calligraffiti.ttf");
		etEditItem.setTypeface(type);
		
		
	}
	
	public void onSaveItem(View v){	
		//send the result back via data intent
		  Intent data = new Intent();
		  // Pass relevant data back as a result
		  itemToEdit.setItem(etEditItem.getText().toString());
		  int priority = 1;

		  try {
			  priority = Integer.parseInt(etPriority.getText().toString());
		  } catch(NumberFormatException nfe) {
		     System.out.println("Could not parse " + nfe);
		  } 
		  itemToEdit.setPriority(priority);
		  String dueDate = parseDueDate(etDueTime.getText().toString());
		  itemToEdit.setDueDate(dueDate);
		  data.putExtra("updatedItem", itemToEdit);
		  data.putExtra("itemPos", itemPos);
		  // Activity finished OK, return the data
		  setResult(RESULT_OK, data); // set result code and bundle data for response

		this.finish();
	}
	
    private String parseDueDate(String ampmTime){
    	String hm = ampmTime.split("\\s+")[0]; 
    	String[] hhmm = hm.split("\\:");
    	int hour = 0;

		  try {
			  hour = Integer.parseInt(hhmm[0]);
		  } catch(NumberFormatException nfe) {
		     System.out.println("Could not parse " + nfe);
		  } 
    	
		 hour += ampmTime.contains("PM") ? 12 : 0;
		 String hh = Integer.toString(hour);
		 
		 SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date now = new Date();
		 String strDate = sdfDate.format(now);
    	
		 return (strDate.split("\\s+")[0] + " " + "00".substring(hh.length()) + hh + ":" + hhmm[1] + ":00");
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
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
