package com.ixopay.generator.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class IO {
	private IO() {}

	public static long copy( InputStream in, OutputStream out ) throws IOException {
		byte[] buffer = new byte[4096]; // a normal size for disk block sizes is 4k, modern disks might have 8k...

		long count = 0;
		int n;
		while( -1 != (n = in.read(buffer)) ) {
			out.write(buffer, 0, n);
			count += n;
		}

		return count;
	}

}
