package com.myweb;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.el.ELException;
import java.net.URL;
import java.security.ProtectionDomain;

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
		// System.out.println(warFile);
		// try {
		// System.out.println( new File( new URL(warFile).getPath()
		// ).isDirectory() );
		// System.out.println( new File( new URL(warFile).getPath() ).isFile()
		// );
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		//
		// } finally {
		// System.exit(0);
		// }

		try {
			if (new File(new URL(warFile).getPath()).isDirectory()){
				warFile = "src/main/webapp";
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

		server.setHandler(context);
		return server;
	}
}
