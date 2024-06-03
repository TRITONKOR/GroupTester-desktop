package com.tritonkor.grouptester.desktop.presentation.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.persistence.entity.User;
import com.tritonkor.grouptester.desktop.persistence.entity.User.Role;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

/**
 * This class is a custom deserializer for converting JSON keys into User objects using Jackson's ObjectMapper.
 * It extends KeyDeserializer, which is part of Jackson's JSON serialization/deserialization framework.
 */
public class UserKeyDeserializer extends KeyDeserializer {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Deserializes the given JSON key into a User object.
     *
     * @param key     The JSON key to deserialize.
     * @param ctxt    The DeserializationContext.
     * @return        The User object deserialized from the JSON key.
     * @throws IOException              If an I/O error occurs during deserialization.
     * @throws JsonProcessingException If a JSON processing error occurs during deserialization.
     */
    @Override
    public User deserializeKey(String key, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = objectMapper.readTree(key);
        User user = User.builder()
                .id(UUID.fromString(node.get("id").asText()))
                .username(node.get("username").asText())
                .email(node.get("email").asText())
                .password("")
                .avatar(node.get("avatar").binaryValue())
                .birthday(LocalDate.parse(node.get("birthday").asText()))
                .role(Role.valueOf(node.get("role").asText()))
                .build();

        return user;
    }
}