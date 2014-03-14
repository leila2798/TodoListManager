package il.ac.huji.todolist;

import java.util.Calendar;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_todo_item);
		
		Button btnOK = (Button) findViewById(R.id.btnOK);
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnOK.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	EditText edtNewItem = (EditText)findViewById(R.id.edtNewItem);
	        	DatePicker dueDatePicker = (DatePicker)findViewById(R.id.datePicker);
	        	int day = dueDatePicker.getDayOfMonth();
	            int month = dueDatePicker.getMonth();
	            int year =  dueDatePicker.getYear();

	            Calendar calendar = Calendar.getInstance();
	            calendar.set(year, month, day);
	            
	        	//extras Date dueDate, String title
	        	
	        	Intent result = new Intent(); 
	        	result.putExtra("title", edtNewItem.getText().toString());
	        	//TODO: can't getDate Extra!!!! so transfer it as long!
	        	result.putExtra("dueDate", calendar.getTime().getTime());
	        	setResult(RESULT_OK, result); 
	        	finish();
	        }
	    });
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	Intent result = new Intent(); 
	        	setResult(RESULT_CANCELED, result); 
	        	finish();
	        }
	    });
	}

	

}
