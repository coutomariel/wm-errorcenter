package com.warmachine.errorcenterapi.base.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BaseControllerTest {

    private InputStream loadResourceAsInputStream(String pathAsString) {
        try {
            return new ClassPathResource(pathAsString).getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("error loading resource " + pathAsString, e);
        }
    }

    protected JsonNode loadAsJsonFromResource(String jsonFilePath) {
        try {
            return new ObjectMapper().readTree(loadResourceAsInputStream(jsonFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String loadResourceAsString(String pathAsString) {
        try {
            return IOUtils.toString(loadResourceAsInputStream(pathAsString), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("error loading resource " + pathAsString, e);
        }
    }
}
