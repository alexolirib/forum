package br.com.estudo.forum.config.swagger;

import br.com.estudo.forum.model.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;

@Configuration
public class SwaggerConfigurations {

    @Bean
    public Docket api() {
            //tipo de documentacao
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //path para documentação
                .apis(RequestHandlerSelectors.basePackage("br.com.estudo.forum"))
                //gerar com tudo do projeto - url raiz
                .paths(PathSelectors.ant("/**"))
                .build()
                //ignore todas as urls que se releciona com usuário
                .ignoredParameterTypes(Usuario.class)
                //adicionar parametro global (para autenticação
                        .globalOperationParameters(
                                Arrays.asList(
                                        new ParameterBuilder()
                                                .name("Authorization")
                                                .description("Header para Token JWT")
                                                .modelRef(new ModelRef("string"))
                                                .parameterType("header")
                                                .required(false)
                                                .build()));
    }

}
