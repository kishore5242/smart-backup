package com.kishore.sb;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartBackupApplication {

	private static boolean running = false;

	public static void main(String[] args) {
		try {
			SpringApplication.run(SmartBackupApplication.class, args);
		} finally {
			if(!running) {
				browse("http://localhost:8520/cmd/");
			}
		}
	}

	public static void browse(String url) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(new URI(url));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		} else {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}