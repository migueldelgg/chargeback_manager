package chargeback_manager.module.identity.persistence.adapter;

import chargeback_manager.module.identity.persistence.repository.UserRepositoryMongo;
import chargeback_manager.module.identity.persistence.entity.UserSchema;
import chargeback_manager.module.identity.persistence.port.UserRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserRepositoryAdapter implements UserRepositoryPort {

    @Autowired
    private final UserRepositoryMongo repositoryMongo;

    public UserRepositoryAdapter(UserRepositoryMongo repositoryMongo) {
        this.repositoryMongo = repositoryMongo;
    }

    @Override
    public UserSchema save(UserSchema user) {
        return repositoryMongo.save(user);
    }

    @Override
    public Optional<UserSchema> findById(String id) {
        return repositoryMongo.findById(id);
    }

    @Override
    public Optional<UserSchema> findByEmail(String email) {
        return repositoryMongo.findByEmail(email);
    }

    @Override
    public void deleteById(String id) {
        repositoryMongo.deleteById(id);
    }
}
