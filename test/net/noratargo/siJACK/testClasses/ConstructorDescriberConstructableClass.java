package net.noratargo.siJACK.testClasses;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import net.noratargo.siJACK.annotations.ConstructorDescription;
import net.noratargo.siJACK.annotations.Name;
import net.noratargo.siJACK.annotations.ParameterDescription;
import net.noratargo.siJACK.annotations.Prefix;

public class ConstructorDescriberConstructableClass {
	public String s;
	public Long l;
	public Boolean b;

	@ConstructorDescription(groupName = "url")
	public ConstructorDescriberConstructableClass(
			@ParameterDescription(description = "The URL object ot use for opening the pipe.", defaultValue = "", isDefaultValueNull = true, name = @Name("url")) URL url) {

	}

	@ConstructorDescription(groupName = "url")
	public ConstructorDescriberConstructableClass(
			@ParameterDescription(defaultValue = "", name = @Name("url")) URL url,
			@ParameterDescription(description = "The time to wait for the connection to open.", defaultValue = "1500", name = @Name("timeout")) long timeout) {

	}

	public ConstructorDescriberConstructableClass(
			@ParameterDescription(description = "The host to connect to. May be a domain or subdomain as well as an IP address", defaultValue = "", name = @Name("host")) String host) {

	}

	public ConstructorDescriberConstructableClass(
			@ParameterDescription(description = "The host to connect to. May be a domain or subdomain as well as an IP address", defaultValue = "", name = @Name("host")) String host,
			@ParameterDescription(description = "The port to connect to", defaultValue = "80", name = @Name("port")) int port) {

	}

	public ConstructorDescriberConstructableClass(
			@ParameterDescription(description = "The host to connect to. May be a domain or subdomain as well as an IP address", defaultValue = "", name = @Name("host")) String host,
			@ParameterDescription(description = "The port to connect to", defaultValue = "80", name = @Name("port")) int port,
			@ParameterDescription(description = "The time to wait for the connection to open.", defaultValue = "1500", name = @Name("timeout")) long timeout) {

	}

	public ConstructorDescriberConstructableClass(
			@ParameterDescription(description = "The host to connect to. May be a domain or subdomain as well as an IP address", defaultValue = "", name = @Name("host")) String host,
			@ParameterDescription(description = "The time to wait for the connection to open.", defaultValue = "1500", name = @Name("timeout")) long timeout) {

	}

	@ConstructorDescription(groupName = "file")
	public ConstructorDescriberConstructableClass(
			@ParameterDescription(defaultValue = "", name = @Name("file")) File file) {

	}

}
