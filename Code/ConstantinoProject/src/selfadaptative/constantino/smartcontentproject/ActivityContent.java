/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 * 
 * Content editor and reader, pretty straight forward
 * Some options are not available if readonly
 */

package selfadaptative.constantino.smartcontentproject;

import java.io.Serializable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityContent extends ActionBarActivity {
	
	public static final String BUNDLE_FILE = "bundle_file";
		
    private EditText mTitleText;
    private EditText mBodyText;

    private SmartFile file;
    
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_content);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        //get file from extra
    	this.getExtras();
    	
        this.setTitle();
        
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);

        //listeners
        mTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            	file.setTitle(mTitleText.getText().toString());
            }
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });
        mBodyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            	file.setContentClear(mBodyText.getText().toString());
            }
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });
        
        populateFields();
    }
    
    //---------------------------------------------------------
  	// Activity flow functions
  	//---------------------------------------------------------
    
    //get extras at init
    public void getExtras(){
    	Intent intent = this.getIntent();
        if (intent.hasExtra(BUNDLE_FILE)){
        	this.file = (SmartFile) intent.getSerializableExtra(BUNDLE_FILE);
        }
        else{
        	this.file = new SmartFile(); //create a new one in case bug with extras...
        	Toast.makeText(this, "plouf", Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.saveState(); //to make sure saved modifications
        outState.putSerializable(BUNDLE_FILE, (Serializable) file);
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      this.file = (SmartFile) savedInstanceState.getSerializable(BUNDLE_FILE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //this.file.setAction_write(false);
        this.saveState();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        //redo test!!!!
        populateFields();        
    }
    
    private void saveState(){
    	//get text from fields
        file.setTitle(mTitleText.getText().toString());
        file.setContentClear(mBodyText.getText().toString());
    }
    
    private void populateFields() {
    	this.setTitle();
    	
    	mTitleText.setText(file.getTitle());
        mBodyText.setText(file.getContentClear());
        
        //if possible to edit
        if (!this.file.isAction_write()){
        	mTitleText.setKeyListener(null);
        	mTitleText.setEnabled(false);
        	mBodyText.setKeyListener(null);
        	mBodyText.setEnabled(false);
        }
    }
    
    //when come back from encoding page
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                this.finish();
            }
            if (resultCode == RESULT_CANCELED) {
                //nothing?
            }
        }
    }
    
 	//---------------------------------------------------------
  	// Option Menu
  	//---------------------------------------------------------
    
    public void setTitle(){
    	if (!this.file.getContentEncoded().equals("")){
    		if (this.file.isAction_write())
    			super.setTitle(R.string.edit_content);
        	else
        		super.setTitle(R.string.read_content);
    	}
    	else
    		super.setTitle(R.string.new_content);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	//menu has less options if not writable
    	if (this.file.isAction_write())
    		getMenuInflater().inflate(R.menu.edit, menu);
    	else
    		getMenuInflater().inflate(R.menu.read, menu);
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i;        
    	int id = item.getItemId();
    	if (id == android.R.id.home){
    		this.finish();
            return true;
    	}
    	else if (id == R.id.action_encode) {
	        i = new Intent(this, ActivityFileEncode.class);
	        i.putExtra(ActivityContent.BUNDLE_FILE, this.file); //send file in extra
	        startActivityForResult(i,1);
		}
    	else if (id == R.id.action_save) {
    		//verify if already saved once
    		file.saveTheFile(this);
		}
    	else if (id == R.id.action_clear) {
    		file = new SmartFile();
    		populateFields();     
		}
		else if (id == R.id.action_help) {
			i = new Intent(this, ActivityHelp.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}
    
}
