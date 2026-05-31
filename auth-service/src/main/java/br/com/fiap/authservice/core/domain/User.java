package br.com.fiap.authservice.core.domain;

import java.util.Collections;
import java.util.Set;

public class User {
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final Set<String> roles;

    private User(String username, String email, String firstName, String lastName, String password, Set<String> roles) {
        validate(username, email, firstName, lastName);
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.roles = roles != null ? Collections.unmodifiableSet(roles) : Collections.emptySet();
    }

    public static User create(String username, String email, String firstName, String lastName, String password) {
        return new User(username, email, firstName, lastName, password, Collections.singleton("user"));
    }

    public static User createWithRoles(String username, String email, String firstName, String lastName, 
                                       String password, Set<String> roles) {
        return new User(username, email, firstName, lastName, password, roles);
    }

    private static void validate(String username, String email, String firstName, String lastName) {
        if (username == null || username.length() < 3 || username.length() > 50) {
            throw new ValidationException("Username must be between 3 and 50 characters");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format");
        }
        if (firstName == null || firstName.length() > 80) {
            throw new ValidationException("First name must not exceed 80 characters");
        }
        if (lastName == null || lastName.length() > 80) {
            throw new ValidationException("Last name must not exceed 80 characters");
        }
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPassword() { return password; }
    public Set<String> getRoles() { return roles; }
}
