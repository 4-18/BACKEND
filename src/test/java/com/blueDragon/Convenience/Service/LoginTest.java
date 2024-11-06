package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Repository.UserRepository;
import com.blueDragon.Convenience.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        userRepository.deleteAll();

        // Save a test user with encoded password
        User testUser = User.signupBuilder()
                .username("username")
                .nickname("nick")
                .password(passwordEncoder.encode("testPassword"))
                .build();

        userRepository.save(testUser);
    }

    @Test
    public void loginTest() throws Exception {
        // Login credentials
        String loginJson = """
                {
                  "username": "username",
                  "password": "testPassword"
                }
                """;

        // Perform login request
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("accessToken"))
                .andExpect(header().exists("refreshToken"))
                .andExpect(MockMvcResultMatchers.header().string("accessToken", org.hamcrest.Matchers.startsWith("Bearer ")))
                .andExpect(MockMvcResultMatchers.header().string("refreshToken", org.hamcrest.Matchers.startsWith("Bearer ")));
    }
}
