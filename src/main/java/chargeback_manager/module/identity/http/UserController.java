package chargeback_manager.module.identity.http;

import chargeback_manager.module.identity.http.dto.signUp.SignUpRequest;
import chargeback_manager.module.identity.persistence.entity.Role;
import chargeback_manager.module.identity.persistence.entity.UserSchema;
import chargeback_manager.module.identity.persistence.port.UserRepositoryPort;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserRepositoryPort repository;

    @Autowired
    private final BCryptPasswordEncoder bcrypt;

    public UserController(BCryptPasswordEncoder bcrypt, UserRepositoryPort repository) {
        this.bcrypt = bcrypt;
        this.repository = repository;
    }

    @PostMapping("/signUp")
    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid SignUpRequest req) {
        var userFromDb = repository.findByEmail(req.email());

        // passar para dominio dps - passar o optional user como parametro
        // salvar o email toLowerCase
        if(userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Parar de pedir name, recordar o email ate o @, vai ficar name.lastName
        // Fazer split no ponto e ter o nome completo Name Lastname

        var user = new UserSchema(
                req.email(),
                req.name(),
                bcrypt.encode(req.password()),
                List.of(Role.BASIC)
        );
        repository.save(user);
    }

}
