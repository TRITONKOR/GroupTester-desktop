package com.tritonkor.grouptester.desktop.presentation.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tritonkor.grouptester.desktop.persistence.entity.User;
import com.tritonkor.grouptester.desktop.persistence.entity.User.Role;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

public class UserKeyDeserializer extends KeyDeserializer {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

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