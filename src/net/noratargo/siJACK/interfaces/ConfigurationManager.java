package net.noratargo.siJACK.interfaces;

/**
 * A ConfigurationManager is capable of setting the values of configureable fields and constructor's parameters to the
 * specified values, while identifying them by their prefix and name.
 * 
 * @author HMulthaupt
 */
public interface ConfigurationManager {

	public void setValue(String name, String value);

	public <T> void setValue(String prefix, String name, String value);

	/**
	 * Returns the String, representing the Seperator, used to seperate a parameter's prefix and name.
	 * 
	 * @return
	 */
	public String getPrefixNameSeperator();
}
