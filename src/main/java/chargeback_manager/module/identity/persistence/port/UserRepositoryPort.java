package chargeback_manager.module.identity.persistence.port;

import chargeback_manager.module.identity.persistence.entity.UserSchema;

import java.util.Optional;

public interface UserRepositoryPort {
    UserSchema save(UserSchema user);
    Optional<UserSchema> findById(String id);
    Optional<UserSchema> findByEmail(String email);
    void deleteById(String id);
}
