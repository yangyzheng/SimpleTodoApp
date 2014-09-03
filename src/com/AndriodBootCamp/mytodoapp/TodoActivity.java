package com.AndriodBootCamp.mytodoapp;

//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.Toast;

public class TodoActivity extends ActionBarActivity {
//	private ArrayList<String> todoItems;
//	private ArrayAdapter<String> todoAdapter;
	
	private ListView lvItems;
	private EditText etNewItem;
	private final int EDIT_REQUEST = 100;
	private List<Todo> todoItems;
	private ArrayAdapter<Todo> todoAdapter ;
	
	private View viewContainer;
	private TodoDataSource datasource;
	private Todo lastDeletedItem;
	private int lastDeletedPos;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		lvItems = (ListView)findViewById(R.id.lvItems);
		etNewItem = (EditText)findViewById(R.id.etNewItem);
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/calligraffiti.ttf");
		etNewItem.setTypeface(type);
		viewContainer = findViewById(R.id.undobar);
		
		//open database
		datasource = new TodoDataSource(this);
	    datasource.open();
	    
	    int count = datasource.getTodosCount(null, null);
	    if (count ==0){
		    datasource.addTodo(new Todo(1, "T-con with PIC", 3, "2014-09-02 22:00:00"));
		    datasource.addTodo(new Todo(1, "Piano Class", 4, "2014-09-02 17:30:00"));
		    datasource.addTodo(new Todo(1, "Submit code", 5, "2014-09-02 21:00:00"));
		    datasource.addTodo(new Todo(1, "Pickup dry clean", 1, "2014-09-02 17:00:00"));
		    datasource.addTodo(new Todo(1, "Check homework", 1, "2014-09-02 7:00:00"));
		    datasource.addTodo(new Todo(1, "Email Daddy", 2, "2014-09-02 22:00:00"));
	    }
		
	    todoItems = datasource.getAllTodosInTimeRange(null, null);
	    todoAdapter = new TodoArrayAdapter(this, todoItems);
	    lvItems.setAdapter(todoAdapter);
		 
//		readItems();  
//		Note: "this" refers to the current context/activity, which can also be obtained
//		by calling getBaseContext()
//		todoAdapter = new ArrayAdapter<String>(this, 
//				android.R.layout.simple_list_item_1, todoItems);		
//		lvItems.setAdapter(todoAdapter);
		setupListViewListener();
	}

	private void setupListViewListener() {
		
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item,
					int pos, long id) {
				lastDeletedItem = todoItems.get(pos);
				lastDeletedPos = pos;
				todoItems.remove(pos);
				todoAdapter.notifyDataSetChanged();
				showUndo(viewContainer);
				//save changes to DB when the item is deleted
				datasource.deleteTodo(lastDeletedItem);
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
	
	public void onClickUndo(View view) {
			todoItems.add(lastDeletedPos, lastDeletedItem);
			todoAdapter.notifyDataSetChanged();
		    Toast.makeText(this, "Deletion undone", Toast.LENGTH_LONG).show();
		    viewContainer.setVisibility(View.GONE);
		  }

		  public static void showUndo(final View viewContainer) {
		    viewContainer.setVisibility(View.VISIBLE);
		    viewContainer.setAlpha(1);
		    viewContainer.animate().alpha(0.4f).setDuration(5000).withEndAction(new Runnable() {

		          @Override
		          public void run() {
		            viewContainer.setVisibility(View.GONE);
		          }
		        });

	}
	
	//uses intent to launch edit activity within the same application
		public void launchEditView(Todo todoItem, int itemPos) {
			  // first parameter is the context, second is the class of the activity to launch
			  Intent it = new Intent(this, EditItemActivity.class);
			  it.putExtra("itemToEdit", todoItem);
			  it.putExtra("itemPos", itemPos); //selected item
			  // brings up the second activity
			  startActivityForResult(it, EDIT_REQUEST);

			}

	 protected void onActivityResult(int requestCode, int resultCode,
             Intent data) {
         if (requestCode == EDIT_REQUEST) {
             if (resultCode == RESULT_OK) {
            	Todo updatedItem = data.getParcelableExtra("updatedItem");
         		int itemPos = data.getIntExtra("itemPos", 0);
         		todoItems.set(itemPos, updatedItem);
         		todoAdapter.notifyDataSetChanged();
         		datasource.updateTodo(updatedItem);				
             }
             //else{}, no other status yet
         }
     }
	 
	//button click event handler
	public void onAddItem(View v){	
		String itemText = etNewItem.getText().toString();
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		String dueDate = strDate.split("\\s+")[0] + " 00:00:00";
		 
		Todo newTodo = new Todo(1, itemText, 1, dueDate);
		Todo addedTodo = datasource.addTodo(newTodo);
		todoItems.add(addedTodo);
		todoAdapter.notifyDataSetChanged();
		
	}
	 
	/*private void populateArrayItems() {
		todoItems = new ArrayList<String>();
		todoItems.add("item 1");
		todoItems.add("item 2");
		todoItems.add("item 3");
	}*/

//	//read ToDo items from file on disk
//	private void readItems(){
//		File filesDir = getFilesDir();
//		File todoFile = new File(filesDir, "todo.txt");
//		try{
//			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
//		} catch(IOException e){
//			todoItems = new ArrayList<String>();
//			e.printStackTrace();
//		}
//	}
//	
//	//save ToDo items to the file on disk
//	private void saveItems(){
//		File filesDir = getFilesDir();
//		File todoFile = new File(filesDir, "todo.txt");
//		try{
//			FileUtils.writeLines(todoFile, todoItems);
//		} catch(IOException e){
//			e.printStackTrace() ;
//		}
//	}
//

	
	
//	 private List<Model> getModel() {
//		    List<Model> list = new ArrayList<Model>();
//		    list.add(get("T-con @10:20 am"));
//		    list.add(get("Pick up dry clean"));
//		    list.add(get("Percussion class"));
//		    list.add(get("Get groceries"));
//		    list.add(get("Submit todo app"));
//		    list.add(get("Get a hair cut"));
//		    // Initially select one of the items
//		    list.get(1).setSelected(true);
//		    return list;
//		  }
//
//	  private Model get(String s) {
//	    return new Model(s);
//	  }
		  
		  
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
