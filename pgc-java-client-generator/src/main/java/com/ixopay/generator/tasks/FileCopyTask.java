package com.ixopay.generator.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

import com.ixopay.generator.model.GenerateContext;
import com.ixopay.generator.model.Placeholders;
import com.ixopay.generator.model.Renaming;
import com.ixopay.generator.util.IO;

public class FileCopyTask implements GeneratorTask {

	private static final Set<String> ignored_dirs;
	private static final Set<String> ignored_files;
	private static final Set<String> text_extensions;

	static {
		ignored_dirs = new HashSet<>();
		ignored_dirs.add("build");
		ignored_dirs.add(".gradle");
		ignored_dirs.add(".idea");

		ignored_files = new HashSet<>();
		ignored_files.add(".eclipse");
		ignored_files.add(".project");
		ignored_files.add(".iml");
		ignored_files.add(".ipr");

		text_extensions = new HashSet<>();
		text_extensions.add(".gradle");
		text_extensions.add(".java");
		text_extensions.add(".md");
		text_extensions.add(".xjb");
		text_extensions.add(".xsd");
	}

	@Override public String describe(GenerateContext ctx) {
		return String.format("Copy project files from '%s' to '%s'", ctx.inputDir, ctx.outputDir);
	}

	@Override
	public void run( GenerateContext ctx ) throws IOException {
		final Renaming renaming = ctx.renaming;
		final Path javaSources = ctx.inputDir.resolve(ctx.javaSourceBase);
		final Path javaExampleSources = ctx.inputDir.resolve(ctx.javaExampleSourceBase);
		final Path resources = ctx.inputDir.resolve(ctx.resourcesBase);
		final Path resourcesBasePackage = resources.resolve(Placeholders.package_placeholder_path);
		final Path javaBasePackage = javaSources.resolve(Placeholders.package_placeholder_path);
		final Path javaExampleBasePackage = javaExampleSources.resolve(Placeholders.package_placeholder_path);

		if( !Files.exists(javaBasePackage) || !Files.isDirectory(javaBasePackage) )
			throw new IllegalArgumentException(String.format("expected %s to contain package %s", javaSources, Placeholders.package_placeholder_path));

		if( !Files.exists(javaExampleBasePackage) || !Files.isDirectory(javaExampleBasePackage) )
			throw new IllegalArgumentException(String.format("expected %s to contain package %s", javaExampleSources, Placeholders.package_placeholder_path));

		if( !Files.exists(resourcesBasePackage) || !Files.isDirectory(resourcesBasePackage) )
			throw new IllegalArgumentException(String.format("expected %s to contain package %s", resources, Placeholders.package_placeholder_path));

		Files.walkFileTree(ctx.inputDir, new FileVisitor<Path>() {
			@Override public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) throws IOException {
				if( Files.isSameFile(dir.getParent(), ctx.inputDir) && ignored_dirs.contains(dir.getFileName().toString()) )
					return FileVisitResult.SKIP_SUBTREE;

				return FileVisitResult.CONTINUE;
			}
			@Override public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
				if( !Files.isRegularFile(file) || ignored_files.contains(file.getFileName().toString()) )
					return FileVisitResult.CONTINUE;

				Path newFile;
				if( file.startsWith(javaSources) )
					newFile = ctx.outputDir.resolve(ctx.javaSourceBase).resolve(renaming.packagePath).resolve(javaBasePackage.relativize(file));
				else if( file.startsWith(javaExampleSources) )
					newFile = ctx.outputDir.resolve(ctx.javaExampleSourceBase).resolve(renaming.packagePath).resolve(javaExampleBasePackage.relativize(file));
				else if( file.startsWith(resources) )
					newFile = ctx.outputDir.resolve(ctx.resourcesBase).resolve(renaming.packagePath).resolve(resourcesBasePackage.relativize(file));
				else
					newFile = ctx.outputDir.resolve(ctx.inputDir.relativize(file));

				newFile = newFile.getParent().resolve(newFile.getFileName().toString().replace(Placeholders.name_placeholder, renaming.name));

				Files.createDirectories(newFile.getParent());

				final String newFileName = newFile.getFileName().toString();
				boolean treatAsTextFile = false;
				for( String replaceExtension : text_extensions )
					if( newFileName.endsWith(replaceExtension) ) {
						treatAsTextFile = true;
						break;
					}

				if( treatAsTextFile ) {
					String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);

					content = content
						.replace(Placeholders.host_placeholder, renaming.host)
						.replace(Placeholders.gateway_host_placeholder, renaming.url.getHost())
						.replace(Placeholders.package_placeholder + ".", renaming.package_ + ".")
						.replace(Placeholders.package_placeholder_path.toString() + "/", renaming.packagePath.toString() + "/")
						.replace(Placeholders.jaxb_classes_placeholder, renaming.jaxbReplacement())
						.replace(Placeholders.name_placeholder, renaming.name)
						.replace(Placeholders.github_organization_placeholder, renaming.githubOrganization)
						.replace(Placeholders.package_placeholder, renaming.package_);

					Files.write(newFile, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
				} else {
					try(
						final InputStream in = Files.newInputStream(file, StandardOpenOption.READ);
						final OutputStream out = Files.newOutputStream(newFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)
					) {
						IO.copy(in, out);
					}
				}
				Files.setPosixFilePermissions(newFile, Files.getPosixFilePermissions(file));

				return FileVisitResult.CONTINUE;
			}
			@Override public FileVisitResult visitFileFailed( Path file, IOException exc ) throws IOException {
				throw new IOException(String.format("Failed to visit file '%s'", file), exc);
			}
			@Override public FileVisitResult postVisitDirectory( Path dir, IOException exc ) {
				return FileVisitResult.CONTINUE;
			}
		});

		// create settings.gradle in case a parent directory is already a gradle project so this becomes a new gradle project
		final Path settingsGradle = ctx.outputDir.resolve("settings.gradle");
		if( !Files.exists(settingsGradle) )
			Files.createFile(settingsGradle);
	}


}
