package net.noratargo.siJACK.interfaces;

public interface InstantiatorManager {

	/**
	 * @param <T>
	 *            The type of the object, being returned.
	 * @param c
	 *            Represents the type of the field.
	 * @param stringRepresentation
	 *            The textual representation of the value. may be null.
	 * @return The actual object.
	 */
	public <T> T getNewInstanceFor(Class<T> c, String stringRepresentation);

	/**
	 * creates a new instance from the given object.
	 * <p>
	 * If <code>T</code> is immutable, then the <code>originalObject</code> may be returned.
	 * @param targetType TODO
	 * @param originalObject
	 *            The original object to create a new instance from.
	 * 
	 * @param <T>
	 *            The type of the object, being returned.
	 * @return A new instance of the original object. This may be the same object, if <code>originalObject</code> is
	 *         immutable.
	 */
	public <T> T getNewInstanceFrom(Class<T> targetType, T originalObject);

	/**
	 * Adds the specified instantiator to this manager.
	 * 
	 * @param i
	 *            The instantiator to add.
	 */
	public void addInstantiator(ParameterInstanciator<?> i);

	/**
	 * Adds the specified Instantiator for the given class <code>c</code> to this manager.
	 * <p>
	 * 
	 * @param i
	 *            The instantiator to add.
	 * @param c
	 *            The class, for that this type is being used.
	 */
	public void addInstantiator(ParameterInstanciator<?> i, Class<?> c);
}
