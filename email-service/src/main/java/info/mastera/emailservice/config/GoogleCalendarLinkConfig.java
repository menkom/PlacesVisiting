package info.mastera.emailservice.config;

import info.mastera.googlecalendarevent.service.GoogleCalendarLinkService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleCalendarLinkConfig {

    @Bean
    public GoogleCalendarLinkService googleCalendarLinkService() {
        return new GoogleCalendarLinkService();
    }
}
