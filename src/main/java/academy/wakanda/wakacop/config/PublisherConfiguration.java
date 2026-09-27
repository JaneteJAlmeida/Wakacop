package academy.wakanda.wakacop.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class PublisherConfiguration {

    @Value("${aws.config.endpointuri:#{null}}")
    private String endpointUrlStr;

    @Value("${cloud.aws.region.static:#{null}}")
    private String regionStr;

    private final AwsConfigProperties awsSnsProperties;
    private final MessageGroupIdRequestHandler messageGroupIdRequestHandler;

    private Optional<String> getEndpointUrl() {
        return Optional.ofNullable(endpointUrlStr);
    }

    private Optional<String> getRegion() {
        return Optional.ofNullable(regionStr);
    }

    @Bean
    public AmazonSNS amazonSNS() {
        log.info("[start] PublisherConfiguration - amazonSNS");
        log.trace("[awsSnsProperties] {}", awsSnsProperties);

        AmazonSNSAsyncClientBuilder clientBuilder = AmazonSNSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsSnsProperties.getAccesskey(), awsSnsProperties.getSecretkey())))
                .withRequestHandlers(messageGroupIdRequestHandler);

        addUrl(clientBuilder);

        AmazonSNSAsync clientSNS = clientBuilder.build();
        log.info("[finish] PublisherConfiguration - amazonSNS");
        return clientSNS;
    }

    private void addUrl(AmazonSNSAsyncClientBuilder clientBuilder) {
        getEndpointUrl()
                .map(url -> new AwsClientBuilder.EndpointConfiguration(url, getRegion()
                        .orElse(DEFAULT_REGION)))
                .ifPresentOrElse(clientBuilder::withEndpointConfiguration, () -> {
                    getRegion().ifPresent(clientBuilder::withRegion);
                });
    }

    @Bean
    public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSNS) {
        NotificationMessagingTemplate template = new NotificationMessagingTemplate(amazonSNS);

        org.springframework.messaging.converter.CompositeMessageConverter compositeConverter =
                (org.springframework.messaging.converter.CompositeMessageConverter) template.getMessageConverter();

        return template;
    }

    private static final String DEFAULT_REGION = "us-east-1";
}
