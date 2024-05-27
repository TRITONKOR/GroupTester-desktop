package com.tritonkor.grouptester.desktop.domain;

import static com.tritonkor.grouptester.desktop.net.ApiUrls.AUTH_USER_URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.net.controller.AuthController;
import com.tritonkor.grouptester.desktop.net.request.RegisterRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.User;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeService {

    private static User currentUser;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean register(RegisterRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();

            String registerRequestJson = ow.writeValueAsString(request);
            AuthController.register(registerRequestJson);

            authenticate(request.getUsername(), request.getPassword());

            return true;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpResponse<String> authenticate(String username, String password) {
        try {
            objectMapper.registerModule(new JavaTimeModule());

            HttpResponse<String> response = AuthController.authenticate(username, password);
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                currentUser = objectMapper.readValue(jsonResponse, new TypeReference<User>() {});
            } else {
                // Обробка помилки або повернення порожнього списку
                throw new RuntimeException("Failed to fetch user: " + response.statusCode());
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
