package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class UrlResource implements Resource {

	private final URL url;

	public UrlResource(URL url) {
		this.url = url;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		//创建一个 URLConnection 对象，它代表与指定 URL 的连接。可用于读取和写入此 URL 引用的资源
		URLConnection con = this.url.openConnection();
		try {
			//通过 getInputStream() 返回一个 InputStream，由此读取 URL 所引用的资源数据
			return con.getInputStream();
		} catch (IOException ex) {
			throw ex;
		}
	}
}
