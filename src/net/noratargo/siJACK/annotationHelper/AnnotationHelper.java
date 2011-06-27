package net.noratargo.siJACK.annotationHelper;

import net.noratargo.siJACK.Parameter;
import net.noratargo.siJACK.ParameterPrefixNamePair;
import net.noratargo.siJACK.annotations.DefaultValue;
import net.noratargo.siJACK.annotations.Description;
import net.noratargo.siJACK.annotations.Name;
import net.noratargo.siJACK.annotations.Prefix;
import net.noratargo.siJACK.interfaces.InstantiatorManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnnotationHelper {

	@SuppressWarnings("unchecked")
	public static <T> Parameter<T> createParameter(Field f, T defaultValue, InstantiatorManager im) {
		/* Determine default prefix: */
		String defaultPrefix = PrefixAnnotationHelper.getDefaultPrefix(f.getDeclaringClass()
				.getAnnotation(Prefix.class), f.getAnnotation(Prefix.class), null, f);

		/* Determine default name: */
		String defaultName = NameAnnotationHelper.getDefaultName(f.getAnnotation(Name.class), f);

		/* Determine default value: */
		defaultValue = getDefaultValue(defaultValue, (Class<T>) f.getType(), f.getAnnotation(DefaultValue.class), im);

		// TODO: Determine all PrefixName pairs.
		Set<ParameterPrefixNamePair> ppnp = new HashSet<ParameterPrefixNamePair>();
		
		

		/* Get the description: */
		String description = getDescription(f.getAnnotation(Description.class));

		/* Create and return the Parameter: */
		return new Parameter<T>(defaultValue, (Class<T>) f.getType(), ppnp, defaultPrefix, defaultName, description);
	}

	public static List<Parameter<?>> createParametersFromConstructor(Constructor<?> c, InstantiatorManager im) {
		return null;
	}

	/**
	 * Returns the description from the given annotation or an empty String (""), if the annotation is not set.
	 * 
	 * @param d
	 *            The {@link Description} annotation. May be <code>null</code>.
	 * @return The description or an empty Stirng.
	 */
	public static String getDescription(Description d) {
		return (d == null ? "" : d.value());
	}

	/**
	 * Returns the default value - depending on the given input.
	 * 
	 * @param <T>
	 *            The type of the default value.
	 * @param defaultValue
	 *            The default value, as it is given on the current field.
	 * @param valueType
	 * @param d
	 * @param im
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDefaultValue(T defaultValue, Class<T> valueType, DefaultValue d, InstantiatorManager im) {
		if (defaultValue != null) {
			return im.getNewInstanceFrom(defaultValue);
		}

		if (d != null) {
			if (d.isNull()) {
				return null;
			}

			return im.getNewInstanceFor(valueType, d.value());
		}

		/* look for nativ datatypes: */
		if (valueType == int.class) {
			return (T) new Integer(0);
		}
		if (valueType == long.class) {
			return (T) new Long(0);
		}
		if (valueType == short.class) {
			return (T) new Short((short) 0);
		}
		if (valueType == byte.class) {
			return (T) new Byte((byte) 0);
		}
		if (valueType == char.class) {
			return (T) new Character((char) 0);
		}
		if (valueType == float.class) {
			return (T) new Float(0);
		}
		if (valueType == double.class) {
			return (T) new Double(0);
		}

		/* nothing set: */
		// TODO: Maybe throw an exception OR nag arout via System.err.println(...)
		return null;
	}

}
