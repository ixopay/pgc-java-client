package com.ixopay.generator.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.ixopay.generator.model.GenerateContext;
import com.ixopay.generator.model.Placeholders;

public class GradleBuildTask implements GeneratorTask {

	@Override public String describe( GenerateContext ctx ) {
		if( ctx.gradleTasks.isEmpty() )
			return "Skipping ./gradlew";
		else
			return String.format("Run ./gradlew %s", ctx.gradleTasks.stream().map(gradleTask -> gradleTask(gradleTask, ctx)).collect(Collectors.joining(", ")));
	}

	@Override public void run( GenerateContext ctx ) throws IOException {
		try {
			List<String> command = new ArrayList<>();
			command.add(ctx.outputDir.resolve("gradlew").toAbsolutePath().toString());

			for( String gradleTask : ctx.gradleTasks )
				command.add(gradleTask(gradleTask, ctx));

			final ProcessBuilder processBuilder = new ProcessBuilder()
				.directory(ctx.outputDir.toFile())
				.command(command)
				.redirectError(ProcessBuilder.Redirect.INHERIT)
				.redirectOutput(ProcessBuilder.Redirect.INHERIT);

			final Map<String,String> env = processBuilder.environment();
			env.clear();
			env.put("JAVA_HOME", System.getProperty("java.home"));

			final Process process = processBuilder.start();

			final boolean finished = process.waitFor(5, TimeUnit.MINUTES);
			if( !finished )
				throw new RuntimeException("process did not finish in 5 minutes");

			if( process.exitValue() != 0 )
				throw new RuntimeException("process did not finish successfully");
		} catch( InterruptedException e ) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}

	private String gradleTask( String gradleTask, GenerateContext ctx ) {
		return gradleTask.replace(Placeholders.directory_prefix_placeholder, ctx.renaming.name + "-");
	}

}
