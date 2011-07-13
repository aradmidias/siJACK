package net.noratargo.siJACK.util;

import javax.naming.ConfigurationException;

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
				try {
					cm.setValue(arg.substring(2));
				} catch (ConfigurationException e) {
					System.err.println("CLIParser.parseCLI() skipping command line argument "+ arg);
				}
			}
		}
	}
}
