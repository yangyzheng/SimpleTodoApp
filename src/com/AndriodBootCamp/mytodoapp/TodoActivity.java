package com.AndriodBootCamp.mytodoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends ActionBarActivity {
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	private final int EDIT_REQUEST = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		lvItems = (ListView)findViewById(R.id.lvItems);
		etNewItem = (EditText)findViewById(R.id.etNewItem);
		readItems();
		//populateArrayItems();	//in-memory ToDo list	  
		//Note: "this" refers to the current context/activity, which can also be obtained
		//by calling getBaseContext()
		todoAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, todoItems);		
		lvItems.setAdapter(todoAdapter);
		setupListViewListener();
	}

	private void setupListViewListener() {
		
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item,
					int pos, long id) {
				todoItems.remove(pos);
				todoAdapter.notifyDataSetChanged();
				saveItems(); //save changes to file when the item is deleted
				return true;
			}		
		});
	
		lvItems.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				
				launchEditView(todoItems.get(pos), pos);				
			}     
		});
	}
	
	//uses intent to launch edit activity within the same application
		public void launchEditView(String itemToEdit, int itemPos) {
			  // first parameter is the context, second is the class of the activity to launch
			  Intent it = new Intent(this, EditItemActivity.class);
			  it.putExtra("itemToEdit", itemToEdit);
			  it.putExtra("itemPos", itemPos); //selected item
			  //startActivity(it); // brings up the second activity
			  startActivityForResult(it, EDIT_REQUEST);

			}

	 protected void onActivityResult(int requestCode, int resultCode,
             Intent data) {
         if (requestCode == EDIT_REQUEST) {
             if (resultCode == RESULT_OK) {
            	String updatedItem = data.getStringExtra("updatedItem");
         		int itemPos = data.getIntExtra("itemPos", 0);
         		todoItems.set(itemPos, updatedItem);
         		todoAdapter.notifyDataSetChanged();
				saveItems(); //save changes to file with the updated item
             }
             //else{}, no other status yet
         }
     }
	 
	/*private void populateArrayItems() {
		todoItems = new ArrayList<String>();
		todoItems.add("item 1");
		todoItems.add("item 2");
		todoItems.add("item 3");
	}*/

	//read ToDo items from file on disk
	private void readItems(){
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try{
			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch(IOException e){
			todoItems = new ArrayList<String>();
			e.printStackTrace();
		}
	}
	
	//save ToDo items to the file on disk
	private void saveItems(){
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try{
			FileUtils.writeLines(todoFile, todoItems);
		} catch(IOException e){
			e.printStackTrace() ;
		}
	}

	//button click event handler
	public void onAddItem(View v){	
		String itemText = etNewItem.getText().toString();
		todoAdapter.add(itemText);
		etNewItem.setText("");
		saveItems(); //write to file when new item is added
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo, menu);
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
