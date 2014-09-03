package com.AndriodBootCamp.mytodoapp;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

public class TodoArrayAdapter extends ArrayAdapter<Todo> {

	 private final List<Todo> list;
	 private final Activity context;

	  public TodoArrayAdapter(Activity context, List<Todo> list) {
	    super(context, R.layout.image_row, list);
	    this.context = context;
	    this.list = list;
	  }

	  static class ViewHolder {
		protected ImageView prButton;
	    protected TextView todoItem;
	    protected TextView dueDateAlarm;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View view = null;
	    Typeface tvFont;
        Typeface tvTimeFont;
        Todo a_todo = list.get(position);
        
	    if (convertView == null) {
	      LayoutInflater inflator = context.getLayoutInflater();
	      view = inflator.inflate(R.layout.image_row, null);
	      final ViewHolder viewHolder = new ViewHolder();
	      viewHolder.prButton = (ImageView)view.findViewById(R.id.icon);
	      viewHolder.todoItem = (TextView) view.findViewById(R.id.label);
	      viewHolder.dueDateAlarm = (TextView) view.findViewById(R.id.dueTime);
	      
	      viewHolder.dueDateAlarm.setOnClickListener(new View.OnClickListener() {
				
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
			        	  viewHolder.dueDateAlarm.setText( selectedHour%12 + ":" + selectedMinute + " " + ampm);
			          }
			      }, hour, minute, true);
			      mTimePicker.setTitle("Select todo item Due Time");
			      mTimePicker.show();
	            }
	          });
	      
	      view.setTag(viewHolder);
	      viewHolder.prButton.setTag(a_todo);
	    } else{   
	    	view = convertView;	     
	    	((ViewHolder) view.getTag()).prButton.setTag(a_todo);
	    }
	      ViewHolder holder = (ViewHolder) view.getTag();
	      //show different color icon according to priority
	      switch(a_todo.getPriority()){
	      case 1:
	    	  holder.prButton.setImageResource(R.drawable.tp1);
	    	  break;
	      case 2:
	    	  holder.prButton.setImageResource(R.drawable.tp2);
	    	  break;
	      case 3:
	    	  holder.prButton.setImageResource(R.drawable.tp3);
	    	  break;
	      case 4:
	    	  holder.prButton.setImageResource(R.drawable.tp4);
	    	  break;
	      case 5:
	    	  holder.prButton.setImageResource(R.drawable.tp5);
	    	  break;
	      }
	      
	      holder.todoItem.setText(a_todo.getItem());
	      AssetManager assetMgr = parent.getContext().getAssets();
	      tvFont = Typeface.createFromAsset(assetMgr, "fonts/calligraffiti.ttf");
	      //tvFont = Typeface.createFromAsset(assetMgr, "fonts/devroye.ttf");
	      holder.todoItem.setTypeface(tvFont);
	      holder.todoItem.setTextColor(Color.BLACK);
	      
	      String dueDate = TodoUtils.GetDueTimeStringForDisplay(a_todo.getDueDate());  	      
	      tvTimeFont = Typeface.createFromAsset(assetMgr, "fonts/dsdigit.ttf");
	      holder.dueDateAlarm.setTypeface(tvTimeFont);
	      int acolor = Color.argb( 255 , 102, 255, 0 );
	      holder.dueDateAlarm.setTextColor(acolor);
	      holder.dueDateAlarm.setText(dueDate);	    
	    
	    return view;
	} 
}