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
	 * Creates a new instance of <code>T</code>.
	 * 
	 * @param param
	 *            The String-representation of the new element.
	 * @return a new instance of <code>T</code>.
	 */
	public T createNewInstanceFromString(String param);

	/**
	 * Creates a new instance of the <code>originalValue</code> if <code>T</code> is mutable. May return the
	 * <code>originalValue</code>, if <code>T</code> is immutable.
	 * <p>
	 * The most important part about this method is, that all instances, that it returns are independent from each other
	 * in that way, that modificatins to one instanc edo not influence the other instance.
	 * 
	 * @param originalValue
	 *            The object to create a copy from.
	 * @return The new object.
	 */
	public T createNewInstance(T originalValue);
}
