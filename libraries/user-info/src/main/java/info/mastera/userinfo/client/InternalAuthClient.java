package info.mastera.userinfo.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.mastera.userinfo.dto.AccountStatusRequest;
import info.mastera.userinfo.dto.AccountStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class InternalAuthClient {

    private static final Logger logger = LoggerFactory.getLogger(InternalAuthClient.class);

    private static final String X_INTERNAL_COMMUNICATION_TOKEN_HEADER = "X-INTERNAL-TOKEN";

    private final String token;
    private final String url;
    private final HttpClient internalAuthHttpClient;
    private final ObjectMapper objectMapper;

    public InternalAuthClient(
            @Value("${auth-internal-client.url}") String url,
            @Value("${auth-internal-client.token}") String token,
            HttpClient internalAuthHttpClient,
            ObjectMapper objectMapper) {
        this.url = url;
        this.token = token;
        this.internalAuthHttpClient = internalAuthHttpClient;
        this.objectMapper = objectMapper;
    }

    private HttpRequest buildRequest(@NonNull AccountStatusRequest request) throws JsonProcessingException {

        return HttpRequest.newBuilder(URI.create(url + "/api/account"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header(X_INTERNAL_COMMUNICATION_TOKEN_HEADER, token)
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(request)))
                .build();
    }

    public AccountStatusResponse getAccountStatus(@NonNull AccountStatusRequest request) {
        try {
            HttpResponse<String> response = internalAuthHttpClient.send(buildRequest(request), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return mapToObject(response);
            }
            logger.error("Unexpected response from authorization service. Request: {}. Response: {}.", request, response);
        } catch (IOException e) {
            logger.error("I/O operation failed. Request: {}.", request, e);
        } catch (InterruptedException e) {
            logger.error("Can't get response from authorization service. Request: {}.", request, e);
            if (Thread.interrupted()) {
                Thread.currentThread().interrupt();
            }
        }
        return new AccountStatusResponse();
    }

    private AccountStatusResponse mapToObject(HttpResponse<String> response) throws JsonProcessingException {
        try {
            return objectMapper.readValue(response.body(), AccountStatusResponse.class);
        } catch (JsonProcessingException e) {
            logger.error("Unexpected format of response body. Response: {}.", response, e);
            return null;
        }
    }
}
