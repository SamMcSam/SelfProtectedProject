/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 *
 *Abstract class that defines both encoding and decoding activities
 * (this way communication with dialog fragments are easier...)
 */

package selfadaptative.constantino.smartcontentproject;

import android.support.v7.app.ActionBarActivity;

public abstract class ActivityFile extends ActionBarActivity {
	
	//done boolean is to check/uncheck on the encoding menu according to click on ok/cancel
	 public void eventPassword(boolean done, String password) {} 
	 public void eventGPS(boolean done, int scale) {} 
	 public void eventDevice(boolean done, boolean manufacturer, boolean model) {} 
	 public void eventNetwork(boolean done, boolean currentWifi) {}  //to modify in the future with other types?
}
