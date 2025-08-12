package gblas.books.backend.controller;

import static gblas.books.backend.util.RepositoryAssertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import gblas.books.backend.entity.UserEntity;
import gblas.books.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static gblas.books.backend.util.RepositoryAssertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private String registeredEmail;
    private String registeredPassword;
    private String registeredUsername;
    private String authToken;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        registeredEmail = "newemail@gmail.com";
        registeredPassword = "best_password";
        registeredUsername = "best_username";

        String registerRequest = String.format("""
        {
            "email": "%s",
            "password": "%s",
            "username": "%s"
        }
        """, registeredEmail, registeredPassword, registeredUsername);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(registeredEmail))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(registeredUsername))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        authToken = new ObjectMapper().readTree(responseBody).get("token").asText();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void register_withValidData_shouldCreateNewUser() throws Exception {
        String requestBody = "{ \"email\": \"email@gmail.com\", \"password\": \"best_password\", \"username\": \"best_username\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("best_username"));
        assertCountEquals(userRepository, 2);
        assertNotNull(userRepository.findByEmail(registeredEmail));

    }

    @Test
    void register_withUserAlreadyRegistered_shouldReturnConflictRequest() throws Exception {
        String registerRequest = String.format("""
        {
            "email": "%s",
            "password": "%s",
            "username": "%s"
        }
        """, registeredEmail, registeredPassword, registeredUsername);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email " + registeredEmail + " already exists."));
    }

    @Test
    void register_withInvalidEmailPasswordUsernameFormat_shouldReturnBadRequest() throws Exception {
        String requestBody = "{ \"email\": \"example.com\", \"password\": \"pass\", \"username\": \"short\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Invalid email format"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Password must be between 8 and 64 characters"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Username must be between 6 and 16 characters"));
    }

    @Test
    void register_withMissingRequiredParameters_shouldReturnBadRequest() throws Exception {
        String requestBody = "{}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Email is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Password is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Username is required"));

        requestBody = "";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Request body is missing or malformed"));
    }

    @Test
    void login_withValidCredentials_shouldCreateUserAndReturnAToken() throws Exception {
        String requestBody = String.format("""
        {
            "email": "%s",
            "password": "%s"
        }
        """, registeredEmail, registeredPassword);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(registeredEmail))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(registeredUsername));
    }

    @Test
    void login_withInvalidCredentials_shouldReturnUnauthorizedRequest() throws Exception {
        String requestBody = String.format("""
        {
            "email": "%s",
            "password": "wrong_%s"
        }
        """, registeredEmail, registeredPassword);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad credentials"));

        requestBody = String.format("""
        {
            "email": "wrong_%s",
            "password": "wrong_%s"
        }
        """, registeredEmail, registeredPassword);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad credentials"));
    }

    @Test
    void login_withMissingRequiredParameters_shouldReturnBadRequest() throws Exception {
        String requestBody = "{}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Email is required"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Password is required"));

        requestBody = "";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Request body is missing or malformed"));
    }

    @Test
    void login_withInvalidFormat_shouldReturnBadRequest() throws Exception {
        String requestBody = "{ \"email\": \"example.com\", \"password\": \"pass\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Invalid email format"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Password must be between 8 and 64 characters"));
    }

    @Test
    void me_withValidToken_shouldReturnUserData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk()).
                andExpect(MockMvcResultMatchers.jsonPath("$.email").value(registeredEmail))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(registeredUsername));
    }

    @Test
    void me_withMissingToken_shouldReturnUnauthorizedRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth")
                        .header("Authorization", "Bearer "))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete_withValidCredentials_shouldDeleteUser() throws Exception {
        String requestBody = String.format("""
        {
            "password": "%s"
        }
        """, registeredPassword);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
        assertFalse(userRepository.findAll().iterator().hasNext());
        assertIsEmpty(userRepository);
    }

    @Test
    void delete_withMissingPassword_shouldReturnBadRequest() throws Exception {
        String requestBody = "{}";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Password is required"));
        assertIsNotEmpty(userRepository);
    }

    @Test
    void delete_withInvalidPassword_shouldReturnUnauthorizedRequest() throws Exception {
        String requestBody = String.format("""
        {
            "password": "wrong_%s"
        }
        """, registeredPassword);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isUnauthorized());
        assertIsNotEmpty(userRepository);
    }
}