package com.tritonkor.grouptester.desktop.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.net.request.Request;

/**
 * Abstract class providing utility methods for services in the GroupTester desktop application.
 */
public abstract class Service {
    /**
     * Converts a Request object to its JSON representation.
     *
     * @param request The Request object to convert.
     * @return The JSON representation of the Request.
     * @throws RuntimeException if an error occurs during JSON processing.
     */
    public static String requestToJson(Request request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();

            return ow.writeValueAsString(request);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
