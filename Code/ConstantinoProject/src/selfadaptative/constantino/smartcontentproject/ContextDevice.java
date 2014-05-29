/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 * 
 * Gives info on device
 */

package selfadaptative.constantino.smartcontentproject;

public class ContextDevice {

	private String build;
	private String manufacturer;
	
	public ContextDevice(){
		this.manufacturer = android.os.Build.MANUFACTURER;
		this.build = android.os.Build.MODEL;
	}
	
	public String getManufacturer(){
		return manufacturer;
	}
	
	public String getBuild(){
		return build;
	}
}
