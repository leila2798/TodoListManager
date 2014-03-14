package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoListManagerAdapter extends ArrayAdapter<ListItem>{
	private ArrayList<ListItem> values;

	  public TodoListManagerAdapter(Context context, ArrayList<ListItem> values) {
	    super(context, R.layout.listrow, values);
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.listrow, null);
		TextView txtTodo = (TextView)view.findViewById(R.id.txtTodoTitle);
		txtTodo.setText(values.get(position).toDoText);
		TextView txtDueDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		try{
			txtDueDate.setText(new SimpleDateFormat("MM/dd/yyyy",Locale.getDefault()).format(values.get(position).dueDate));
		}
		catch (Exception e){
			txtDueDate.setText("No due date");
		}
    // alternate colors
		if (values.get(position).isOverdue()) {
			txtTodo.setTextColor(Color.RED);  
		} else {
			txtTodo.setTextColor(Color.BLACK);  
		}

	    return view;
	  }
	  
	  public ListItem getListItem(int position){
		  if (position <0||position >=values.size())
			  return null;
		  return values.get(position);  
	  }
	  
} 

