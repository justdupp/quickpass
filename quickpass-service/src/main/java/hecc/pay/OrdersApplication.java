package hecc.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @Auther xuhoujun
 * @Description:
 * @Date: Created In 下午10:11 on 2018/3/1.
 */
@SpringBootApplication
@EnableFeignClients
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})
@EnableCaching
public class OrdersApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrdersApplication.class, args);
    }
}
