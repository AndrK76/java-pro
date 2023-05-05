package ru.otus.andrk.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    public GroupedOpenApi publicApi(@Value("${api.rest-api-prefix}/${api.rest-api-version}")String prefix) {
        return GroupedOpenApi.builder()
                .group("WebApp")
                .pathsToMatch(String.format("%s/**", prefix))
                .build();
    }
}
