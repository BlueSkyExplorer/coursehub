package dev.coursehub.account;

import java.nio.charset.StandardCharsets;
import java.security.Principal;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    private static final String FORM_VIEW = "account/register";

    private final RegistrationService registration;

    public RegistrationController(RegistrationService registration) {
        this.registration = registration;
    }

    @InitBinder("registrationForm")
    void configureBinding(WebDataBinder binder) {
        binder.setAllowedFields("username", "fullName", "email", "password");
    }

    @GetMapping("/register")
    String showForm(Principal principal, Model model) {
        if (principal != null) {
            return "redirect:/";
        }

        model.addAttribute("registrationForm", new RegistrationForm());
        return showRegistrationPage(model);
    }

    @PostMapping("/register")
    String register(
            Principal principal,
            @Valid @ModelAttribute("registrationForm") RegistrationForm form,
            BindingResult errors,
            Model model,
            RedirectAttributes redirect
    ) {
        if (principal != null) {
            return "redirect:/";
        }

        String password = form.getPassword();
        if (!errors.hasFieldErrors("password") && password != null
                && password.getBytes(StandardCharsets.UTF_8).length > 72) {
            errors.rejectValue(
                    "password", "password.tooLong", "Use at most 72 bytes in your password."
            );
        }

        if (errors.hasErrors()) {
            return showRegistrationPage(model);
        }

        switch (registration.register(form)) {
            case USERNAME_TAKEN -> errors.rejectValue(
                    "username", "username.taken", "This username is already in use."
            );
            case EMAIL_TAKEN -> errors.rejectValue(
                    "email", "email.taken", "This email address is already in use."
            );
            case CREATED -> {
                redirect.addFlashAttribute("registrationSuccessful", true);
                return "redirect:/";
            }
        }

        return showRegistrationPage(model);
    }

    private String showRegistrationPage(Model model) {
        model.addAttribute("pageTitle", "Create an account | CourseHub");
        return FORM_VIEW;
    }
}