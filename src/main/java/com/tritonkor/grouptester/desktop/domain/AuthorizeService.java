package com.tritonkor.grouptester.desktop.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.net.controller.AuthController;
import com.tritonkor.grouptester.desktop.net.request.RegisterRequest;
import com.tritonkor.grouptester.desktop.net.request.UnauthorizeRequest;
import com.tritonkor.grouptester.desktop.persistence.entity.User;
import jakarta.annotation.PreDestroy;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeService extends com.tritonkor.grouptester.desktop.domain.Service {

    private static User currentUser;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean register(RegisterRequest request) {
        AuthController.register(requestToJson(request));

        authenticate(request.getUsername(), request.getPassword());

        return true;
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

    @PreDestroy
    public void unauthorize() {
        if (currentUser != null) {
            UnauthorizeRequest request = UnauthorizeRequest.builder()
                    .userId(currentUser.getId())
                    .build();

            AuthController.unauthorize(requestToJson(request));
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
