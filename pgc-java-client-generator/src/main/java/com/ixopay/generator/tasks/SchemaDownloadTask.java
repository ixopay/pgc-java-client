package com.ixopay.generator.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import com.ixopay.generator.model.GenerateContext;
import com.ixopay.generator.util.IO;

public class SchemaDownloadTask implements GeneratorTask {

	private static final String schema_url_format = "%s/Schema/V2/%s";
	private static final Map<String,String> schemas;

	static {
		schemas = new HashMap<>();
		schemas.put("Callback", "callback.xsd");
		schemas.put("Options", "options.xsd");
		schemas.put("Result", "result.xsd");
		schemas.put("Schedule", "schedule.xsd");
		schemas.put("ScheduleResult", "scheduleResult.xsd");
		schemas.put("Status", "status.xsd");
		schemas.put("StatusResult", "statusResult.xsd");
		schemas.put("Transaction", "transaction.xsd");
	}

	@Override public String describe( GenerateContext ctx ) {
		return String.format("Downloading schemas from '%s'", String.format(schema_url_format, ctx.renaming.url, "..."));
	}

	@Override public void run( GenerateContext ctx ) throws IOException {
		Path schemasPath = ctx.outputDir.resolve(ctx.resourcesBase).resolve(ctx.renaming.packagePath.resolve("client")).resolve("schemas");
		Files.createDirectories(schemasPath);

		for( Map.Entry<String,String> schemaEntry : schemas.entrySet() ) {
			String schemaName = schemaEntry.getKey();
			String schemaFileName = schemaEntry.getValue();

			URL schemaUrl = new URL(String.format(schema_url_format, ctx.renaming.url, schemaName));

			final HttpURLConnection urlConnection = (HttpURLConnection)schemaUrl.openConnection();
			try {
				urlConnection.setRequestProperty("User-Agent", "curl/7.54.0"); // otherwise CloudFlare blocks us

				if( urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK )
					throw new IOException(String.format("failed to fetch schema '%s', HTTP status was %d", schemaUrl, urlConnection.getResponseCode()));

				try(
					InputStream schemaIn = urlConnection.getInputStream();
					OutputStream schemaOut = Files.newOutputStream(schemasPath.resolve(schemaFileName), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)
				) {
					IO.copy(schemaIn, schemaOut);
				}
			} finally {
				urlConnection.disconnect();
			}
		}
	}

}
