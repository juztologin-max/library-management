package com.library.security;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.servlet.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpRedirectConfiguration {
	@Value("${insecure_redirection.http.port}")
	private int httpPort;

	@Value("${server.port}")
	private int httpsPort;

	@Bean
	public ServletWebServerFactory tomcatServletWebServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		Connector con = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		con.setPort(httpPort);
		con.setScheme("http");
		con.setSecure(false);
		con.setRedirectPort(httpsPort);
		factory.addAdditionalConnectors(con);
		return factory;
	}

}
