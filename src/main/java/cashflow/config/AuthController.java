package cashflow.config;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
public class AuthController {

    private final JwtService jwtService;


    @PostMapping("/api/token")
    public ResponseEntity<?> getToken(@RequestParam String username, @RequestParam String roles) {
        String token = jwtService.generateToken(username, List.of(roles));
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}

