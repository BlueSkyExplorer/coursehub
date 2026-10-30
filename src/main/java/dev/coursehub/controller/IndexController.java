package dev.coursehub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "CourseHub");
        model.addAttribute(
                "introduction",
                "A shared workspace for learning materials, resources, polls, and discussion."
        );

        return "index";
    }
}