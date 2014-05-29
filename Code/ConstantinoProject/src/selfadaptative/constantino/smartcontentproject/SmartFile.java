/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 * 
 * MOST IMPORTANT CLASS
 * The smartdocument
 * contains all functions for encrypting/decryption
 * 
 * you ABSOLUTELY need commons-codec-1.9 for AES to work
 */

package selfadaptative.constantino.smartcontentproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

//imported to do the encryption - jar are in libs/commons-codec-1.9
import org.apache.commons.codec.binary.Base64; //if you don't have this, it won't work!

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.Toast;

public class SmartFile implements Serializable {
	
	private static final String SALT = "smartContent2014";
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String contentEncoded;
	private String contentClear;
	
	private ArrayList<Policy> policies;

	private boolean action_write;
	//private Document xml_tree;

	//---------------------------------------------------------
  	// Constructors
  	//---------------------------------------------------------

	public void initialize(){
		this.title = "";
		this.contentEncoded = "";
		this.contentClear = "";
		this.policies = new ArrayList<Policy>();
		this.action_write = true;
		//this.xml_tree = null;
	}
	
	//creates new file
	public SmartFile(){
		this.initialize();
	}
	
	//loads file from file - either draft or complete smart doc
	@SuppressWarnings("resource")
	public SmartFile(String filename, Activity ctx){
			
			this.initialize();
			this.setTitle(filename.split("\\.")[0].trim());
			
			//String ret = "";

		    try {
		    	File file = FileManager.getFileFromDir(ctx, filename);
		    	
		        InputStream inputStream = new FileInputStream(file);

		        if ( inputStream != null ) {
		            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		            String receiveString = "";
		            String[] elements = null;

		            //first line - verify if a smartdocument 
		            receiveString = bufferedReader.readLine();
		            if (receiveString.equals(ctx.getResources().getString(R.string.prologue_file)) || receiveString.equals(ctx.getResources().getString(R.string.prologue_draft))){
		            	
		            	//if a draft, everything below is the content
		            	if (receiveString.equals(ctx.getResources().getString(R.string.prologue_draft))){
		            		while ((receiveString = bufferedReader.readLine()) != null) {
		            			this.contentClear += receiveString;
		            		}
		            	}
		            	//if a complete smart document
		            	else{
		            		//second line, the action type
		            		receiveString = bufferedReader.readLine();
		            		if (receiveString.equals(ctx.getResources().getString(R.string.policy_action_type_read)))
		            			this.setAction_write(false);
	            			else
	            				this.setAction_write(true);
		            		
		            		//next lines, context needed - until "content"
		            		boolean stillPolicies = true;		            		
		            		while (stillPolicies){
		            			receiveString = bufferedReader.readLine();
		            			if (receiveString.equals(ctx.getResources().getString(R.string.content_separator)))
		            				stillPolicies = false;
		            			else{
			            			elements = receiveString.split("\\s");
			            			
			            			//add policy
			            			if (elements.length > 1)
			            				this.policies.add(new Policy(EnumPolicyType.valueOf(elements[0]), elements[1]));
			            			else
			            				this.policies.add(new Policy(EnumPolicyType.valueOf(elements[0])));
		            			}
		            		}
		            		
		            		//following lines, encoded content
		            		while ((receiveString = bufferedReader.readLine()) != null) {
		            			this.contentEncoded += receiveString;
		            		}
		            		
		            	}
		            	
		            }
		            else
		            	throw new Exception("Not a smartdocument!");
		            
		            inputStream.close();
		        }
		    }catch (Exception e){
		    	Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
		    	this.initialize(); // erase everything...
		    }
		
	}
	
	//---------------------------------------------------------
  	// Funtions for file management
  	//---------------------------------------------------------
    
	public void saveTheFile(Activity ctx){

		String extension;
		String writtenContent;
		
		if (policies.isEmpty()){
			extension = ctx.getResources().getString(R.string.extension_draft);
			writtenContent = this.saveDraft(ctx);
		}
		else{
			extension = ctx.getResources().getString(R.string.extension_file);
			this.encodeFile(ctx); //recall encode in case not done...
			writtenContent = this.saveSmartDocument(ctx);
			
			//erase draft if one of same name
			File file = new File(FileManager.getAlbumStorageDir(ctx) + "/" + this.getTitle() + ctx.getResources().getString(R.string.extension_draft));
        	file.delete();
		}
			
		try{
			FileWriter writer = new FileWriter(FileManager.getAlbumStorageDir(ctx) + "/" + this.getTitle() + extension, false);
			writer.write(writtenContent);
			writer.flush();
			writer.close();
		}
		catch(Exception e){
			Toast.makeText(ctx, "Error while writing", Toast.LENGTH_SHORT).show();
		}
	}
	
