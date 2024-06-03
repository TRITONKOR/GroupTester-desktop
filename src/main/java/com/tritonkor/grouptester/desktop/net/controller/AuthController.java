package com.tritonkor.grouptester.desktop.net.controller;

import static com.tritonkor.grouptester.desktop.net.ApiUrls.AUTH_USER_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.REGISTER_USER_URL;
import static com.tritonkor.grouptester.desktop.net.ApiUrls.UNAUTH_USER_URL;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.json.JSONObject;

/**
 * Controller class for handling authentication-related requests in the GroupTester desktop application.
 */
public class AuthController extends RequestController{
    /**
     * Registers a new user.
     *
     * @param registerRequestJson The JSON string containing the registration request data.
     * @return The HTTP response indicating the result of the registration request.
     */
    public static HttpResponse<String> register(String registerRequestJson) {
        return postRequest(REGISTER_USER_URL, registerRequestJson);
    }

    /**
     * Deauthorizes a user.
     *
     * @param unauthorizeRequestJson The JSON string containing the deauthorization request data.
     * @return The HTTP response indicating the result of the deauthorization request.
     */
    public static HttpResponse<String> unauthorize(String unauthorizeRequestJson) {
        return postRequest(UNAUTH_USER_URL, unauthorizeRequestJson);
    }

    /**
     * Authenticates a user with the given username and password.
     *
     * @param username The username of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return The HTTP response containing the result of the authentication attempt.
     */
    public static HttpResponse<String> authenticate(String username, String password) {
        try {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(AUTH_USER_URL))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(json.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            String responseBody = response.body();

            return response;
        } catch (Exception e) {
            return null;
        }
    }
}
