package il.ac.huji.todolist;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {
	ArrayList<String> items;
	TodoListManagerAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		items = new ArrayList<String>();
		listAdapter = new TodoListManagerAdapter(this, items);
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
	            EditText edtNewItem = (EditText)findViewById(R.id.edtNewItem);
	            
	            items.add(edtNewItem.getText().toString());
	            listAdapter.notifyDataSetChanged();
	            
	            edtNewItem.setText("");
	            edtNewItem.requestFocus();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	// Creating a context Menu when the user long click on an item
	  @Override
	  public void onCreateContextMenu(ContextMenu menu, View v,
	          ContextMenuInfo menuInfo) {
	      super.onCreateContextMenu(menu, v, menuInfo);
	      AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;
	      menu.setHeaderTitle(listAdapter.getItem(aInfo.position));
	      menu.add(1, 1, 1, "Delete");
	  }

	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
		  switch(item.getItemId()) 
		  { 
		  case 1: 
			  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		      items.remove(info.position);
		      listAdapter.notifyDataSetChanged();
		      return true; 
		  default: 
			  return super.onContextItemSelected(item); 
		  } 
		 
	  }
}
