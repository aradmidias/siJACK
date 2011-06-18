package net.noratargo.applicationFramework.interfaces;

import net.noratargo.applicationFramework.Parameter;

import java.util.Set;

public interface ConfigurationManager {

	public void setParameter(String name, String value);

//	public void setParameter(CharSequence name, CharSequence value);
	
	public void setParameter(String prefix, String name, String value);

//	public void setParameter(CharSequence prefix, CharSequence name, CharSequence value);
	
	/**
	 * Returns the String, representing the Seperator, used to seperate a parameter's prefix and name.
	 * @return
	 */
	public String getPrefixNameSeperator();
	
	/**
	 * Returns an immutable set of all currently defined Parameters.
	 * 
	 * @return an immutable set of all currently defined Parameters. This set may be <b>immutable</b>.
	 */
	public Set<Parameter<?>> getParameters();
}
