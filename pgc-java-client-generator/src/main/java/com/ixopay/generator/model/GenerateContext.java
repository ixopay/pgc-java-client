package com.ixopay.generator.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

	// settings
	public final List<String> gradleTasks;

	private GenerateContext( Path javaSourceBase, Path javaExampleSourceBase, Path resourcesBase, Path inputDir, Path outputDir, Renaming renaming, List<String> gradleTasks ) {
		this.javaSourceBase = javaSourceBase;
		this.javaExampleSourceBase = javaExampleSourceBase;
		this.resourcesBase = resourcesBase;
		this.inputDir = inputDir;
		this.outputDir = outputDir;
		this.renaming = renaming;
		this.gradleTasks = gradleTasks;
	}

	public GenerateContext forSubProject( Path subProjectDir ) {
		Path subProjectOutputDir = outputDir.resolve(inputDir.relativize(subProjectDir).toString().replace(Placeholders.directory_prefix_placeholder, renaming.name + "-"));
		return from(subProjectDir, subProjectOutputDir, renaming, gradleTasks);
	}

	public static GenerateContext from( Path inputDir, Path outputDir, Renaming renaming, List<String> gradleTasks ) {
		final Path javaSourceBase = Paths.get("src", "main", "java");
		final Path javaExampleSourceBase = Paths.get("src", "example", "java");
		final Path resourcesBase = Paths.get("src", "main", "resources");

		return new GenerateContext(
			javaSourceBase,
			javaExampleSourceBase,
			resourcesBase,
			inputDir,
			outputDir,
			renaming,
			gradleTasks
		);
	}
}
