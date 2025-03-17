package chargeback_manager.infraestructre.config;

import chargeback_manager.module.identity.persistence.entity.Role;
import chargeback_manager.module.identity.persistence.entity.UserSchema;
import chargeback_manager.module.identity.persistence.port.UserRepositoryPort;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class AdminConfig implements CommandLineRunner {

    private UserRepositoryPort repository;

    private BCryptPasswordEncoder bcrypt;

    public AdminConfig(BCryptPasswordEncoder bcrypt, UserRepositoryPort repository) {
        this.bcrypt = bcrypt;
        this.repository = repository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<Role> roles = List.of(Role.ADMIN, Role.BASIC);
        var adminEmail = "miguel.delgado@zignet.com.br";
        var userAdmin = repository.findByEmail(adminEmail);
        userAdmin.ifPresentOrElse(
                (user) -> {
                    System.out.println("Admin ja existe: " + userAdmin.get().getEmail());
                },
                () -> {
                    var user = new UserSchema(
                            "miguel.delgado@zignet.com.br",
                            "Miguel Santos Delgado",
                            bcrypt.encode("Miguel007"),
                            roles
                    );
                    repository.save(user);
                    var admin = repository.findByEmail(adminEmail);
                    System.out.println("Usuario adicionado! "+ admin.toString());
                }
        );
    }
}
