package com.AndriodBootCamp.mytodoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoSQLiteHelper extends SQLiteOpenHelper {

	  //Database name
	  private static final String DATABASE_NAME = "todos.db";
	  //Database version
	  private static final int DATABASE_VERSION = 1;  
	  
	  //Table name
	  public static final String TABLE_TODO = "todo";  
	  
	  //Table columns
	  public static String COLUMN_ID = "id";
	  public static String COLUMN_ITEM = "item";
	  public static String COLUMN_PRIORITY = "priority";
	  public static String COLUMN_DUEDATE ="dueDate";
	  //public static String COLUMN_CREATEDDATEIME = "createdDateTime";
	  //public static String COLUMN_MODIFIEDDATETIME = "modifiedDateTime";
	  
	  //constructor
	  public TodoSQLiteHelper(Context context) {
		    super(context, DATABASE_NAME, null, DATABASE_VERSION);
		  }
	
	  // Database creation SQL statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_TODO + "(" 
		  + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_ITEM + " text not null, "
	      + COLUMN_PRIORITY + " int not null, "
	      + COLUMN_DUEDATE + " text not null); ";
	      //+ COLUMN_CREATEDDATEIME + " text not null, "
	      //+ COLUMN_MODIFIEDDATETIME + " text not null);";
	  
	  // Create table
	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }
	
	  //Upgrade
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(TodoSQLiteHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
	    onCreate(db);
	  }

} 
