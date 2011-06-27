package net.noratargo.siJACK.annotationHelper;

import net.noratargo.siJACK.annotations.Name;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * The Methods in this class should help, when working with the {@link Name} annotation.
 * 
 * @author HMulthaupt
 */
public class NameAnnotationHelper {

	/**
	 * 
	 * @param pm
	 * @param defaultName
	 * @param declaringClassName The name of the class, declaring this field.
	 * @return The default name for the given field. If the returned Sting is an empty String (""), then there is no
	 *         default name.
	 */
	public static String getDefaultName(Name n, Field f) {
		if (n == null) {
			return f.getName();
		}
		
		int defaultNameIndex = n.defaultName();

		return getDefaultName(n, f.getName(), "the index you specified is either too small or too large. There is no element with the index "
				+ defaultNameIndex + "\n" + "You must choose from a value IN [-1, "
				+ (n.value().length - (n.addFieldNameIfPossible() ? 0 : 1)) + "] for "
				+ f.getDeclaringClass().getName() + "." + f.getName());
	}
	
	public static String getDefaultValue(Name n, Constructor<?> c) {
		if (n == null) {
			//TODO: zui früher stunde erneut prüfen, ob das hier sinnvoll ist!
			throw new RuntimeException("You HAVE TO specify a name for every consturctor.");
		}
		
		int defaultNameIndex = n.defaultName();
		
		String defaultName = getDefaultName(n, null, "the index you specified is either too small or too large. There is no element with the index "
				+ defaultNameIndex + "\n" + "You must choose from a value IN [-1, "
				+ (n.value().length - (n.addFieldNameIfPossible() ? 0 : 1)) + "] for "
				+ c.getDeclaringClass().getName() + "." + c.getName() + "("+ Arrays.toString(c.getParameterTypes()) +")");
		
		if (defaultName == null) {
			//TODO: zui früher stunde erneut prüfen, ob das hier sinnvoll ist!
			throw new RuntimeException("You HAVE TO specify a name for every consturctor.");
		}
		
		return defaultName;
	}
	
	private static String getDefaultName(Name n, String defaultName, String possibleErrorMessage) {
		int defaultNameIndex = n.defaultName();

		if (defaultNameIndex == -1) {
			return "";
		} else if (defaultNameIndex < -1
				|| (n.addFieldNameIfPossible() ? defaultNameIndex > n.value().length
						: defaultNameIndex >= n.value().length)) {
			throw new IndexOutOfBoundsException(possibleErrorMessage);
		} else {
			return defaultNameIndex == n.value().length ? defaultName : n.value()[defaultNameIndex];
		}
	}

}
