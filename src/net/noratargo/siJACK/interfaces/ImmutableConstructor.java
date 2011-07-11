package net.noratargo.siJACK.interfaces;

import java.util.List;

public interface ImmutableConstructor<T> {

	public String getDescription();

	public Class<T> getDeclaringClass();

	/**
	 * The order of the elements also represents the order of the constructor's parameters.
	 * 
	 * @return
	 */
	public List<ImmutableParameter<?>> getParameters();

	public boolean isParital();

	public boolean isDefault();
}
