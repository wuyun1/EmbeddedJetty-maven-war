package com.myweb;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.el.ELException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class EmbeddedServerMain {
	public static void main(String[] args) throws Exception {

		String contextPath = "/";
		int port = Integer.getInteger("port", 8080);

		Server server = createServer(contextPath, port);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}

	private static Server createServer(String contextPath, int port) {

		ProtectionDomain protectionDomain = EmbeddedServerMain.class.getProtectionDomain();
		URL location = protectionDomain.getCodeSource().getLocation();
		
		String warFile = location.toExternalForm();
//		List<String> staticResource = new ArrayList<String>();
		
		try {
			File file = new File(new URL(warFile).getPath()).getAbsoluteFile();
			System.out.println("RootPath: "+file);
			if (file.isDirectory()){
				warFile = "src/main/webapp";
//				String staticResourcePath="";
				if(new File(warFile).exists()){
				}else{
					warFile="";
				}
			}else if (!file.toString().endsWith(".war")){
				warFile = "";
			}else{
				System.setProperty("org.apache.jasper.compiler.disablejsr199", "true");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		// use Eclipse JDT compiler
		Server server = new Server(port);
		server.setStopAtShutdown(true);
		WebAppContext context = new WebAppContext(warFile, contextPath);
		context.setServer(server);

		// 设置work dir,war包将解压到该目录，jsp编译后的文件也将放入其中。
		String currentDir = new File(location.getPath()).getParent();
		File workDir = new File(currentDir, "work");
		context.setTempDirectory(workDir);
//		try {
//			context.newResource("");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		server.setHandler(context);
		return server;
	}
}
