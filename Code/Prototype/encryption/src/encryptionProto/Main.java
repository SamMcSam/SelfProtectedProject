package encryptionProto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static String getInput(String text){
		//  prompt the user
		System.out.print(text);
		
		//  open up standard input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String textReturned = null;
		
		//  read the username from the command-line; need to use try/catch with the
		//  readLine() method
		try {
			textReturned = br.readLine();
		} catch (IOException ioe) {
			System.out.println("IO error");
		    System.exit(1);
		}
		
		return textReturned;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	  String textToEncrypt = getInput("Enter the text to be encrypted : ");
	  String password = getInput("Enter the password to use as key : ");

      System.out.println("Now encrypting...");
      
      EncryptedEntity encrypted = new EncryptedEntity(textToEncrypt, password);
      System.out.println("Encrypted as : " + encrypted.getEncryptedString());
      
      String password2 = getInput("Decrypt it with this key : ");
      System.out.println("The as : " + encrypted.decrypt(password2));
      
      String password3 = getInput("Decrypt it with this key : ");
      System.out.println("Encrypted as : " + encrypted.decrypt(password3));
	}

}
