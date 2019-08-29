package com.ixopay.generator.tasks;

import java.io.IOException;

import com.ixopay.generator.model.GenerateContext;

public interface GeneratorTask {
	String describe( GenerateContext ctx );
	void run( GenerateContext ctx ) throws IOException;
}
