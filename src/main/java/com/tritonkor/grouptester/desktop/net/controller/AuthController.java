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

public class AuthController extends RequestController{
    public static HttpResponse<String> register(String registerRequestJson) {
        return postRequest(REGISTER_USER_URL, registerRequestJson);
    }

    public static HttpResponse<String> unauthorize(String unauthorizeRequestJson) {
        return postRequest(UNAUTH_USER_URL, unauthorizeRequestJson);
    }

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
