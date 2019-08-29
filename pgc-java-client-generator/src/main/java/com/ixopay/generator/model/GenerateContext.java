package com.ixopay.generator.model;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GenerateContext {
	// relative
	public final Path javaSourceBase;
	public final Path javaExampleSourceBase;
	public final Path resourcesBase;

	// input
	public final Path inputDir;

	// output
	public final Path outputDir;
	public final Renaming renaming;

	private GenerateContext( Path javaSourceBase, Path javaExampleSourceBase, Path resourcesBase, Path inputDir, Path outputDir, Renaming renaming ) {
		this.javaSourceBase = javaSourceBase;
		this.javaExampleSourceBase = javaExampleSourceBase;
		this.resourcesBase = resourcesBase;
		this.inputDir = inputDir;
		this.outputDir = outputDir;
		this.renaming = renaming;
	}

	public static GenerateContext from( Path inputDir, Path outputDir, Renaming renaming ) {
		final Path javaSourceBase = Paths.get("src", "main", "java");
		final Path javaExampleSourceBase = Paths.get("src", "example", "java");
		final Path resourcesBase = Paths.get("src", "main", "resources");

		return new GenerateContext(
			javaSourceBase,
			javaExampleSourceBase,
			resourcesBase,
			inputDir,
			outputDir,
			renaming
		);
	}
}
