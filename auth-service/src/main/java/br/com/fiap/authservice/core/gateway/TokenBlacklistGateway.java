package br.com.fiap.authservice.core.gateway;

public interface TokenBlacklistGateway {
    void addToBlacklist(String token, long expiresIn);
    boolean isBlacklisted(String token);
}
