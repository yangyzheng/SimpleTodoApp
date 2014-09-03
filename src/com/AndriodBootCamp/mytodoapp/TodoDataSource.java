package com.AndriodBootCamp.mytodoapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

//Data Source for ToDo table
public class TodoDataSource {

	// Database fields
	  private SQLiteDatabase database;
	  private TodoSQLiteHelper dbHelper;
	  private String[] allColumns = { 
			  TodoSQLiteHelper.COLUMN_ID,
			  TodoSQLiteHelper.COLUMN_ITEM,
			  TodoSQLiteHelper.COLUMN_PRIORITY,
			  TodoSQLiteHelper.COLUMN_DUEDATE};
			 // TodoSQLiteHelper.COLUMN_CREATEDDATEIME,
			 // TodoSQLiteHelper.COLUMN_MODIFIEDDATETIME};
			  

	  public TodoDataSource(Context context) {
	    dbHelper = new TodoSQLiteHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	// Adding new contact
	  public Todo addTodo(Todo todo) {
		  
	    ContentValues values = new ContentValues();
	    values.put(allColumns[1], todo.getItem()); // item
	    values.put(allColumns[2], todo.getPriority()); // priority
	    values.put(allColumns[3], todo.getDueDate().toString()); // dueDate
	    //values.put(allColumns[4], todo.getCreatedDateTime().toString()); //creation
	    //values.put(allColumns[5], todo.getModifiedDateTime().toString()); //modification
	 
	    long insertId = database.insert(TodoSQLiteHelper.TABLE_TODO, null, values);
	    Cursor cursor = database.query(TodoSQLiteHelper.TABLE_TODO,
	            allColumns, TodoSQLiteHelper.COLUMN_ID + " = " + insertId, null,
	            null, null, null);
	    cursor.moveToFirst();
	    Todo newTodo = cursorToToDo(cursor);
        cursor.close();
        return newTodo;
	  }
	   
	  // Getting single ToDo item
	  public Todo getTodo(int id) {
        Cursor cursor = database.query(TodoSQLiteHelper.TABLE_TODO, 
        		allColumns, 
        		TodoSQLiteHelper.COLUMN_ID + " = ",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        
        Todo todoItem = cursorToToDo(cursor);
        cursor.close();
        return todoItem;
	  }
	   
	  // Getting ToDo items with time range
	  public List<Todo> getAllTodosInTimeRange(String beginDate, String endDate) {
		  List<Todo> todoList = new ArrayList<Todo>();
		  String  selectQuery;
		  if (beginDate == null || endDate == null) 
		  {
			//get total count
			  selectQuery  = "SELECT * FROM " + TodoSQLiteHelper.TABLE_TODO ;
		  }
		  else{

//	        String  selectQuery  = "SELECT * FROM " + TodoSQLiteHelper.TABLE_TODO +
//	        		" where " + TodoSQLiteHelper.COLUMN_DUEDATE + " >= Datetime("+beginDate +")" + 
//	        		" AND " + TodoSQLiteHelper.COLUMN_DUEDATE + " <= Datetime(" + endDate +")";
	        
	        selectQuery  = "SELECT * FROM " + TodoSQLiteHelper.TABLE_TODO +
	        		" WHERE strftime('%s', " + TodoSQLiteHelper.COLUMN_DUEDATE + ")  BETWEEN strftime('%s', " +  
	        				beginDate + ") AND strftime('%s', " + endDate + ")";
		  }
	        
	        Cursor cursor = database.rawQuery(selectQuery, null);
	 
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	 Todo todoItem = cursorToToDo(cursor);
	                // Adding ToDo item to list
	            	 todoList.add(todoItem);
	            } while (cursor.moveToNext());
	        }
	        
//	        different way of looping
//	        cursor.moveToFirst();
//		    while (!cursor.isAfterLast()) {
//		    	Todo todoItem = cursorToToDo(cursor);
//		    	 todoList.add(todoItem);
//		      cursor.moveToNext();
//		    }
	        cursor.close();
		  return todoList;
	  }
	   
	  // Getting ToDo Count
	  public int getTodosCount(String beginDate, String endDate) {
		  String selectQuery;
		  if (beginDate == null || endDate == null) 
		  {
			//get total count
			  selectQuery  = "SELECT * FROM " + TodoSQLiteHelper.TABLE_TODO ;
		  }
		  else{
			  selectQuery  = "SELECT * FROM " + TodoSQLiteHelper.TABLE_TODO +
        		" where " + TodoSQLiteHelper.COLUMN_DUEDATE + " >= DateTime("+beginDate +")" + 
        		" AND " + TodoSQLiteHelper.COLUMN_DUEDATE + " <= DateTime(" + endDate +")";
		  }
	  
		  Cursor cursor = database.rawQuery(selectQuery, null);
		  int count = cursor.getCount();
//		  if (cursor.moveToFirst()) {
//			  count = cursor.getInt(0); //index 0 is id
//		  }
		  cursor.close();
		  return count;
	  }
	            
	  // Updating single ToDo
	  public int updateTodo(Todo todo) {
		  
		ContentValues values = new ContentValues();
	    values.put(allColumns[1], todo.getItem()); // item
	    values.put(allColumns[2], todo.getPriority()); // priority
	    values.put(allColumns[3], todo.getDueDate().toString()); // dueDate
	    //values.put(allColumns[4], todo.getCreatedDateTime().toString()); //creation
	    //values.put(allColumns[5], todo.getModifiedDateTime().toString()); //modification
       
	    int updateId = database.update(TodoSQLiteHelper.TABLE_TODO, 
	    		values, 
	    		TodoSQLiteHelper.COLUMN_ID + " = ? ", 
	    		new String[] { String.valueOf(todo.getId()) });
	    
	    return updateId;
	  }
	   
	  // Deleting single ToDo
	  public void deleteTodo(Todo todo) {
		  int id = todo.getId();
		    System.out.println("todo deleted with id: " + id);
		    database.delete(TodoSQLiteHelper.TABLE_TODO, TodoSQLiteHelper.COLUMN_ID
		        + " = " + id, null);
	  }
	  
	  private Todo cursorToToDo(Cursor cursor) {
	    Todo todo = new Todo(
	    		cursor.getInt(0),
	    		cursor.getString(1),
	    		cursor.getInt(2),
	    		cursor.getString(3)
	    		//cursor.getString(4),
	    		//cursor.getString(5)
	    		);
	   
	    return todo;
	  }
	} 
