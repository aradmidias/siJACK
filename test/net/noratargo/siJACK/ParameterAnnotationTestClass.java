package net.noratargo.siJACK;

import net.noratargo.siJACK.annotations.ParameterDescription;
import net.noratargo.siJACK.annotations.Prefix;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.TypeVariable;

/**
 * This class is only a dummy-test. It is used for analysing, how 
 * @author HMulthaupt
 *
 */
public class ParameterAnnotationTestClass {

	public ParameterAnnotationTestClass(@ParameterDescription int p1, int p2, @ParameterDescription int p3) {
		// TODO Auto-generated constructor stub
	}

	public <T> ParameterAnnotationTestClass(@ParameterDescription long p1, @Prefix("foo") @ParameterDescription int p2, @ParameterDescription int p3, T x, String s) {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {

		Class<?> c = ParameterAnnotationTestClass.class;
		
		for (Constructor<?> constr : c.getConstructors()) {
			System.out.println("ParameterAnnotationTestClass.main() constructor: "+ c);
			Annotation[][] parameterAnnotations = constr.getParameterAnnotations();
			Class<?>[] parameterTypes = constr.getParameterTypes();
			
			
			for (int i = 0; i < parameterAnnotations.length; i++) {
				System.out.println("ParameterAnnotationTestClass.main()   param "+ i);
				System.out.println("ParameterAnnotationTestClass.main()     class: "+ parameterTypes[i].getName());
				
				for (Annotation a : parameterAnnotations[i]) {
					System.out.println("ParameterAnnotationTestClass.main()     annotation: "+a);
				}
			}
			
			System.out.println("ParameterAnnotationTestClass.main() TypeParameters:");
			for (TypeVariable<?> tv : constr.getTypeParameters()) {
				System.out.println("ParameterAnnotationTestClass.main()  tv="+tv);
			}

		}
	}
}
