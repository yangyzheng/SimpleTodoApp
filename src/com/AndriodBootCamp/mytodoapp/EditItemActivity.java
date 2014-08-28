package com.AndriodBootCamp.mytodoapp;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends ActionBarActivity {

	private EditText etEditItem;
	private int itemPos;
	private String itemToEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		//get data passed in from Intent bundle
		itemToEdit = getIntent().getStringExtra("itemToEdit");
		itemPos = getIntent().getIntExtra("itemPos", 0);		
		etEditItem.setText(itemToEdit);
		//Be sure the user's cursor in the text field is at the end of the current text
		//value and is in focus by default.
		etEditItem.setSelection(itemToEdit.length());		
	}
	
	public void onSaveItem(View v){	
		//send the result back via data intent
		  Intent data = new Intent();
		  // Pass relevant data back as a result
		  data.putExtra("updatedItem", etEditItem.getText().toString());
		  data.putExtra("itemPos", itemPos);
		  // Activity finished OK, return the data
		  setResult(RESULT_OK, data); // set result code and bundle data for response

		this.finish();
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
