package il.ac.huji.todolist;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TodoListDataSource {
	// Database fields
	  private SQLiteDatabase database;
	  private DAL dbHelper;
	  private String[] allColumns = { DAL.COLUMN_ID,
	      DAL.COLUMN_TITLE, DAL.COLUMN_DUE };

	  public TodoListDataSource(Context context) {
	    dbHelper = new DAL(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public ListItem addItem(String title, long dueDate) {
	    ContentValues values = new ContentValues();
	    values.put(DAL.COLUMN_TITLE, title);
	    values.put(DAL.COLUMN_DUE, dueDate);
	    long insertId = database.insert(DAL.TABLE_TODO, null,
	        values);
	    Cursor cursor = database.query(DAL.TABLE_TODO,
	        allColumns, DAL.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    ListItem newItem = cursorToListItem(cursor);
	    cursor.close();
	    return newItem;
	  }

	  public void deleteItem(ListItem item) {
	    long id = item.id;
	    database.delete(DAL.TABLE_TODO, DAL.COLUMN_ID
	        + " = " + id, null);
	  }

	  public ArrayList<ListItem> getallItems() {
	    ArrayList<ListItem> items = new ArrayList<ListItem>();

	    Cursor cursor = database.query(DAL.TABLE_TODO,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      ListItem item = cursorToListItem(cursor);
	      items.add(item);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return items;
	  }

	  private ListItem cursorToListItem(Cursor cursor) {
	    ListItem item = new ListItem(new Date(cursor.getLong(2)), cursor.getString(1));
	    item.id=cursor.getLong(0);
	    return item;
	  }
}
