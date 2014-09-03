package com.AndriodBootCamp.mytodoapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Todo implements Parcelable {
	  private int _id;  //DB primary key
	  private String _item;
	  private int _priority;
	  private String _dueDate;
	  //private Date _createdDateTime;
	  //private Date _modifiedDateTime;
	  

//	  public Todo(int id, String item, int priority, 
//			  Date dueDate) {//, Date createdDateTime, Date modifiedDateTime) {
//		  _id = id;
//		  _item = item;
//		  _priority = priority;
//		  _dueDate = dueDate;
//		  //_createdDateTime = createdDateTime;
//		  //_modifiedDateTime = modifiedDateTime;
//	  }
	  
	  public Todo(int id, String item, int priority, 
			  String dueDate) {//,  String createdDateTime, String modifiedDateTime) {
		  _item = item;
		  _priority = priority;
		  _dueDate = dueDate;
		 //_dueDate = ConvertToDateTime(dueDate);
		  //_createdDateTime = ConvertToDateTime(createdDateTime);
		  //_modifiedDateTime = ConvertToDateTime(modifiedDateTime);
	  }
	  
	  
	  public int getId() {
	    return _id;
	  }

	  public void setId(int id) {
	    this._id = id;
	  }

	  public String getItem() {
	    return _item;
	  }

	  public void setItem(String item) {
	    this._item = item;
	  }

	  public int getPriority() {
		return _priority;
	  }

	  public void setPriority(int priority) {
	    this._priority = priority;
	  }

	  public String getDueDate() {
		return this._dueDate;
	  }

	  public void setDueDate(String dueDate) {
	    this._dueDate = dueDate;
	  }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(_id);
		dest.writeString(_item);
		dest.writeInt(_priority);
		dest.writeString(_dueDate);
	}

	public static final Creator<Todo> CREATOR = 
			new Parcelable.Creator<Todo>() {
	        public Todo createFromParcel(Parcel in) {
	            return new Todo(in);
        }

        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    // constructor that takes a Parcel and gives 
    // an object populated with it's values
    private Todo(Parcel in) {
        _id = in.readInt();
        _item = in.readString();
        _priority = in.readInt();
        _dueDate = in.readString();
    }
//	  public Date getDueDate() {
//		return this._dueDate;
//	  }

//	  public void setDueDate(Date dueDate) {
//	    this._dueDate = dueDate;
//	  }
//	  public Date getCreatedDateTime() {
//	    return _createdDateTime;
//	  }
//
//	  public void setCreatedDateTime(Date createdDateTime) {
//	    this._createdDateTime= createdDateTime;
//	  }
//	  
//	  public Date getModifiedDateTime() {
//	    return _modifiedDateTime;
//	  }
//
//	  public void setModifiedDateTime(Date modifiedDateTime) {
//	    this._modifiedDateTime= modifiedDateTime;
//	  }
} 
