package com.ssafypjt.bboard.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@OpenAPIDefinition(
		info = @Info(title = "BBOARD API 명세서",
				description = "bboard controller API 명세서",
				version = "v1"))
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi chatOpenApi() {
		String[] paths = {"/v1/**"};

		return GroupedOpenApi.builder()
				.group("COUPLE API v1")
				.pathsToMatch(paths)
				.build();
	}

}
