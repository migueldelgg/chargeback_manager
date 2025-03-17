package chargeback_manager.module.identity.http;

import chargeback_manager.module.identity.http.dto.signIn.SignInRequest;
import chargeback_manager.module.identity.http.dto.signIn.SignInResponse;
import chargeback_manager.module.identity.persistence.entity.Role;
import chargeback_manager.module.identity.persistence.port.UserRepositoryPort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    @Autowired
    private final JwtEncoder jwtEncoder;
    private final UserRepositoryPort repository;
    private BCryptPasswordEncoder bcrypt;

    public TokenController(JwtEncoder jwtEncoder, UserRepositoryPort repository, BCryptPasswordEncoder bcrypt) {
        this.jwtEncoder = jwtEncoder;
        this.repository = repository;
        this.bcrypt = bcrypt;
    }

    @PostMapping("/users/oauth2/token")
    @ResponseStatus(HttpStatus.OK)
    public SignInResponse signIn(@RequestBody @Valid SignInRequest signinRequest) {
        var user = repository.findByEmail(signinRequest.email());
        if(user.isEmpty() || !user.get().isLoginCorrect(signinRequest, bcrypt)) {
            throw new BadCredentialsException("user or password is invalid");
        }

        var now = Instant.now();
        var expiresIn = 600L;
        var userId = user.get().getId();

        var scopes = user.get().getRoles()
                .stream()
                .map(Role::name)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .id(userId)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims));
        return new SignInResponse(jwtValue.getTokenValue(), expiresIn);
    }
}
