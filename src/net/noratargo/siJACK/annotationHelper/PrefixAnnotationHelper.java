package net.noratargo.siJACK.annotationHelper;

import net.noratargo.siJACK.annotations.Prefix;

import java.lang.reflect.Field;

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
	 * @param thirdLevel
	 *            The parameter's prefix (if applicable). Must be <code>null</code>, if not specified.
	 * @return The Prefix, or an Empty String.
	 */
	public static String getDefaultPrefix(Prefix firstLevel, Prefix secondLevel, Prefix thirdLevel, Field f) {
		return thirdLevel != null ? getDefaultValue(thirdLevel, f) : (secondLevel != null ? getDefaultValue(
				secondLevel, f) : (firstLevel != null ? getDefaultValue(firstLevel, f) : f.getDeclaringClass().getName()));
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

}
