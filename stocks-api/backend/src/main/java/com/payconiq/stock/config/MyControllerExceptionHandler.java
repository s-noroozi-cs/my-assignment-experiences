package com.payconiq.stock.config;

import com.payconiq.stock.api.model.GlobalErrorResponse;
import com.payconiq.stock.exception.BadRequestException;
import com.payconiq.stock.exception.InternalServerError;
import com.payconiq.stock.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Saeid Noroozi
 * This class help to manage rest controller exception and validation
 * The goal of this class was define single point to manage api exception handler,
 * Also define uniform response model for all exception and validation scenarios.
 * Define simple class (com.payconiq.stock.api.model.GlobalErrorResponse ) that help to,
 * uniform all exception response that help client.
 */

@ControllerAdvice
public class MyControllerExceptionHandler extends ResponseEntityExceptionHandler {

    public static ResponseEntity<Object> handle(HttpStatus status, String message, String path, List<String> errors) {
        Object body = GlobalErrorResponse.builder()
                .status(status)
                .message(message)
                .details(errors)
                .path(path)
                .build();
        return new ResponseEntity(body, new HttpHeaders(), status);
    }

    public static ResponseEntity<Object> handle(HttpStatus status, String message, String path) {
        return handle(status, message, path, Collections.emptyList());
    }

    private String getRequestPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request) {
        return handle(HttpStatus.NOT_FOUND, ex.getMessage(), getRequestPath(request));
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(Exception ex, WebRequest request) {
        return handle(HttpStatus.BAD_REQUEST, ex.getMessage(), getRequestPath(request));
    }

    @ExceptionHandler({InternalServerError.class})
    public ResponseEntity<Object> handleInternalServerError(Exception ex, WebRequest request) {
        return handle(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), getRequestPath(request));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        return handle(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), getRequestPath(request), List.of("error occurred"));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return handle(HttpStatus.BAD_REQUEST, ex.getMessage(), getRequestPath(request), errors);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return handle(HttpStatus.BAD_REQUEST, ex.getMessage(), getRequestPath(request), List.of(error));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        return handle(HttpStatus.BAD_REQUEST, ex.getMessage(), getRequestPath(request), errors);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        return handle(HttpStatus.BAD_REQUEST, ex.getMessage(), getRequestPath(request), List.of(error));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        return handle(HttpStatus.NOT_FOUND, ex.getMessage(), getRequestPath(request), List.of(error));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
        return handle(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), getRequestPath(request), List.of(builder.toString()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));
        return handle(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage(), getRequestPath(request), List.of(builder.toString()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        String msg = "request body is not valid.";
        String error = ex.getMessage();
        return handle(HttpStatus.BAD_REQUEST, msg, getRequestPath(request), List.of(error));
    }
}
