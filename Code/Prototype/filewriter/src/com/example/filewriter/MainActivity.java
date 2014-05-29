package com.example.filewriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	//pour affichage list
	private ListView lv;
	ArrayList<String> item;
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		 lv = (ListView)findViewById(R.id.list);
		
		if (savedInstanceState == null) {
			//recharge
		}
		
		//POUR RETOUR EN ARRIERE
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//+ ajout parent dans manifest
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
			if (id == R.id.action_ajout) {
				createTheFile("test");
			}
			if (id == R.id.action_settings) {
				//createTheFile("test");
			}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //saveState();
        //outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        item = new ArrayList<String>();
        
        //list the files
        File file[] = this.getAlbumStorageDir("MySmartContent").listFiles();
        for (int i=0; i < file.length; i++)
        {
        	item.add(file[i].getName());
        }
        
        String[] from =  item.toArray(new String[item.size()]);
        
        this.adapter = new ArrayAdapter<String>(this,R.layout.list_row, R.id.text1, from);
        lv.setAdapter(this.adapter);  
        
    }
	
	
	// FOR WRITING
	//----------------------------------------------------------

	public void createTheFile(String filename){

		String string = "Hello world!";
		
		try{
			FileWriter writer = new FileWriter(this.getAlbumStorageDir("MySmartContent") + "/" + filename + ".smart", false);
			writer.write(string);
			writer.flush();
			writer.close();
		}
		catch(Exception e){
			Toast.makeText(this, "Error while writing", Toast.LENGTH_SHORT)
	          .show();
		}
		
	}
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	// Get the directory
	@SuppressLint("NewApi")
	public File getAlbumStorageDir(String albumName) {
	    // Get the directory for the user's public pictures directory. 
	    //File file = new File(this.getExternalFilesDir(null), albumName);
	    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), albumName);
	    
	    //creates directory
	    if (!file.mkdirs()) {
	    	if (!file.isDirectory()){
	    		Toast.makeText(this, "Directory not created", Toast.LENGTH_SHORT)
		          .show();
	    	}
	    }
	    return file;
	}
	
}
