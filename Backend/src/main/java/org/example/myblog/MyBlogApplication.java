package org.example.myblog;

import org.example.myblog.config.DotenvConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.myblog.mapper")
public class MyBlogApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(MyBlogApplication.class);
		application.addInitializers(new DotenvConfig());
		application.run(args);
	}
}
