package com.tenpo.calculator.exception;

import com.tenpo.calculator.security.exception.InvalidArgumentException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles UsernameNotFoundException,LoginFailedException reports Login failed
     *
     * @param ex      UsernameNotFoundException, LoginFailedException
     * @param request
     * @return ResponseEntity with HTTP status 404
     */
    @ExceptionHandler({UsernameNotFoundException.class, LoginFailedException.class})
    public ResponseEntity<?> handleResourceNotFoundException(Exception ex, WebRequest request) {
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(new Date()).httpStatus(HttpStatus.NOT_FOUND).message(ex.getMessage()).build();
        return new ResponseEntity<>(
                errorDetail, new HttpHeaders(), errorDetail.getHttpStatus());
    }

    /**
     * Handles if cannot locate a User by its username and pass.
     *
     * @param ex      BadCredentialsException
     * @param request
     * @return ResponseEntity with HTTP status 400
     */
    @ExceptionHandler({BadCredentialsException.class, SignatureException.class, MalformedJwtException.class, UnsupportedJwtException.class})
    public ResponseEntity<?> handleBadCredentialException(Exception ex, WebRequest request) {
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(new Date()).httpStatus(HttpStatus.BAD_REQUEST).message(ex.getMessage()).build();
        return new ResponseEntity<>(
                errorDetail, new HttpHeaders(), errorDetail.getHttpStatus());
    }

    /**
     * Handles bad request exception
     *
     * @param ex      InvalidArgumentException, IllegalArgumentException, BadCredentialsException, MissingServletRequestParameterException
     * @param request
     * @return ResponseEntity with HTTP status 400
     */
    @ExceptionHandler({InvalidArgumentException.class})
    public ResponseEntity<?> handleBadRequestException(Exception ex, WebRequest request) {
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(new Date()).httpStatus(HttpStatus.BAD_REQUEST).message(ex.getMessage()).build();
        return new ResponseEntity<>(
                errorDetail, new HttpHeaders(), errorDetail.getHttpStatus());
    }



    /**
     * Handles ConstraintViolationException, reports the result of constraint violations
     *
     * @param ex      ConstraintViolationException
     * @param request
     * @return ResponseEntity with HTTP status 400
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(new Date()).httpStatus(HttpStatus.BAD_REQUEST).message("Validation ERROR").details(errors).build();
        return new ResponseEntity<>(
                errorDetail, new HttpHeaders(), errorDetail.getHttpStatus());
    }

    /**
     * Handles MethodArgumentNotValidException, thrown when argument annotated with @Valid failed validation
     *
     * @param ex      MethodArgumentNotValidException
     * @param request
     * @return ResponseEntity with HTTP status 400
     */

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(new Date()).httpStatus(HttpStatus.BAD_REQUEST).message("Validation ERROR").details(errors).build();
        return new ResponseEntity<>(
                errorDetail, new HttpHeaders(), errorDetail.getHttpStatus());
    }


    /**
     * Handles MethodArgumentTypeMismatchException, thrown when method argument is not the expected type
     *
     * @param ex      MethodArgumentTypeMismatchException
     * @param request
     * @return ResponseEntity with HTTP status 400
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(new Date()).httpStatus(HttpStatus.BAD_REQUEST).message(ex.getLocalizedMessage()).details(
                List.of(error)).build();
        return new ResponseEntity<>(
                errorDetail, new HttpHeaders(), errorDetail.getHttpStatus());
    }


    /**
     * Handle previously unhandled exceptions to keep response format
     *
     * @param ex      Exception
     * @param request
     * @return ResponseEntity with HTTP status 500
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleGlobalException(
            Exception ex, WebRequest request) {
        ErrorDetail errorDetail = ErrorDetail.builder().timestamp(new Date()).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message(ex.getMessage()).build();
        return new ResponseEntity<>(
                errorDetail, new HttpHeaders(), errorDetail.getHttpStatus());
    }
}
