package com.ixopay.generator.model;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Placeholders {
	public static final String jaxb_classes_placeholder = "// JAXB_CLASSES";
	public static final String gateway_host_placeholder = "gateway.client.pgc";
	public static final String package_placeholder = "pgc";
	public static final Path package_placeholder_path = Paths.get("pgc");
	public static final String host_placeholder = "client.pgc";
	public static final String name_placeholder = "pgcclient";
	public static final String github_organization_placeholder = "PgcClient";
}
