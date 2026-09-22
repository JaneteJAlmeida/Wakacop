package academy.wakanda.wakacop.config;

import feign.Client;
import feign.Logger;
import feign.Request;
import feign.slf4j.Slf4jLogger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableFeignClients(basePackages = {"academy.wakanda"})
@EnableDiscoveryClient
public class FeignConfig {

    @Value("${feign.connectTimeout:30000}")
    private long connectTimeout;

    @Value("${feign.readTimeout:300000}")
    private long readTimeout;

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    Logger logger() {
        return new Slf4jLogger(FeignConfig.class);
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, true);
    }

    @Bean
    public FeignErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    public Client feignClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            return new Client.Default(sslContext.getSocketFactory(), (hostname, session) -> true);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar SSL para o Feign", e);
        }
    }
}
