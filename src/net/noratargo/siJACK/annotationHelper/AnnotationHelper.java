package net.noratargo.siJACK.annotationHelper;

import net.noratargo.siJACK.Parameter;
import net.noratargo.siJACK.ParameterPrefixNamePair;
import net.noratargo.siJACK.annotations.DefaultValue;
import net.noratargo.siJACK.annotations.Description;
import net.noratargo.siJACK.annotations.Name;
import net.noratargo.siJACK.annotations.Prefix;
import net.noratargo.siJACK.interfaces.InstantiatorManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class AnnotationHelper {

	@SuppressWarnings("unchecked")
	public static <T> Parameter<T> createParameter(Field f, T defaultValue, InstantiatorManager im) {
		/* Determine default prefix: */
		String defaultPrefix = PrefixAnnotationHelper.getDefaultPrefix(f.getDeclaringClass()
				.getAnnotation(Prefix.class), f.getAnnotation(Prefix.class), f);

		/* Determine default name: */
		String defaultName = NameAnnotationHelper.getDefaultName(f.getAnnotation(Name.class), f);

		/* Determine default value: */
		defaultValue = getDefaultValue(defaultValue, (Class<T>) f.getType(), f.getAnnotation(DefaultValue.class), im);

		/* Determine all PrefixName pairs: */
		Set<ParameterPrefixNamePair> ppnp = getParameterPrefixPairs(PrefixAnnotationHelper.fillPrefixes(f),
				NameAnnotationHelper.fillNames(f));

		/* Get the description: */
		String description = getDescription(f.getAnnotation(Description.class));

		/* Create and return the Parameter: */
		return new Parameter<T>(defaultValue, (Class<T>) f.getType(), ppnp, defaultPrefix, defaultName, description);
	}

	/**
	 * Returns a list of parameters, that represent the current Constructor's parameters.
	 * 
	 * @param c
	 *            The constructor to represent.
	 * @param im
	 *            The {@link InstantiatorManager} to use for determining the default values.
	 * @return A list in which every element represents one parameter of the current constructor. The order of the
	 *         elements is equal to the order of the constructor's parameters. rturns <code>null</code>, if this
	 *         constructor should be avoided.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Parameter<?>[] createParametersFromConstructor(Constructor<?> c, InstantiatorManager im) {
		Parameter<?>[] parameters = new Parameter<?>[c.getParameterTypes().length];
		Annotation[][] pAnnotations = c.getParameterAnnotations();
		Class<?>[] cTypes = c.getParameterTypes();

		boolean skipThisOne = true;

		for (int i = 0; i < parameters.length; i++) {
			/* this represents all annotations of the current parameter: */
			Annotation[] annotations = pAnnotations[i];

			Prefix p = null;
			Name n = null;
			DefaultValue dv = null;
			Description des = null;

			for (Annotation a : annotations) {
				if (a instanceof Prefix) {
					p = (Prefix) a;
				} else if (a instanceof Name) {
					n = (Name) a;
				} else if (a instanceof DefaultValue) {
					dv = (DefaultValue) a;
				} else if (a instanceof Description) {
					des = (Description) a;
				} else {
					/* no annotation, that we support here. */
				}
			}

			if (p != null || n != null || dv != null || des != null) {
				skipThisOne = false;

				/* this is the current parameter's type: */
				Class<?> type = cTypes[i];

				/* we got the annotations. so let's put them together. */

				/* Determine default prefix: */
				String defaultPrefix = PrefixAnnotationHelper.getDefaultPrefix(
						c.getDeclaringClass().getAnnotation(Prefix.class), c.getAnnotation(Prefix.class), p, c, i);

				/* Determine default name: */
				String defaultName = NameAnnotationHelper.getDefaultName(n, c, i);

				/* Determine default value: */
				Object defaultValue = getDefaultValue(null, type, dv, im);

				/* Determine all PrefixName pairs: */
				Set<ParameterPrefixNamePair> ppnp = getParameterPrefixPairs(PrefixAnnotationHelper.fillPrefixes(c, p),
						NameAnnotationHelper.fillNames(n));

				/* Get the description: */
				String description = getDescription(des);

				/* Create and return the Parameter: */
				parameters[i] = new Parameter(defaultValue, type, ppnp, defaultPrefix, defaultName, description);
			}
		}

		return skipThisOne ? null : parameters;
	}

	private static Set<ParameterPrefixNamePair> getParameterPrefixPairs(Set<String> prefixes, Set<String> names) {
		Set<ParameterPrefixNamePair> ppnp = new HashSet<ParameterPrefixNamePair>();

		/* build all possible prefix-name constellations, that are posible: */
		for (String prefix : prefixes) {
			for (String name : names) {
				ppnp.add(new ParameterPrefixNamePair(prefix, name));
			}
		}

		return ppnp;
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
