package com.example.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.io.IOException;

@SpringBootApplication
public class TodoAppApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TodoAppApplication.class);

		// ✅ 起動完了後にGoogle Chromeで開く
//		app.addListeners((ApplicationListener<ApplicationReadyEvent>) event -> {
//			try {
//				System.out.println("Trying to open browser...");
//
//				// Windows向け：Google Chromeのパス
//				String chromePath = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
//				String url = "http://localhost:8080";
//
//				// ChromeでURLを開く
//				Runtime.getRuntime().exec(new String[] { chromePath, url });
//
//				System.out.println("Chrome should have opened.");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});

		app.run(args);
	}
}
