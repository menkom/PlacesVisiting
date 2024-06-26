package info.mastera.authservice.controller;

import info.mastera.authservice.dto.AccountStatusRequest;
import info.mastera.authservice.dto.LoginRequest;
import info.mastera.authservice.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    private static final String X_INTERNAL_COMMUNICATION_TOKEN_HEADER = "X-INTERNAL-TOKEN";

    private final AccountService accountService;
    private final String internalCommunicationToken;

    public AccountController(AccountService accountService,
                             @Value("${internal-communication-token}") String internalCommunicationToken
    ) {
        this.accountService = accountService;
        this.internalCommunicationToken = internalCommunicationToken;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createBasic(@RequestBody @Valid LoginRequest authRequest) {
        try {
            if (accountService.create(authRequest.username(), authRequest.password())) {
                return ResponseEntity.accepted().build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Can't create account");
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginBasic(@RequestBody @Valid LoginRequest authRequest) {
        try {
            return ResponseEntity.ok(accountService.login(authRequest.username(), authRequest.password()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(403));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> getAccountStatus(
            @RequestHeader(value = X_INTERNAL_COMMUNICATION_TOKEN_HEADER, required = false) final String accessToken,
            @RequestBody @Valid AccountStatusRequest request) {
        if (!internalCommunicationToken.equals(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Header token wrong or missing.");
        }
        return ResponseEntity.ok(accountService.getAccountStatus(request.username()));
    }
}
