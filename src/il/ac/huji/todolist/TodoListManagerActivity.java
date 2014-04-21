package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
//import com.parse.ParseAnalytics;

public class TodoListManagerActivity extends Activity {
	ArrayList<ListItem> items;
	TodoListManagerAdapter listAdapter;
	TodoListDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//parse code
		Parse.initialize(this, "Y9TO7S3OQHrnbtvfo1wA1o24T9g22b1F9TOKXpIQ", "YcCpkMEc8A3WoXuI1N61k6YrF5bzYbJ2ERFn5qSC");

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_todo_list_manager);

		items=new ArrayList<ListItem>();

		listAdapter = new TodoListManagerAdapter(getApplicationContext(), items);
		ListView todoList = (ListView) findViewById(R.id.lstTodoItems);

		todoList.setAdapter(listAdapter);

		/*
		try{
			datasource = new TodoListDataSource(getApplicationContext());
			datasource.open();

			items = datasource.getallItems();
		}
		catch(Exception e){
			//TODO: handle database-related exceptions if needed (currently ignoring)
		}

		 */

		//HERE GOES THE ASYNCTASK
		LoadTodoDBTask atask = new LoadTodoDBTask();
		atask.execute();

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
			Intent intent = new Intent(getApplicationContext(), AddNewTodoItemActivity.class); 
			startActivityForResult(intent, 1); 

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		if(resultCode==RESULT_OK && requestCode==1) {
			// save the new item to the database
			ListItem todo = datasource.addItem(data.getStringExtra("title"), data.getLongExtra("dueDate", -1));

			//save to parse
			ParseObject todoItem = new ParseObject("todo");
			todoItem.put("title", todo.toDoText);
			todoItem.put("dueDate", todo.dueDate.getTime());
			//UNIQUE!!! Used to delete an item
			todoItem.put("sqLiteId", todo.id);
			todoItem.saveInBackground();

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
			//delete from array
			ListItem itemToRemove = items.remove(info.position);
			//delete from SQLite
			datasource.deleteItem(itemToRemove);
			//delete from parse using unique sqlite id
			ParseQuery<ParseObject> query = ParseQuery.getQuery("todo");
			query.whereEqualTo("sqLiteId", itemToRemove.id);
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> todoList, ParseException e) {
					if (e == null&&todoList.size()>0) {
						todoList.get(0).deleteInBackground();
					} 
					//ignoring exception
				}
			});
			/*
			try {
				List<ParseObject> todoList=query.find();
				if (todoList.size()>0) {
		        	todoList.get(0).deleteInBackground();
		        } 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
			}
			 */
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

	@Override
	protected void onDestroy() {
		datasource.close();
		super.onDestroy();
	}

	private class LoadTodoDBTask extends AsyncTask<Void, ArrayList<ListItem>, Void> {
		private final int ITEMS_NUM = 10;
		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Void...voids) {
			try{
				datasource = new TodoListDataSource(getApplicationContext());
				datasource.open();
				int totItemsCnt = datasource.getAllItemsCount();
				for (int i = 0; i < totItemsCnt; i+=ITEMS_NUM)
				{
					publishProgress(datasource.getItems(ITEMS_NUM, i));
				}	
			}
			catch(Exception e){
				//TODO: handle database-related exceptions if needed (currently ignoring)
			}	
			/*finally{
				datasource.close();
			}*/
			return null;
		}

		protected void onProgressUpdate(ArrayList<ListItem>... progress) {
			items.addAll(progress[0]);
			listAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			setProgressBarIndeterminateVisibility(false);
			
		}
	}
}
