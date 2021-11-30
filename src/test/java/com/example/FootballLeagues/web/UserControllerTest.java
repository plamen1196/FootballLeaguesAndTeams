package com.example.FootballLeagues.web;

import com.example.FootballLeagues.model.entity.User;
import com.example.FootballLeagues.model.entity.enums.UserRoleEnum;
import com.example.FootballLeagues.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WithUserDetails("admin")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    public User testUser;

    void initUser() {
        User user = new User();
        user.setUsername("testName");
        user.setFullName("Name");
        user.setPassword("12345");
        user.setActive(true);

        testUser = userRepository.save(user);
    }

    @Test
    void testGetChangeUserRoles() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/roles"))
                .andExpect(status().isOk())
                .andExpect(view().name("change-user-roles"))
                .andExpect(model().attributeExists("changeUserRole"))
                .andExpect(model().attributeExists("bad_credentials"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void testPatchUserRoles() throws Exception {
        initUser();
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/user/roles")
                        .param("username", testUser.getUsername())
                        .param("userRole", UserRoleEnum.ADMIN.name())
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeCount(0));

        User user = userRepository.findByUsername(testUser.getUsername()).orElse(null);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getRoles().size(), 2);

    }
}