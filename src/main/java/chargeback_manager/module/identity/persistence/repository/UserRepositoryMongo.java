package chargeback_manager.module.identity.persistence.repository;

import chargeback_manager.module.identity.persistence.entity.UserSchema;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryMongo extends MongoRepository<UserSchema, String> {
    Optional<UserSchema> findByEmail(String email);
}
