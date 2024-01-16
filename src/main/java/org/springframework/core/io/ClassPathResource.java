package org.springframework.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * classpath下的资源
 *
 */
public class ClassPathResource implements Resource {

	private final String path;

	public ClassPathResource(String path) {
		this.path = path;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		//使用当前类的类加载器，通过指定的路径获取资源文件的输入流。
		//getResourceAsStream() 它可用于从类路径中获取指定路径的资源文件，并返回该资源文件的输入流。
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.path);
		if (is == null) {
			throw new FileNotFoundException(this.path + " cannot be opened because it does not exist");
		}
		return is;
	}
}
