package info.mastera.authservice.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.MessageFormat;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        String message = "Required request body is missing or invalid";

        if (cause instanceof JsonParseException jpe) {
            message = jpe.getOriginalMessage();
        } else if (cause instanceof InvalidFormatException invalidFormatException) {
            message = MessageFormat.format("`{0}` has invalid format. `dd/MM/yyyy` format is expected.", invalidFormatException.getValue());
        }
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleGenericException(Exception e) {
        logger.error("Controller generic exception: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpServletResponse.SC_BAD_REQUEST)
                .body("Error on request processing. Contact support.");
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("Illegal argument exception: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpServletResponse.SC_BAD_REQUEST)
                .body("Can't process request.");
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<String> handleExpiredJwtException() {
        return ResponseEntity
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .body("Session expired. Login required.");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleMethodAgrumentException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body("Not valid value in fields:[" + message + "]");
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<String> handleEntityNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({IllegalAccessException.class})
    public ResponseEntity<String> handleIllegalAccessException() {
        return ResponseEntity.badRequest().body("Can't map new value to existing");
    }
}
