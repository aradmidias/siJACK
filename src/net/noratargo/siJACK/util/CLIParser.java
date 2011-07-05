package net.noratargo.siJACK.util;

import net.noratargo.siJACK.interfaces.ConfigurationManager;

/**
 * This class represents a simple parser for CLI arguments.
 * 
 * @author HMulthaupt
 *
 */
public class CLIParser {

	/**
	 * Parses command line parameters.
	 * <p>
	 * Parameters, prefixed with <code>--</code> are interpreted as setting a value. A single <code>--</code> indicates,
	 * that no more parameters should be interpreted.
	 * 
	 * @param args
	 * @param cm
	 */
	public static void parseCLI(String[] args, ConfigurationManager cm) {
		for (String arg : args) {
			if (args.equals("--")) {
				/* do not continue! */
				break;
			}
			
			if (arg.startsWith("--")) {
				/* try to split the arg into ist prefix:name and value part: */
				int i = arg.indexOf('=');

				cm.setValue(arg.substring(2, i), arg.substring(i + 1));
			}
		}
	}
}
