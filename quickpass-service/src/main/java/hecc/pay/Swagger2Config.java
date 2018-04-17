package hecc.pay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author xuhoujun
 * @description: swagger2 配置
 * @date: Created In 下午11:39 on 2018/3/2.
 */
@Configuration
@RefreshScope
public class Swagger2Config {
    @Value("${quickpass-swagger2.hostname}")
    private String hostName;

    @Value("${quickpass-swagger2.isEnable}")
    private boolean isEnable;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).enable(isEnable)
                .apiInfo(apiInfo()).host(hostName).select()
                .apis(RequestHandlerSelectors.basePackage("hecc.pay"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("闪付业务中心APIS")
                .description("具体内容详见：http://xuhoujun.com")
                .termsOfServiceUrl("http://xuhoujun.com")
                .version("1.0").build();
    }
}
