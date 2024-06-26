package info.mastera.userserviceapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {

    @GetMapping("test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("You are authenticated");
    }

    @GetMapping
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("No need authentication");
    }

    @GetMapping("/principal")
    public Object getPrincipal(Authentication authentication) {
        return authentication.getPrincipal();
    }
}
