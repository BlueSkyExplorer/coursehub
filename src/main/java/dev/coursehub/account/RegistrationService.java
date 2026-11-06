package dev.coursehub.account;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    public enum Result {
        CREATED,
        USERNAME_TAKEN,
        EMAIL_TAKEN
    }

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    public Result register(RegistrationForm form) {
        Optional<Result> conflict = findConflict(form);
        if (conflict.isPresent()) {
            return conflict.get();
        }

        User learner = new User(
                form.getUsername(),
                form.getFullName(),
                form.getEmail(),
                passwordEncoder.encode(form.getPassword()),
                Role.LEARNER
        );

        try {
            users.saveAndFlush(learner);
            return Result.CREATED;
        } catch (DataIntegrityViolationException failure) {
            return findConflict(form).orElseThrow(() -> failure);
        }
    }

    private Optional<Result> findConflict(RegistrationForm form) {
        if (users.existsByUsername(form.getUsername())) {
            return Optional.of(Result.USERNAME_TAKEN);
        }
        if (users.existsByEmail(form.getEmail())) {
            return Optional.of(Result.EMAIL_TAKEN);
        }
        return Optional.empty();
    }
}