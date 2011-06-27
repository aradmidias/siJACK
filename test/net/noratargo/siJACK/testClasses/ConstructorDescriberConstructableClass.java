package net.noratargo.siJACK.testClasses;

import net.noratargo.siJACK.annotations.DefaultValue;
import net.noratargo.siJACK.annotations.Description;
import net.noratargo.siJACK.annotations.Name;

import java.io.File;
import java.net.URL;

public class ConstructorDescriberConstructableClass {
	public String s;
	public Long l;
	public Boolean b;

	public ConstructorDescriberConstructableClass(
			@Description("The URL object ot use for opening the pipe.") @DefaultValue("") @Name("url") URL url) {

	}

	public ConstructorDescriberConstructableClass(
			@Name("url") URL url,
			@Description("The time to wait for the connection to open.") @DefaultValue("1500") @Name("timeout") long timeout) {

	}

	public ConstructorDescriberConstructableClass(
			@Description("The host to connect to. May be a domain or subdomain as well as an IP address") @DefaultValue("") @Name("host") String host) {

	}

	public ConstructorDescriberConstructableClass(
			@Description("The host to connect to. May be a domain or subdomain as well as an IP address") @DefaultValue("") @Name("host") String host,
			@Description("The port to connect to") @DefaultValue("80") @Name("port") int port) {

	}

	/**
	 * 
	 * @param host
	 * @param port
	 * @param timeout
	 */
	public ConstructorDescriberConstructableClass(
			@Description("The host to connect to. May be a domain or subdomain as well as an IP address") @DefaultValue("") @Name("host") String host,
			@Description("The port to connect to") @DefaultValue("80") @Name("port") int port,
			@Description("The time to wait for the connection to open.") @DefaultValue("1500") @Name("timeout") long timeout) {

	}

	public ConstructorDescriberConstructableClass(
			@Description("The host to connect to. May be a domain or subdomain as well as an IP address") @DefaultValue("") @Name("host") String host,
			@Description("The time to wait for the connection to open.") @DefaultValue("1500") @Name("timeout") long timeout) {

	}

	public ConstructorDescriberConstructableClass(@Name("file") File file) {

	}

}
