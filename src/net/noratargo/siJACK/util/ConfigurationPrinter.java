package net.noratargo.siJACK.util;

import java.io.OutputStream;
import java.util.Set;

import net.noratargo.siJACK.interfaces.ConfigurationManager;
import net.noratargo.siJACK.interfaces.ImmutableConstructor;
import net.noratargo.siJACK.interfaces.ImmutableParameter;

public class ConfigurationPrinter {

	public static void printToStream(ConfigurationManager cm, OutputStream os) {
		Set<ImmutableParameter<?>> fields = cm.getFields();
		Set<ImmutableConstructor<?>> constructors = cm.getConstructors();
		
		
		
	}
}
