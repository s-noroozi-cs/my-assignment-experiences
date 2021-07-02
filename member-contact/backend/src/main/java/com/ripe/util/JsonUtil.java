package com.ripe.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class JsonUtil {
    private ObjectMapper objectMapper;

    @Autowired
    public JsonUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> Either<Throwable, T> deserialize(String jsonModel, Class<T> clazz) {
        Try<T> tryResult = Try.of(() -> objectMapper.readValue(jsonModel.getBytes(StandardCharsets.UTF_8), clazz));
        if (tryResult.isSuccess())
            return Either.right(tryResult.get());
        else
            return Either.left(tryResult.getCause());
    }
}
