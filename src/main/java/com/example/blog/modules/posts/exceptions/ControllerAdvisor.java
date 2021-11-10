package com.example.blog.modules.posts.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        body.put("timestamp", LocalDate.now());
        body.put("status", status.value());
        body.put("count", ex.getBindingResult().getErrorCount());
        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    //    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException exception,
//            HttpHeaders headers,
//            HttpStatus status,
//            WebRequest request) {
//        System.out.println("slm");
//        String bodyOfResponse = exception.getMessage();
//        return new ResponseEntity<>("slm", headers, status);
//    }


}
