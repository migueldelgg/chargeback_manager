package chargeback_manager.module.identity.http.dto.signIn;

public record SignInResponse(String accessToken, Long expiresIn) {
}
