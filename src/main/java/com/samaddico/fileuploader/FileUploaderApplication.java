package com.samaddico.fileuploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class FileUploaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploaderApplication.class, args);
	}
}
