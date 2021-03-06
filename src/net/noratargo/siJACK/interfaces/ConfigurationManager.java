package net.noratargo.siJACK.interfaces;

import java.util.Set;

import javax.naming.ConfigurationException;

/**
 * A ConfigurationManager is capable of setting the values of configureable fields and constructor's parameters to the
 * specified values, while identifying them by their prefix and name.
 * 
 * @author HMulthaupt
 */
public interface ConfigurationManager {

	/**
	 * Specifies the value for a parameter in a single string. The string should contain the prefix, the name and the
	 * value. The delimiters between prefix, name and value are implementation dependent.
	 * 
	 * @param pns
	 *            The string containing prefix, name and value.
	 * @throws ConfigurationException 
	 */
	public void setValue(String pns) throws ConfigurationException;

	public void setValue(String name, String value) throws ConfigurationException;

	public <T> void setValue(String prefix, String name, String value) throws ConfigurationException;

	/**
	 * Returns the String, representing the Seperator, used to seperate a parameter's prefix and name.
	 * 
	 * @return
	 */
	public String getPrefixNameSeperator();

	/**
	 * Returns a set of all Fields, that can be configured. This set does not contain information about the parameters
	 * of the known constructors, although there may be {@link ImmutableParameter}sin this set, that are also in one or
	 * more of the {@link ImmutableConstructor#getParameters()} Lists.
	 * 
	 * @return
	 */
	public Set<ImmutableParameter<?>> getFields();

	/**
	 * Returns a set of all known constructors.
	 * 
	 * @param <T>
	 * @return
	 */
	public <T> Set<ImmutableConstructor<?>> getConstructors();

}
