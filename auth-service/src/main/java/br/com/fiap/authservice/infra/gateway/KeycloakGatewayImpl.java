package br.com.fiap.authservice.infra.gateway;

import br.com.fiap.authservice.core.domain.LoginResult;
import br.com.fiap.authservice.core.domain.User;
import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;

@Service
public class KeycloakGatewayImpl implements IdentityProviderGateway {
    private final Keycloak keycloak;
    private final String realm;
    private final String clientId;
    private final String clientSecret;
    private final String authServerUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KeycloakGatewayImpl(
            Keycloak keycloak,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.resource}") String clientId,
            @Value("${keycloak.credentials-secret}") String clientSecret,
            @Value("${keycloak.auth-server-url}") String authServerUrl,
            RestTemplate restTemplate
    ) {
        this.keycloak = keycloak;
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authServerUrl = authServerUrl;
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void createUser(User user) {
        try {
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Create user representation
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(user.getUsername());
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            userRepresentation.setEnabled(true);

            // Create user
            Response response = usersResource.create(userRepresentation);
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create user: " + response.getStatus());
            }

            // Set password
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(user.getPassword());

            usersResource.get(userId).resetPassword(passwordCred);

            // Assign roles
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                var roles = user.getRoles().stream()
                        .map(roleName -> realmResource.roles().get(roleName).toRepresentation())
                        .toList();
                usersResource.get(userId).roles().realmLevel().add(roles);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating user in Keycloak", e);
        }
    }

    @Override
    public LoginResult login(String username, String password) {
        try {
            String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            String body = "grant_type=password&client_id=" + clientId + "&client_secret=" + clientSecret
                    + "&username=" + username + "&password=" + password;

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(body, headers);
            org.springframework.http.ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to login: " + response.getStatusCode());
            }

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return new LoginResult(
                    jsonNode.get("access_token").asText(),
                    jsonNode.get("refresh_token").asText(),
                    jsonNode.get("expires_in").asLong(),
                    jsonNode.get("token_type").asText()
            );
        } catch (IOException e) {
            throw new RuntimeException("Error parsing login response", e);
        }
    }

    @Override
    public LoginResult refreshToken(String refreshToken) {
        try {
            String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            String body = "grant_type=refresh_token&client_id=" + clientId + "&client_secret=" + clientSecret
                    + "&refresh_token=" + refreshToken;

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(body, headers);
            org.springframework.http.ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to refresh token");
            }

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return new LoginResult(
                    jsonNode.get("access_token").asText(),
                    jsonNode.get("refresh_token").asText(),
                    jsonNode.get("expires_in").asLong(),
                    jsonNode.get("token_type").asText()
            );
        } catch (IOException e) {
            throw new RuntimeException("Error parsing refresh token response", e);
        }
    }

    @Override
    public void logout(String token) {
        // Token is added to blacklist in TokenBlacklistGateway
        // Keycloak doesn't require explicit logout, token management is handled via blacklist
    }
}
