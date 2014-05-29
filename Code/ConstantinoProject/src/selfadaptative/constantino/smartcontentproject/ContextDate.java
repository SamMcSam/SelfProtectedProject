/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 * 
 * Gives data on the date and time - not used as context
 */

package selfadaptative.constantino.smartcontentproject;

import java.util.Calendar;

public class ContextDate {

	private Calendar c;
	
	public ContextDate(){
	}
	
	public String getDate(){
		c = Calendar.getInstance(); 				
		String date = "" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR);
	
		return date;
	}
	
	public String getTime(){
		c = Calendar.getInstance(); 		
		String time = "" + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
		
		return time;
	}
	
	//functions to threat data???
	
}
