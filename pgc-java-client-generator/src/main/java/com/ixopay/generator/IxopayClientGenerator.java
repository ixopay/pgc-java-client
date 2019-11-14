package com.ixopay.generator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.ixopay.generator.model.GenerateContext;
import com.ixopay.generator.model.Renaming;
import com.ixopay.generator.tasks.FileCopyTask;
import com.ixopay.generator.tasks.GeneratorTask;
import com.ixopay.generator.tasks.GradleBuildTask;
import com.ixopay.generator.tasks.SchemaDownloadTask;

public class IxopayClientGenerator {

	public static void main( String[] args ) throws IOException {
		Options options = new Options();

		options.addOption(
			Option.builder("p")
				.longOpt("package")
				.desc("Java package name for the generated client library.")
				.hasArg(true)
				.type(String.class)
				.required()
				.build()
		);
		options.addOption(
			Option.builder("n")
				.longOpt("name")
				.desc("Short name of the generated client library, used for naming the library itself (i.e. Maven artifact name is \"<name>-java-client\").")
				.hasArg(true)
				.type(String.class)
				.required()
				.build()
		);
		options.addOption(
			Option.builder()
				.longOpt("product-name")
				.desc("Product name for javadoc.")
				.hasArg(true)
				.type(String.class)
				.required()
				.build()
		);
		options.addOption(
			Option.builder()
				.longOpt("github-organization")
				.desc("GitHub organization or username, used for README.md links to GitHub and Jitpack.io.")
				.hasArg(true)
				.type(String.class)
				.required()
				.build()
		);
		options.addOption(
			Option.builder("u")
				.longOpt("url")
				.desc("Base URL for XSD schemas of the client (e.g. https://gateway.ixopay.com).")
				.hasArg(true)
				.type(URL.class)
				.required()
				.build()
		);
		options.addOption(
			Option.builder("t")
				.longOpt("tokenization-url")
				.desc("Base URL tokenization API (e.g. https://secure.ixopay.com).")
				.hasArg(true)
				.type(URL.class)
				.build()
		);
		options.addOption(
			Option.builder("o")
				.longOpt("output-dir")
				.desc("Output directory for the generated client library.")
				.hasArg(true)
				.type(File.class)
				.required()
				.build()
		);
		options.addOption(
			Option.builder("i")
				.longOpt("input-dir")
				.desc("Input directory for the template client library.")
				.hasArg(true)
				.type(File.class)
				.required()
				.build()
		);
		options.addOption(
			Option.builder("g")
				.longOpt("gradle-task")
				.desc("Gradle task to execute in the output directory.")
				.hasArgs()
				.type(String.class)
				.build()
		);
		options.addOption(
			Option.builder("s")
				.longOpt("disable-download-schemas")
				.desc("Skip downloading XSD schemas.")
				.hasArg(false)
				.type(Boolean.class)
				.build()
		);

		HelpFormatter helpFormatter = new HelpFormatter();
		CommandLineParser parser = new DefaultParser();
		String commandName = "pgc-generate";
		try {
			final CommandLine cmd = parser.parse(options, args);

			final String package_ = (String)cmd.getParsedOptionValue("p");
			final String name = (String)cmd.getParsedOptionValue("n");
			final String productName = (String)cmd.getParsedOptionValue("product-name");
			final String githubOrganization = (String)cmd.getParsedOptionValue("github-organization");
			final URL url = (URL)cmd.getParsedOptionValue("u");
			@Nullable final URL tokenizationUrl = (URL)cmd.getParsedOptionValue("t");
			final File outputDir = (File)cmd.getParsedOptionValue("o");
			final File inputDir = (File)cmd.getParsedOptionValue("i");
			final List<String> gradleTasks = Arrays.asList(cmd.getOptionValues("g"));
			final boolean disableDownloadSchemas = cmd.hasOption("s");

			if( !outputDir.exists() ) {
				System.err.printf("--output-dir/-o \"%s\" doesn't exist.%n", outputDir);
				helpFormatter.printHelp(commandName, options, true);
				System.exit(1);
			}

			if( !outputDir.isDirectory() ) {
				System.err.printf("--output-dir/-o \"%s\" is not a directory.%n", outputDir);
				helpFormatter.printHelp(commandName, options, true);
				System.exit(1);
			}

			if( !inputDir.exists() ) {
				System.err.printf("--input-dir/-i \"%s\" doesn't exist.%n", inputDir);
				helpFormatter.printHelp(commandName, options, true);
				System.exit(1);
			}

			if( !inputDir.isDirectory() ) {
				System.err.printf("--input-dir/-i \"%s\" is not a directory.%n", inputDir);
				helpFormatter.printHelp(commandName, options, true);
				System.exit(1);
			}

			new IxopayClientGenerator(
				package_,
				name,
				productName,
				githubOrganization,
				url,
				tokenizationUrl,
				outputDir.toPath().toAbsolutePath(),
				inputDir.toPath().toAbsolutePath(),
				gradleTasks,
				disableDownloadSchemas
			).run();
		} catch( ParseException e ) {
			System.err.printf("Wrong argument: %s%n", e.getMessage());
			helpFormatter.printHelp(commandName, options, true);
			System.exit(1);
		}
	}

	private final GenerateContext ctx;
	private final boolean disableDownloadSchemas;

	public IxopayClientGenerator( String package_, String name, String productName, String githubOrganization, URL url, @Nullable URL tokenizationUrl, Path outputDir, Path inputDir, List<String> gradleTasks, boolean disableDownloadSchemas ) {
		this.ctx = GenerateContext.from(inputDir, outputDir, Renaming.from(name, url, tokenizationUrl, package_, productName, githubOrganization), gradleTasks);
		this.disableDownloadSchemas = disableDownloadSchemas;
	}

	public void run() throws IOException {
		List<GeneratorTask> tasks = new ArrayList<>(3);
		tasks.add(new FileCopyTask());
		if( !disableDownloadSchemas )
			tasks.add(new SchemaDownloadTask());
		tasks.add(new GradleBuildTask());

		for( GeneratorTask task : tasks ) {
			System.out.printf("Running %s%n", task.describe(ctx));
			task.run(this.ctx);
		}
	}

}
