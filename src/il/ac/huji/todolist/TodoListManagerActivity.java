package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {
	ArrayList<ListItem> items;
	TodoListManagerAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		items = new ArrayList<ListItem>();
		listAdapter = new TodoListManagerAdapter(getApplicationContext(), items);
		ListView todoList = (ListView) findViewById(R.id.lstTodoItems);
		
		todoList.setAdapter(listAdapter);
		
		registerForContextMenu(todoList);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menuItemAdd:
	            
	        	//TODO: start new activity to add an item
	        	Intent intent = new Intent(getApplicationContext(), AddNewTodoItemActivity.class); 
	        	//TODO: check the request code
	        	startActivityForResult(intent, 1); 
	        	 
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		 if(resultCode==RESULT_OK && requestCode==1) {
	       		 String title = data.getStringExtra("title");
	       		 Date dueDate = new Date(data.getLongExtra("dueDate", -1));
	       		 ListItem todo = new ListItem(dueDate, title);
	       		 items.add(todo);
	       		 listAdapter.notifyDataSetChanged();
		 }       		 
    } 
	
	// Creating a context Menu when the user long click on an item
	  @Override
	  public void onCreateContextMenu(ContextMenu menu, View v,
	          ContextMenuInfo menuInfo) {
	      super.onCreateContextMenu(menu, v, menuInfo);
	      AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;
	      ListItem item = listAdapter.getListItem(aInfo.position);
	      menu.setHeaderTitle(item.toDoText);
	      menu.add(1, 1, 1, "Delete");
	      if (item.isCall)
	    	  menu.add(1,2,2, "Call");
	  }

	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
		  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		  switch(item.getItemId()) 
		  { 
		  case 1: 
			  items.remove(info.position);
		      listAdapter.notifyDataSetChanged();
		      return true;
		  case 2:
			  Intent callIntent = new Intent(Intent.ACTION_DIAL);
			  callIntent.setData(Uri.parse("tel:"+listAdapter.getListItem(info.position).getPhone()));
	          startActivity(callIntent);
		  default: 
			  return super.onContextItemSelected(item); 
		  } 
		 
	  }
}