	public String saveDraft(Activity ctx){	
		String content = ctx.getResources().getString(R.string.prologue_draft) + "\n";
		content += this.getContentClear();
		return content;
	}
	
	public String saveSmartDocument(Activity ctx){
		String content = ctx.getResources().getString(R.string.prologue_file) + "\n";
		
		//policy
		if (this.isAction_write())
			content += ctx.getResources().getString(R.string.policy_action_type_write);
		else
			content += ctx.getResources().getString(R.string.policy_action_type_read);
		content += "\n";
		
		//different contexts needed
		for (int i=0;i<this.getPolicies().size();i++){
			content += this.getPolicies().get(i).getType().toString() + " " + this.getPolicies().get(i).getProperty() + "\n";
		}
		
		//encoded content
		content += ctx.getResources().getString(R.string.content_separator) + "\n";
		content += this.getContentEncoded();
		return content;
	}
	
	//---------------------------------------------------------
  	// Functions for encrypting data
  	//---------------------------------------------------------
	
	@SuppressLint("TrulyRandom")
	public String encrypt(Activity ctx, String text, String password){
		
		String encryptedString = "";
		
		try{
			//hash the key
			byte[] key = this.hashKey(password);
			
			//AES encryption with hashed key
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encrypted = Base64.encodeBase64(cipher.doFinal(text.getBytes()));
			encryptedString = new String(encrypted);
		}
		catch (Exception e){
			Toast.makeText(ctx, "Error while encrypting!", Toast.LENGTH_SHORT).show();
		}
		
		return encryptedString;
	} 
	
	public String decrypt(Activity ctx, String encryptedString, String keyGiven){
		String decryptedString = "";
		try{
			//hash the key
			byte[] key = this.hashKey(keyGiven);
			
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			decryptedString = new String(cipher.doFinal(Base64.decodeBase64(encryptedString.getBytes()))); //best line right here ;-)
		}
		catch (Exception e){
			//Toast.makeText(ctx, "Could not decrypt file", Toast.LENGTH_SHORT).show();
			//DO SOMETHING ELSE?
		}
		return decryptedString;
	}
	
	//Convert key to SHA-256 hash
	private byte[] hashKey(String password){
		//password = "bidule";
		byte[] key = null;
		
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(password.getBytes());
			key = new byte[16];
			System.arraycopy(digest.digest(), 0, key, 0, key.length);
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		return key;
		
	}
	
	//---------------------------------------------------------
  	// Functions for encoding/decoding file
  	//---------------------------------------------------------
	
	//compute secret key
	public String computeKey(){		
		String action = Boolean.toString(this.isAction_write());
		String policies = "";
		for (int i=0;i<this.getPolicies().size();i++){
			policies += this.getPolicies().get(i).getType().toString() + this.getPolicies().get(i).getProperty();
		}
		String context = "";
		for (int i=0;i<this.getPolicies().size();i++){
			context += this.getPolicies().get(i).getContext();
		}
		
		String key = SALT + action + policies + context;
		return key;
	}
	
	//generate encoded content
	public void encodeFile(Activity ctx){
		String secretKey = this.computeKey();
		this.setContentEncoded(this.encrypt(ctx, this.getContentClear(), secretKey));
	}
	
	//generate decoded content
	public boolean decodeFile(Activity ctx){
		if (!this.policies.isEmpty()){
			this.setContentClear("");
			String secretKey = this.computeKey();
						
			String content = this.decrypt(ctx, this.getContentEncoded(), secretKey);
			this.setContentClear(content);
		}
		
		if (!this.getContentClear().equals(""))
			return true;
		else
			return false;
	}

	//---------------------------------------------------------
  	// Policies
  	//---------------------------------------------------------
   	
	//Add policy
	public void addPolicy(Policy pol){
		this.policies.add(pol);
	}
	
	//Removes policy
	public boolean removePolicy(EnumPolicyType type){
		boolean removed = false;
		
		for (int i=0; i<policies.size(); i++){
			if (this.policies.get(i).getType() == type){
				this.policies.remove(i);
				removed = true;
				break;
			}
		}
		
		return removed;
	}
	
	//---------------------------------------------------------
  	// Getters and setters
  	//---------------------------------------------------------
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentEncoded() {
		return contentEncoded;
	}

	public void setContentEncoded(String contentEncoded) {
		this.contentEncoded = contentEncoded;
	}

	public String getContentClear() {
		return contentClear;
	}

	public void setContentClear(String contentClear) {
		this.contentClear = contentClear;
	}

	public ArrayList<Policy> getPolicies() {
		return policies;
	}

	public boolean isAction_write() {
		return action_write;
	}

	public void setAction_write(boolean action_write) {
		this.action_write = action_write;
	}

	/*
	public Document getXml_tree() {
		return xml_tree;
	}

	public void setXml_tree(Document xml_tree) {
		this.xml_tree = xml_tree;
	}
	*/
}
