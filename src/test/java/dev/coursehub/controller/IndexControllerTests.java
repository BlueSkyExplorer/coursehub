package dev.coursehub.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import dev.coursehub.config.SecurityConfig;
import org.springframework.context.annotation.Import;

@WebMvcTest(IndexController.class)
@Import(SecurityConfig.class)
class IndexControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homePageUsesIndexViewAndProvidesIntroduction() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("pageTitle", "CourseHub"))
                .andExpect(model().attribute(
                        "introduction",
                        "A shared workspace for learning materials, resources, polls, and discussion."
                ));
    }
}