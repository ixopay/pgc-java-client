package com.ixopay.generator.tasks;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.ixopay.generator.model.GenerateContext;

public class GradleBuildTask implements GeneratorTask {

	@Override public String describe( GenerateContext ctx ) {
		return "Run ./gradlew xjc javadoc assemble";
	}

	@Override public void run( GenerateContext ctx ) throws IOException {
		try {
			final ProcessBuilder processBuilder = new ProcessBuilder()
				.directory(ctx.outputDir.toFile())
				.command(ctx.outputDir.resolve("gradlew").toAbsolutePath().toString(), "xjc", "javadoc", "assemble")
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

}
