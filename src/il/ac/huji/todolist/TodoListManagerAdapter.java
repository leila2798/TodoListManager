package il.ac.huji.todolist;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoListManagerAdapter extends ArrayAdapter<String>{
	private final ArrayList<String> values;

	  public TodoListManagerAdapter(Context context, ArrayList<String> values) {
	    super(context, R.layout.listrow, values);
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.listrow, null);
		TextView txtTodo = (TextView)view.findViewById(R.id.txtTodoItem);
		txtTodo.setText(values.get(position));
    // alternate colors
		if (position % 2 == 1) {
			txtTodo.setTextColor(Color.RED);  
		} else {
			txtTodo.setTextColor(Color.BLUE);  
		}

	    return view;
	  }
} 

