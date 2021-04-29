package com.git.plushmarket.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> Optional<String> objToJsonString(T t) {
        String result = null;
        try {
            result = objectMapper.writeValueAsString(t);
        } catch (Throwable ex) {
            logger.error("Exception in serializing object, message: {}", ex.getMessage());
        } finally {
            return Optional.ofNullable(result);
        }
    }

    public static <T> Optional<T> jsonStringToObj(String json, Class<T> clazz) {
        T result = null;
        try {
            result = objectMapper.readValue(json, clazz);
        } catch (Throwable ex) {
            logger.error("Exception in deSerializing json String , message: {}", ex.getMessage());
        } finally {
            return Optional.ofNullable(result);
        }
    }

}
