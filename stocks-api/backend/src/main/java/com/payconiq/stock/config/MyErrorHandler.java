package com.payconiq.stock.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * In some scenario, my custom controller exception handler (com.payconiq.stock.config.MyControllerExceptionHandler),
 * did not catch exception or error (when not related to this controller such as not define path or etc),
 * This error handler catch error and try to uniform response model for it.
 * This handler help to client to see same object model for all exceptions and errors
 */

@Controller
public class MyErrorHandler implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        String msg = "Unknown or Unhandled Error in processing your request";

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                msg = "There is not exist any handler for your request";
                return MyControllerExceptionHandler.handle(HttpStatus.NOT_FOUND, msg, path);
            } else {
                return MyControllerExceptionHandler.handle(HttpStatus.valueOf(statusCode), msg, path);
            }
        }

        return MyControllerExceptionHandler.handle(HttpStatus.INTERNAL_SERVER_ERROR, msg, path);
    }
}
