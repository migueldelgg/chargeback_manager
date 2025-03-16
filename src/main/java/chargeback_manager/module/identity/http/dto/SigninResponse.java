package chargeback_manager.module.identity.http.dto;

public record SigninResponse(String accessToken, Long expiresIn) {
}
