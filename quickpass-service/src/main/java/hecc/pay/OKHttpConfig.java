package hecc.pay;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Auther xuhoujun
 * @Description: okHttp Bean
 * @Date: Created In 下午5:44 on 2018/4/1.
 */
@Configuration
public class OKHttpConfig {

    private static final long OK_HTTP_TIME_OUT_SECOND = 60;

    @Bean
    public OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(OK_HTTP_TIME_OUT_SECOND, TimeUnit.SECONDS)
                .readTimeout(OK_HTTP_TIME_OUT_SECOND, TimeUnit.SECONDS)
                .writeTimeout(OK_HTTP_TIME_OUT_SECOND, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        return builder.build();
    }
}
