package dev.coursehub.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.test.autoconfigure.webmvc.SecurityMockMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(SecurityMockMvcAutoConfiguration.class)
@ActiveProfiles("test")
class RegistrationTests {

    private static final String TEST_PASSWORD = "CedarTrailExample2026";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository users;

    @Autowired
    private PasswordEncoder passwords;

    @BeforeEach
    void clearTestAccounts() {
        users.deleteAll();
    }

    @Test
    void visitorCanOpenRegistrationAndStylesheet() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/register"))
                .andExpect(model().attributeExists("registrationForm"));

        mvc.perform(get("/css/site.css")).andExpect(status().isOk());
        mvc.perform(get("/login")).andExpect(status().isForbidden());
    }

    @Test
    void createsLearnerWithBcryptEvenWhenRoleAndIdAreSubmitted() throws Exception {
        mvc.perform(registration("avery", "avery@example.com", TEST_PASSWORD)
                        .with(csrf())
                        .param("role", "INSTRUCTOR")
                        .param("id", "99999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("registrationSuccessful", true));

        User saved = users.findByUsername("avery").orElseThrow();
        assertThat(saved.getRole()).isEqualTo(Role.LEARNER);
        assertThat(saved.getId()).isNotNull().isNotEqualTo(99999L);
        assertThat(saved.getPasswordHash()).startsWith("$2").isNotEqualTo(TEST_PASSWORD);
        assertThat(passwords.matches(TEST_PASSWORD, saved.getPasswordHash())).isTrue();
        assertThat(users.count()).isEqualTo(1);
    }

    @Test
    void rejectsMissingFields() throws Exception {
        mvc.perform(post("/register").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/register"))
                .andExpect(model().attributeHasFieldErrors(
                        "registrationForm", "username", "fullName", "email", "password"
                ));

        assertThat(users.count()).isZero();
    }

    @Test
    void rejectsInvalidEmailAndShortPassword() throws Exception {
        mvc.perform(registration("avery", "invalid-email", "short").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors(
                        "registrationForm", "email", "password"
                ));

        assertThat(users.count()).isZero();
    }

    @Test
    void rejectsPasswordOverBcryptByteLimit() throws Exception {
        mvc.perform(registration("avery", "avery@example.com", "\u00E9".repeat(37)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "registrationForm", "password", "password.tooLong"
                ));

        assertThat(users.count()).isZero();
    }

    @Test
    void rejectsDuplicateUsernameAndEmailOnTheirRespectiveFields() throws Exception {
        mvc.perform(registration("avery", "avery@example.com", TEST_PASSWORD).with(csrf()))
                .andExpect(redirectedUrl("/"));

        mvc.perform(registration("avery", "morgan@example.com", TEST_PASSWORD).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "registrationForm", "username", "username.taken"
                ));

        mvc.perform(registration("morgan", "avery@example.com", TEST_PASSWORD).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode(
                        "registrationForm", "email", "email.taken"
                ));

        assertThat(users.count()).isEqualTo(1);
    }

    @Test
    void rejectsPostWithoutCsrfToken() throws Exception {
        mvc.perform(registration("avery", "avery@example.com", TEST_PASSWORD))
                .andExpect(status().isForbidden());

        assertThat(users.count()).isZero();
    }

    @Test
    void redirectsAuthenticatedUserToHome() throws Exception {
        mvc.perform(get("/register").with(user("existing")))
                .andExpect(redirectedUrl("/"));

        mvc.perform(registration("avery", "avery@example.com", TEST_PASSWORD)
                        .with(user("existing")).with(csrf()))
                .andExpect(redirectedUrl("/"));

        assertThat(users.count()).isZero();
    }

    private MockHttpServletRequestBuilder registration(
            String username, String email, String password
    ) {
        return post("/register")
                .param("username", username)
                .param("fullName", "Avery Lane")
                .param("email", email)
                .param("password", password);
    }
}