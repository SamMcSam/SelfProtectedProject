/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 * 
 * Contains functions to manage the directory where files are saved
 */

package selfadaptative.constantino.smartcontentproject;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

public class FileManager {
	
	//UPDATE !! now files are in /MySmartContent and not /Documents/MySmartContent because Documents is NOT available in API below level 19!!!!!
	public static final String ALBUM_NAME = "MySmartContent";
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	// Get the directory
	@SuppressLint("NewApi")
	public static File getAlbumStorageDir(Activity activity) {
	    //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), ALBUM_NAME); //api level 19
	    File file = new File(Environment.getExternalStoragePublicDirectory(""), ALBUM_NAME); 
	    
	    //creates directory if not already
	    if (!file.mkdirs()) {
	    	if (!file.isDirectory()){
	    		Toast.makeText(activity, "Directory not created", Toast.LENGTH_SHORT)
		          .show();
	    	}
	    }
	    return file;
	}
	
	//Get a file
	public static File getFileFromDir(Activity activity, String filename){
		return new File(getAlbumStorageDir(activity), filename);
	}

}
