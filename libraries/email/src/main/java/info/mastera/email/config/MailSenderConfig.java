package info.mastera.email.config;

import info.mastera.email.util.Validate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfig {
    private static final int DEFAULT_PORT = 0;
    private static final String DEFAULT_PROPERTY_VALUE = "OVERRIDE_VALUE";

    private final MailProperties mailProperties;

    public MailSenderConfig(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        Validate.notEmpty(mailProperties.getHost(), "Mail host can't be empty");
        Validate.isValid(mailProperties.getPort() != DEFAULT_PORT, "Mail port can't be empty");
        Validate.notEmpty(mailProperties.getUsername(), "Mail username can't be empty");
        Validate.isValid(!DEFAULT_PROPERTY_VALUE.equals(mailProperties.getUsername()), "Set username value for mail");
        Validate.notEmpty(mailProperties.getPassword(), "Mail password can't be empty");
        Validate.isValid(!DEFAULT_PROPERTY_VALUE.equals(mailProperties.getPassword()), "Set password value for mail");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.putAll(mailProperties.getProperties());

        return mailSender;
    }
}
