package dev.coursehub.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationForm {

    @NotBlank(message = "Enter a username.")
    @Pattern(
            regexp = "[A-Za-z0-9_]{3,50}",
            message = "Use 3 to 50 letters, digits, or underscores."
    )
    private String username;

    @NotBlank(message = "Enter your full name.")
    @Size(max = 100, message = "Use at most 100 characters.")
    private String fullName;

    @NotBlank(message = "Enter your email address.")
    @Email(message = "Enter a valid email address.")
    @Size(max = 254, message = "Use at most 254 characters.")
    private String email;

    @NotBlank(message = "Enter a password.")
    @Size(min = 12, message = "Use at least 12 characters.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}