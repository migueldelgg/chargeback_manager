package chargeback_manager.module.identity.http;

import chargeback_manager.module.identity.http.dto.SigninRequest;
import chargeback_manager.module.identity.http.dto.SigninResponse;
import chargeback_manager.module.identity.persistence.port.UserRepositoryPort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class TokenController {

    @Autowired
    private final JwtEncoder jwtEncoder;
    private final UserRepositoryPort repository;
    private BCryptPasswordEncoder bcrypt;


    public TokenController(JwtEncoder jwtEncoder, UserRepositoryPort repository) {
        this.jwtEncoder = jwtEncoder;
        this.repository = repository;
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public SigninResponse signin(@RequestBody @Valid SigninRequest signinRequest) {
        var user = repository.findByEmail(signinRequest.email());

        if(user.isEmpty() || !user.get().isLoginCorrect(signinRequest, bcrypt)) {
            throw new BadCredentialsException("user or password is invalid");
        }

        var now = Instant.now();
        var expiresIn = 600L;

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.get().getId())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = "";
        return new SigninResponse(jwtValue, expiresIn);
    }

}
