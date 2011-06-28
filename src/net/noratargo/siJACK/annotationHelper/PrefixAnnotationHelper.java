package net.noratargo.siJACK.annotationHelper;

import net.noratargo.siJACK.annotations.Prefix;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * THe methods in thes class should help, when working with the {@link Prefix} annotation.
 * 
 * @author HMulthaupt
 */
public class PrefixAnnotationHelper {

	/**
	 * @param firstLevel
	 *            The clas sor interface's prefix. Must be <code>null</code>, if not specified.
	 * @param secondLevel
	 *            THe Field's or Constructor's prefix. Must be <code>null</code>, if not specified.
	 * @param f
	 *            The field, for that the default prefix should be determindes.
	 * @return The Prefix, or an Empty String.
	 */
	public static String getDefaultPrefix(Prefix firstLevel, Prefix secondLevel, Field f) {
		return secondLevel != null ? getDefaultValue(secondLevel, f) : (firstLevel != null ? getDefaultValue(
				firstLevel, f) : f.getDeclaringClass().getName());
	}

	private static String getDefaultValue(Prefix p, Field f) {
		int defaultPrefixIndex = p.defaultValue();

		if (defaultPrefixIndex == -1) {
			return "";
		} else if (defaultPrefixIndex < -1
				|| (p.addClassPathToPrefixes() ? defaultPrefixIndex > p.value().length : defaultPrefixIndex >= p
						.value().length)) {
			throw new IndexOutOfBoundsException(
					"the index you specified is either too small or too large. There is no element with the index "
							+ defaultPrefixIndex + "\n" + "You must choose from a value IN [-1, "
							+ (p.value().length - (p.addClassPathToPrefixes() ? 0 : 1)) + "] for "
							+ f.getDeclaringClass() + "." + f.getName());
		} else {
			return defaultPrefixIndex == p.value().length ? f.getDeclaringClass().getName()
					: p.value()[defaultPrefixIndex];
		}
	}

	/**
	 * @param firstLevel
	 *            The clas sor interface's prefix. Must be <code>null</code>, if not specified.
	 * @param secondLevel
	 *            THe Field's or Constructor's prefix. Must be <code>null</code>, if not specified.
	 * @param thirdLevel
	 *            The parameter's prefix (if applicable). Must be <code>null</code>, if not specified.
	 * @return The Prefix, or an Empty String.
	 */
	public static String getDefaultPrefix(Prefix firstLevel, Prefix secondLevel, Prefix thirdLevel, Constructor<?> c,
			int parameterIndex) {
		return (thirdLevel != null ? getDefaultValue(secondLevel, c, parameterIndex)
				: (secondLevel != null ? getDefaultValue(secondLevel, c, parameterIndex)
						: (firstLevel != null ? getDefaultValue(firstLevel, c, parameterIndex) : c.getDeclaringClass()
								.getName())));
	}

	private static String getDefaultValue(Prefix p, Constructor<?> c, int parameterIndex) {
		int defaultPrefixIndex = p.defaultValue();

		if (defaultPrefixIndex == -1) {
			return "";
		} else if (defaultPrefixIndex < -1
				|| (p.addClassPathToPrefixes() ? defaultPrefixIndex > p.value().length : defaultPrefixIndex >= p
						.value().length)) {
			throw new IndexOutOfBoundsException(
					"the index you specified is either too small or too large. There is no element with the index "
							+ p.defaultValue() + "\n" + "You must choose from a value IN [-1, "
							+ (p.value().length - (p.addClassPathToPrefixes() ? 0 : 1)) + "] for the "
							+ (parameterIndex + 1) + ". parameter of the constructor "
							+ c.getDeclaringClass().getName() + "." + c.getName() + "("
							+ Arrays.toString(c.getParameterTypes()) + ")");
		} else {
			return defaultPrefixIndex == p.value().length ? c.getDeclaringClass().getName()
					: p.value()[defaultPrefixIndex];
		}
	}

	/**
	 * @param prefixes
	 * @param f
	 */
	public static Set<String> fillPrefixes(Field f) {
		Set<String> prefixes = new HashSet<String>();
		Prefix cPrefix = f.getDeclaringClass().getAnnotation(Prefix.class);
		Prefix fPrefix = f.getAnnotation(Prefix.class);
		String declaringClassName = f.getDeclaringClass().getName();

		if (cPrefix == null && fPrefix == null) {
			/* no explicit prefix set, so set the default one: */
			prefixes.add(declaringClassName);
		} else {
			/* add all class prefixes: */
			fillPrefixes(prefixes, cPrefix, declaringClassName);
			fillPrefixes(prefixes, fPrefix, declaringClassName);
		}
		
		return prefixes;
	}

	/**
	 * @param prefixes
	 * @param f
	 */
	public static Set<String> fillPrefixes(Constructor<?> c, Prefix paramPrefix) {
		Set<String> prefixes = new HashSet<String>();
		
		Prefix cPrefix = c.getDeclaringClass().getAnnotation(Prefix.class);
		Prefix fPrefix = c.getAnnotation(Prefix.class);
		String declaringClassName = c.getDeclaringClass().getName();

		if (cPrefix == null && fPrefix == null && paramPrefix == null) {
			/* no explicit prefix set, so set the default one: */
			prefixes.add(declaringClassName);
		} else {
			/* add all class prefixes: */
			fillPrefixes(prefixes, cPrefix, declaringClassName);
			fillPrefixes(prefixes, fPrefix, declaringClassName);
			fillPrefixes(prefixes, paramPrefix, declaringClassName);
		}
		
		return prefixes;
	}
	
	private static void fillPrefixes(Set<String> prefixes, Prefix p, String declaringClassName) {
		if (p != null) {
			for (String s : p.value()) {
				prefixes.add(s);
			}

			if (p.addClassPathToPrefixes()) {
				prefixes.add(declaringClassName);
			}
		}
	}
}
