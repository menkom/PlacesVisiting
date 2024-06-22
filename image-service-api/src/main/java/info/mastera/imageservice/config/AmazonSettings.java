package info.mastera.imageservice.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "amazon.s3")
public class AmazonSettings {

    private String host;
    private String region;
    private String accessKey;
    private String secretKey;
    private String bucket;

    public String getHost() {
        return host;
    }

    public AmazonSettings setHost(String host) {
        this.host = host;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public AmazonSettings setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public AmazonSettings setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public AmazonSettings setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public AmazonSettings setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }
}
