package BtRestIa.BTRES.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BtRestIa.BTRES.application.service.AuthUseCase;
import BtRestIa.BTRES.application.service.RegisterUseCase;
import BtRestIa.BTRES.infrastructure.dto.request.LoginResquestDto;
import BtRestIa.BTRES.infrastructure.dto.request.RegisterRequestDto;
import BtRestIa.BTRES.infrastructure.dto.response.JwtResponseDto;
import BtRestIa.BTRES.infrastructure.security.RSAKeyManager;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthUseCase authUseCase;
    private final RegisterUseCase registerUseCase;
    private final RSAKeyManager rsaKeyManager;

    public AuthController(AuthUseCase authUseCase, RegisterUseCase registerUseCase, RSAKeyManager rsaKeyManager) {
        this.authUseCase = authUseCase;
        this.registerUseCase = registerUseCase;
        this.rsaKeyManager = rsaKeyManager;
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDto> register (@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        System.out.println(registerRequestDto);
       registerUseCase.register(registerRequestDto);
        return ResponseEntity.status(201).build();
    }

     @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginResquestDto loginResquestDto) {
        return ResponseEntity.ok(authUseCase.login(loginResquestDto));
    }

    @GetMapping("/public-key")
    public ResponseEntity<String> getPublicKey() {
        try {
            return ResponseEntity.ok(rsaKeyManager.getKeyFromResource("/keys/public_key.pem"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error loading public key");
        }
    }

}
