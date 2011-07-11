package net.noratargo.siJACK.interfaces;

import net.noratargo.siJACK.ParameterPrefixNamePair;
import net.noratargo.siJACK.annotations.DefaultValue;
import net.noratargo.siJACK.annotations.Description;
import net.noratargo.siJACK.annotations.Name;

import java.util.Set;

public interface ImmutableParameter<T> {

	/**
	 * Returns this parameter's current value. If <code>null</code> is returned, you should check the
	 * {@link #hasDefaultValue()} method in order to see, whether a default parameter is available for this parameter.
	 * 
	 * @return
	 */
	public T getDefaultValue();

	public T getCurrentValue();

	/**
	 * Retruns this parameter's type. This is always available.
	 * 
	 * @return this parameter's type.
	 */
	public Class<T> getValueClassType();

	/**
	 * Returns all prefix and name combinations, under that this parameter is accessable.
	 * @return
	 */
	public Set<ParameterPrefixNamePair> getParameterPrefixNamePairs();

	public String getDefaultPrefix();

	/**
	 * Returns the default name for this property.
	 * <p>
	 * An empty string will be returned, if this has been specified as default name, OR if the default name has neither
	 * been set, nor could be determined (this is the case for a construcotr's parameter without a {@link Name}
	 * annotation).
	 * 
	 * @return The default Name for this parameter.
	 */
	public String getDefaultName();

	/**
	 * Returns the description for this parameter. If this parameter has no description then an empty string will be
	 * returned. A description is only available, if the parameter is annotatied with the {@link Description}
	 * annotation.
	 * 
	 * @return This parameter's description.
	 */
	public String getDescription();

	/**
	 * Returns <code>true</code> if a default value has been set for this parameter. Returns <code>false</code>, if This
	 * is this parameter belongs to a constructor, that has no {@link DefaultValue} annotation. In any other case, this
	 * metod will return <code>true</code>.
	 * 
	 * @return <code>true</code> if the {@link #getDefaultValue()} Method's return value has a meaning,
	 *         <code>false</code> otherwise.
	 */
	public boolean hasDefaultValue();
}
