package com.example.FootballLeagues.web;

import org.junit.jupiter.api.Test;
import org.mockito.plugins.MockMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class UserLoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testOpenLoginForm() throws Exception {
        mockMvc.
                perform(MockMvcRequestBuilders.get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}