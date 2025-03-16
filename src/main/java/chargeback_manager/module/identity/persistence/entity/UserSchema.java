package chargeback_manager.module.identity.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.List;

@Document(collection = "users")
@JsonIgnoreProperties(ignoreUnknown = false)
public class UserSchema {

    @Id
    private String id;

    @Email
    private String email;

    private String name;

    private String password;

    private List<Role> roles;

    @CreatedDate
    private OffsetDateTime createdAt;

    @LastModifiedDate
    private OffsetDateTime updatedAt;
}
