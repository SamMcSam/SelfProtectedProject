/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 *
 * Main activity - file manager
 */

package selfadaptative.constantino.smartcontentproject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class ActivityMain extends ActionBarActivity {

    private static final int OPEN_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getSupportActionBar().setTitle(R.string.app_name);
		
		//ajout de la liste
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new FragmentDirectory()).commit();
		}
	}
    
 	//---------------------------------------------------------
  	// List menu
  	//---------------------------------------------------------
    
    @SuppressLint("ValidFragment")
	public static class FragmentDirectory extends ListFragment {

    	private DirectoryAdapter adapter;
    	private String selectedFile; //name of file selected
		
    	@Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            menu.add(0, OPEN_ID, 0, R.string.menu_open);
            menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            switch(item.getItemId()) {
            	// OPEN FILES
	            case OPEN_ID:
	    			String extension = selectedFile.split("\\.")[1].trim();
	    			if ((extension.equals("smart")) || (extension.equals("draft"))){
	    				Intent i;
	    				if (extension.equals("smart"))
	    					i = new Intent(this.getActivity(), ActivityFileDecode.class);
	    				else
	    					i = new Intent(this.getActivity(), ActivityContent.class);
	    					
	    				i.putExtra(ActivityContent.BUNDLE_FILE, new SmartFile(selectedFile, this.getActivity())); //file open
		            	
		            	//Toast.makeText(this.getActivity(), selectedFile + " selected", Toast.LENGTH_LONG).show();
		            	startActivity(i);
	    			}
	    			else{
	    				Toast.makeText(this.getActivity(), "Can't open '" + selectedFile + "' : file type not supported", Toast.LENGTH_LONG).show();
	    			}	            	
	                return true;
                case DELETE_ID:
                	File file = new File(FileManager.getAlbumStorageDir(this.getActivity()) + "/" + selectedFile);
                	boolean deleted = file.delete();
                	if (deleted)
                		Toast.makeText(getActivity(), "File deleted!", Toast.LENGTH_LONG).show();
                	else
                		Toast.makeText(getActivity(), "Error : could not delete file...", Toast.LENGTH_LONG).show();
                	this.afficher();
                    return true;
            }
            return super.onContextItemSelected(item);
        }
        
    	@Override
		public void onListItemClick(ListView l, View v, int position, long id) {     		
    		selectedFile = (String) getListAdapter().getItem(position);
    		this.getActivity().openContextMenu(l);
		}
	    
	    @Override
		public void onResume() {
	        super.onResume();
	        this.afficher();
	    }
		
	    @SuppressLint("NewApi")
		public void afficher(){
	    	ArrayList<String> item = new ArrayList<String>();
	        
	        //list the files 
	        File file[] = FileManager.getAlbumStorageDir(this.getActivity()).listFiles();
	        for (int i=0; i < file.length; i++)
	        {
	        	item.add(file[i].getName());
	        }
	        	        
	        Collections.sort(item);
	        
	        this.adapter = new DirectoryAdapter(this.getActivity(),R.layout.list_row, item);
	        this.setListAdapter(this.adapter);  
	        registerForContextMenu(this.getView());
	    }
	    
	}
	
	//---------------------------------------------------------
	// Option Menu
	//---------------------------------------------------------
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		int id = item.getItemId();
		if (id == R.id.action_about) {
	        i = new Intent(this, ActivityAbout.class);
			startActivity(i);
		}
		else if (id == R.id.action_help) {
			i = new Intent(this, ActivityHelp.class);
			startActivity(i);
		}
		else if (id == R.id.action_new) {
			i = new Intent(this, ActivityContent.class);
			i.putExtra(ActivityContent.BUNDLE_FILE, new SmartFile()); //new file created
	        startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

}
