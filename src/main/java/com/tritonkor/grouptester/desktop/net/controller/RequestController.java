package com.tritonkor.grouptester.desktop.net.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;

/**
 * Controller class for handling HTTP requests in the GroupTester desktop application.
 */
public class RequestController {
    /**
     * Sends a GET request to the specified URL with optional query parameters.
     *
     * @param url        The URL to send the GET request to.
     * @param parameters The map containing query parameters for the GET request.
     * @return The HTTP response from the server.
     */
    public static HttpResponse<String> getRequest(String url, Map<String, String> parameters) {
        StringBuilder stringBuilder = new StringBuilder(url);

        if (!parameters.isEmpty() & Objects.nonNull(parameters)) {
            stringBuilder.append("?");
            parameters.forEach((key, value) -> {
                value = value.replace(" ", "+");
                stringBuilder.append(key).append("=").append(value).append("&");
            });
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(stringBuilder.toString()))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            return sendRequest(request);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * Sends a POST request to the specified URL with the given request body.
     *
     * @param url  The URL to send the POST request to.
     * @param body The body of the POST request.
     * @return The HTTP response from the server.
     */
    public static HttpResponse<String> postRequest(String url, String body) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .POST(body==null?HttpRequest.BodyPublishers.noBody():HttpRequest.BodyPublishers.ofString(body))
                    .build();

            return sendRequest(request);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * Sends an HTTP request and retrieves the response.
     *
     * @param request The HTTP request to send.
     * @return The HTTP response from the server.
     */
    private static HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
