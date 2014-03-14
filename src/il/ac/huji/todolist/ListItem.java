package il.ac.huji.todolist;

import java.util.Calendar;
import java.util.Date;

public class ListItem {
	private final static String CALL_STR = "Call ";
	public Date dueDate;
	public String toDoText;
	public boolean isCall;
		
	public ListItem(Date date, String text){
		dueDate = date;
		toDoText = text;
		isCall = text.startsWith(CALL_STR);
	}
	
	public boolean isOverdue(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    
		return (dueDate!=null)&&(dueDate.before(calendar.getTime()));
	}
	
	public String getPhone(){
		return (isCall)?toDoText.replace(CALL_STR, ""):"";
	}
}
