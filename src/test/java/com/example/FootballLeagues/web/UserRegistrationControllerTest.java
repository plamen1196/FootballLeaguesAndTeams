package com.example.FootballLeagues.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserRegistrationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testOpenRegisterForm() throws Exception {
        mockMvc.
                perform(MockMvcRequestBuilders.get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }
}