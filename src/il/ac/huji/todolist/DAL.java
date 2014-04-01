package il.ac.huji.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DAL extends SQLiteOpenHelper {

	//define constants for DB table definition 
	  public static final String TABLE_TODO = "todo";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_TITLE = "title";
	  public static final String COLUMN_DUE = "due";
	  
	  private static final String DATABASE_NAME = "todo_db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_TODO + "(" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_TITLE
	      + " text not null, "+COLUMN_DUE+" long not null);";

	  public DAL(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
	    onCreate(db);
	  }
}