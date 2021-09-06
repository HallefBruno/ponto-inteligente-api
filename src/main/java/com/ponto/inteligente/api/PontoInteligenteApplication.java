package com.ponto.inteligente.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "JUS API", version = "2.0", description = "Api de automatização de processos"))
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", in = SecuritySchemeIn.HEADER, name = "Token", bearerFormat = "JWT")
//@SecurityScheme(name = "javainuseapi", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class PontoInteligenteApplication {

    public static void main(String[] args) {
        SpringApplication.run(PontoInteligenteApplication.class, args);
    }

}
