package net.noratargo.siJACK.interfaces;

/**
 * A class, implementing this interface is able to create an object out of the given parameter.
 * <p>
 * There might lateron be additional methods for creating instances of a specific type with other Objecttypes.
 * 
 * @author HMulthaupt
 * @param <T>
 *            The type, for that the implementing class creates new instances.
 */
public interface ParameterInstanciator<T> {

	/**
	 * Returns the class-object representing the type, that the implementing class can instantiate.
	 * 
	 * @return the class-object representing the type, that the implementing class can instantiate.
	 */
	public Class<T> getInstanceType();

	/**
	 * Creates a NEW instance from the given string. The given string is to be interpreted as a textual representation
	 * of parameters to instantiate the new istance.
	 * <p>
	 * It might be, that both parameters are <code>null</code>. In this case, <code>null</code> <b>may</b> be returned
	 * (but this is not necessary). (Since it is either wanted, or caused by blind stupidity).
	 * <p>
	 * If both parameters are given, it depends on the classes implementation, which of the two you will prefer. For
	 * <b>immutable objects</b> it is legal, to return the provided one (if it is not <code>null</code>). For immutable
	 * objects you have to create a new instance. Whether you then prefer the <code>params</code> or the
	 * <code>defaultValue</code> parameter, may depend on your personal preferences.<br />
	 * <b>In any case, if both parameters are not <code>null</code>, they both represent the same Object.</b>
	 * <p>
	 * If you are planning to clone the given <code>defaultObject</code>, keep in mind, that a deep-copy might be the
	 * preferred way to provide trouble, while a shalow copy will spare you some time and memory.
	 * 
	 * @param params
	 *            A textual representation of parameters, to be used for creating a new instance. Can be
	 *            <code>null</code>, if a default value has been present.
	 * @param currentValue
	 *            The originally created default value. Can be <code>null</code>, if a default value is not yet created.
	 * @return The new instance. If the returned object is mutable, then it is REUQIRED to be a NEW instance, so that
	 *         modifications to this instance do not interference with other instances.
	 */
	public T createNewInstance(String params, T currentValue);
}
