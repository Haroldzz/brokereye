package life.freebao.devops.brokereye.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class HttpClientConfiguration {
    @Bean
    public HttpClient httpClient(){
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(5000L))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }
}
