package org.example.myblog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "github")
public class GithubConfig {
    private String token;
    private String username;
    private String repo;
    private String branch = "main";
}
