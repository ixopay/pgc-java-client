package com.ixopay.generator.model;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Renaming {
	public static final String[] jaxb_classes = {
		"pgc.client.model.callback.CallbackType",
		"pgc.client.model.callback.ErrorType",
		"pgc.client.model.options.OptionsType",
		"pgc.client.model.result.ErrorType",
		"pgc.client.model.result.ResultType",
		"pgc.client.model.schedule.ScheduleRequestType",
		"pgc.client.model.schedule.ScheduleResultType",
		"pgc.client.model.status_result.StatusResultType",
		"pgc.client.model.status.StatusType",
		"pgc.client.model.transaction.TransactionType"
	};

	public final String name;
	public final URL url;
	public final String package_;
	public final String githubOrganization;
	public final String host;
	public final Path packagePath;

	private Renaming( String name, URL url, String package_, String githubOrganization, String host, Path packagePath ) {
		this.name = name;
		this.url = url;
		this.package_ = package_;
		this.githubOrganization = githubOrganization;
		this.host = host;
		this.packagePath = packagePath;
	}

	public String jaxbReplacement() {
		return Arrays.stream(jaxb_classes)
			.map(s -> s.replace(Placeholders.package_placeholder + ".", package_ + ".") + ".class")
			.collect(Collectors.joining(",\n\t\t"));
	}

	public static Renaming from( String name, URL url, String package_, String githubOrganization ) {
		String[] packageParts = package_.split("\\.");
		StringBuilder host = new StringBuilder();
		for( int i = packageParts.length - 1; i >= 0; i-- ) {
			host.append(packageParts[i]);
			if( i > 0 )
				host.append('.');
		}

		Path packagePath = Paths.get(packageParts[0], Arrays.copyOfRange(packageParts, 1, packageParts.length));

		return new Renaming(name, url, package_, githubOrganization, host.toString(), packagePath);
	}
}
