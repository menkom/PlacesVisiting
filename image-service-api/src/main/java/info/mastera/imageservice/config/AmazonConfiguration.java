package info.mastera.imageservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.signer.AwsS3V4Signer;
import software.amazon.awssdk.core.client.config.SdkAdvancedClientOption;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class AmazonConfiguration {

    @Bean
    public S3Client s3Client(AmazonSettings amazonSettings) {
        AwsCredentials credentials =
                AwsBasicCredentials.create(
                        amazonSettings.getAccessKey(),
                        amazonSettings.getSecretKey());

        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        return S3Client.builder()
                .endpointOverride(URI.create(amazonSettings.getHost()))
                .region(Region.AWS_GLOBAL)
                .credentialsProvider(credentialsProvider)
                .forcePathStyle(true) // access bucket by path (host/bucket) instead of url way (bucket.host)
                .overrideConfiguration(c ->
                        c.putAdvancedOption(SdkAdvancedClientOption.SIGNER, AwsS3V4Signer.create())
                )
                .build();
    }
}
