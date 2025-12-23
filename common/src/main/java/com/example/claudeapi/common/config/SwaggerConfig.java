package com.example.claudeapi.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("상품 & 쿠폰 관리 API")
                        .version("1.0")
                        .description("상품 및 쿠폰 관리 시스템 REST API 문서입니다."))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("로컬 서버")
                ));
    }
}
