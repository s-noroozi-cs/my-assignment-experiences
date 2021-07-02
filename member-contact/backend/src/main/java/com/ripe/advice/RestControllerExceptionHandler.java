package com.ripe.advice;

import com.ripe.controller.model.ExceptionResponseModel;
import com.ripe.exception.BadRequestException;
import com.ripe.exception.InternalServerError;
import com.ripe.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = BadRequestException.class)
    protected ResponseEntity<ExceptionResponseModel> handleBadRequest(RuntimeException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(makeModel(ex, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<ExceptionResponseModel> handleNotFound(RuntimeException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(makeModel(ex, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(value = InternalServerError.class)
    protected ResponseEntity<ExceptionResponseModel> handleInternalServerError(RuntimeException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(makeModel(ex, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private ExceptionResponseModel makeModel(RuntimeException ex, HttpStatus status) {
        ExceptionResponseModel model = new ExceptionResponseModel();
        model.setStatusText(String.valueOf(status.value()));
        model.setData(status.name() + ": " + ex.getMessage());
        return model;
    }
}
