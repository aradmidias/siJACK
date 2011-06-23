package net.noratargo.siJACK.annotationHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import net.noratargo.siJACK.ParameterPrefixNamePair;
import net.noratargo.siJACK.annotations.ParameterDescription;
import net.noratargo.siJACK.annotations.ParameterName;
import net.noratargo.siJACK.annotations.Prefix;
import net.noratargo.siJACK.interfaces.InstantiatorManager;

public class ParameterDescriptionHelper {
	
	private static Prefix determineCurrentPrefix(Field f) {
		Prefix prefix = f.getAnnotation(Prefix.class);
		if (prefix == null) {
			/* look for a prefix at the defining class: */
			prefix = f.getDeclaringClass().getAnnotation(Prefix.class);

			if (prefix == null) {
				prefix = createPrefix(new String[] {}, 0, true);
			}
		}
		
		return prefix;
	}

	private static Prefix createPrefix(final String[] value, final int defaultValue, final boolean add) {
		return new Prefix() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String[] value() {
				return value;
			}

			@Override
			public int defaultValue() {
				return defaultValue;
			}

			@Override
			public boolean addFullQualifiedClassNameToPrefixList() {
				return add;
			}
		};
	}

	public static ParameterPrefixNamePair getDefaultPrefixNamePair(ParameterDescription pd, Field f) {
		ParameterName[] index = pd.name();
		int defaultIndex = pd.defaultParameterNameIndex();
		Class<?> declaringClass = f.getDeclaringClass();

		/* identify the default prefix and name: */
		if (defaultIndex == -1) {
			/* no defautl prefix and/or name: */
			return new ParameterPrefixNamePair("", "");
		} else if (defaultIndex >= index.length || defaultIndex < -1) {
			throw new IndexOutOfBoundsException(
					"the index you specified is either too small or too large. There is no element with the index "
							+ defaultIndex + "\n" + "You must choose from a value IN [-1, " + (index.length - 1)
							+ "] for " + declaringClass + "." + f.getName());
		} else {
			ParameterName i = index[defaultIndex];

			/* try to finde the prefix: */
			String def = getDefaultPrefix(i.prefix(), declaringClass, f);

			/* if there is no default prefix given (def==null) look for a prefix in the field-prefix: */
			def = (def == null && i.applyFieldPrefix() ? getDefaultPrefix(determineCurrentPrefix(f), declaringClass ,f)
					: def);
			return new ParameterPrefixNamePair(def == null ? "" : def, getDefaultName(i, f));
		}
	}

	/**
	 * @param p
	 * @param c
	 * @return <code>null</code> if no default prefix is given, or could not be determined, else a {@link String} is
	 *         returned.
	 */
	private static String getDefaultPrefix(Prefix p, Class<?> c, Field f) {
		int defaultPrefixIndex = p.defaultValue();

		if (defaultPrefixIndex == -1) {
			return null;
		} else if (defaultPrefixIndex < -1
				|| (p.addFullQualifiedClassNameToPrefixList() ? defaultPrefixIndex > p.value().length
						: defaultPrefixIndex >= p.value().length)) {
			throw new IndexOutOfBoundsException(
					"the index you specified is either too small or too large. There is no element with the index "
							+ defaultPrefixIndex + "\n" + "You must choose from a value IN [-1, "
							+ (p.value().length - (p.addFullQualifiedClassNameToPrefixList() ? 0 : 1)) + "] for "
							+ f.getDeclaringClass() + "." + f.getName());
		} else {
			return defaultPrefixIndex == p.value().length ? c.getName() : p.value()[defaultPrefixIndex];
		}
	}

	/**
	 * @param pm
	 * @param c
	 * @return The default name for the given field. If the returned Sting is an empty String (""), then there is no
	 *         default name.
	 */
	private static String getDefaultName(ParameterName pm, Field f) {
		int defaultNameIndex = pm.defaultValue();

		if (defaultNameIndex == -1) {
			return "";
		} else if (defaultNameIndex < -1
				|| (pm.addFieldNameToValues() ? defaultNameIndex > pm.value().length
						: defaultNameIndex >= pm.value().length)) {
			throw new IndexOutOfBoundsException(
					"the index you specified is either too small or too large. There is no element with the index "
							+ defaultNameIndex + "\n" + "You must choose from a value IN [-1, "
							+ (pm.value().length - (pm.addFieldNameToValues() ? 0 : 1)) + "] for " + f.getDeclaringClass()
							+ "." + f.getName());
		} else {
			return defaultNameIndex == pm.value().length ? f.getName() : pm.value()[defaultNameIndex];
		}
	}

	public static Set<ParameterPrefixNamePair> getParameterPrefixNamePairSet(ParameterDescription paramDescription, Field f) {
		HashSet<ParameterPrefixNamePair> parameterNames = new HashSet<ParameterPrefixNamePair>();

		Prefix p = determineCurrentPrefix(f);
		String declaringClassName = f.getDeclaringClass().getName();
		
		/* create the prefix-name-set: */
		for (ParameterName pn : paramDescription.name()) {
			/* stores all collected prefixes for later combindings. */
			HashSet<String> prefixes = new HashSet<String>();

			/* stores all collected names for later combindings. */
			HashSet<String> names = new HashSet<String>();

			/* collect all prefixes: */
			if (pn.applyFieldPrefix()) {
				addPrefixesToSet(prefixes, p, declaringClassName);
			}

			addPrefixesToSet(prefixes, pn.prefix(), declaringClassName);

			/* collect all names: */
			for (String name : pn.value()) {
				names.add(name);
			}

			if (pn.addFieldNameToValues()) {
				names.add(f.getName());
			}

			/* Create the pairing and add them to the parameterNames set: */
			for (String prefix : prefixes) {
				for (String name : names) {
					parameterNames.add(new ParameterPrefixNamePair(prefix, name));
				}
			}
		}
		
		return parameterNames;
	}

	private static void addPrefixesToSet(Set<String> s, Prefix p, String declaringClassName) {
		for (String prefix : p.value()) {
			s.add(prefix);
		}

		if (p.addFullQualifiedClassNameToPrefixList()) {
			s.add(declaringClassName);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getDefaultValue(Field f, Object o, ParameterDescription paramDescription, InstantiatorManager im) {
		Class<?> declaringClassName = f.getDeclaringClass();
		
		try {
			if (o == null && !Modifier.isStatic(f.getModifiers())) {
				/*
				 * we can not access a non-static field with a null-object. So we try to create one (if this does not
				 * work, we can continue outside of this try-catch-block, since we could not obtain the value by this
				 * way, anyway):
				 */
				o = declaringClassName.newInstance();
			}

			return (T) f.get(o);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		/* look for a default value inside the Annotation: */
		String value = paramDescription.defaultValue();

		if ("".equals(value)) {
			/* it is posible, that we should use an empty string as default value. */
			if (paramDescription.doNotComplainAboutDefaultParameter()) {
				System.err
						.println("Warning: Maybe there is no explicit default value set for field: "
								+ declaringClassName.getName()
								+ "."
								+ f.getName()
								+ " I will try to continue using "
								+ (paramDescription.isDefaultValueNull() ? "null" : "an empty String\"\"")
								+ " instead. "
								+ "If you want to have null as default, set the isDefaultValueNull parameter to true. If you do not want to see this message again, set doNotComplainAboutNullParameter to true!");
			}

			value = paramDescription.isDefaultValueNull() ? null : "";
		}

		return im.getNewInstanceFor((Class<T>) f.getType(), value);
	}
	
	public static String getDescription(ParameterDescription pd) {
		return "".equals(pd.value()) ? pd.description() : pd.value();
	}
}
