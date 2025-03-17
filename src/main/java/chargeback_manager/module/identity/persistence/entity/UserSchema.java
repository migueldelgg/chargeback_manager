package chargeback_manager.module.identity.persistence.entity;

import chargeback_manager.module.identity.http.dto.signIn.SignInRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
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
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public UserSchema(String email, String name, String password, List<Role> roles) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public boolean isLoginCorrect
            (@Valid SignInRequest signinRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(signinRequest.password(), this.password);
    }

    public String getId() {
        return id;
    }

    public @Email String getEmail() {
        return email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "UserSchema{" +
                "createdAt=" + createdAt +
                ", id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
