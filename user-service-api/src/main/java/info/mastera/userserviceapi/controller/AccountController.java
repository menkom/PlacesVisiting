package info.mastera.userserviceapi.controller;

import info.mastera.userserviceapi.dto.LoginRequest;
import info.mastera.userserviceapi.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createBasic(@RequestBody @Valid LoginRequest authRequest) {
        try {
            if (accountService.create(authRequest.username(), authRequest.password())) {
                return ResponseEntity.accepted().build();
            }
            return ResponseEntity.badRequest().body("Can't create account");
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
}
