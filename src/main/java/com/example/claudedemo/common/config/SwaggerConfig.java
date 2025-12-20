package com.example.claudedemo.common.config;

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
                        .title("상품 관리 API")
                        .version("1.0")
                        .description("상품 관리 시스템 REST API 문서입니다.\n\n" +
                                "이 API는 상품의 CRUD(생성, 조회, 수정, 삭제) 기능을 제공합니다."))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("로컬 서버")
                ));
    }
}
