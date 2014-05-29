/* Samuel Constantino
 * Self-adaptative project
 * spring 2014
 * ----
 * 
 * Policy class (is serializable to be able to be sent from one activity to another)
 */

package selfadaptative.constantino.smartcontentproject;

import java.io.Serializable;

public class Policy implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EnumPolicyType type;
	private String property;
	private String context;

	public Policy(){
		this.type = null;
		this.property = "";
		this.context = "";
	}
	
	public Policy( EnumPolicyType type ){
		this.type = type;
		this.property = "";
		this.context = "";
	}
	
	public Policy( EnumPolicyType type, String property ){
		this.type = type;
		this.property = property;
		this.context = "";
	}
	
	public String getProperty(){
		return property;
	}
	
	public EnumPolicyType getType(){
		return type;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String str) {
		this.context = str;
	}
	
}