package encryptionProto;


import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/// A INCLURE!!!!!!!!!!!!
import org.apache.commons.codec.binary.Base64;

//Symmetric cryptography - uses same key in encrypting and decrypting

public class EncryptedEntity {

	
	private String encryptedString;
	
	public EncryptedEntity(String text, String password){
		
		try{
			//hash the key
			byte[] key = this.hashKey(password);
			
			//AES encryption with hashed key
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			this.encryptedString = Base64.encodeBase64String(cipher.doFinal(text.getBytes()));
		}
		catch (Exception e){
			System.out.println(e);
			System.out.println("Error while encrypting!");
			System.exit(1);
		}
		
	} 
	
	public String decrypt(String keyGiven){
		String decryptedString = "**wrong key!**";
		try{
			//hash the key
			byte[] key = this.hashKey(keyGiven);
			
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			decryptedString = new String(cipher.doFinal(Base64.decodeBase64(this.encryptedString)));
		}
		catch (Exception e){
			System.out.println("Error while decrypting!");
		}
		return decryptedString;
	}
	
	public String getEncryptedString(){
		return this.encryptedString;
	}
	
	//Convert key to SHA-256 hash
	private byte[] hashKey(String password){
		
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
	
}
