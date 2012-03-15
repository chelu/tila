/*
 * Copyright 2009-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.joseluismartin.gtc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Stream utility class
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class StreamUtils {
	
	public static void write(InputStream is, OutputStream os) throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = is.read(buffer)) > 0)
			os.write(buffer, 0, len);
		
	}
}
