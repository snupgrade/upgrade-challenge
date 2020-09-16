package com.upgrade.challenge.commonservicelibrary.exception;

import java.util.concurrent.ExecutionException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import com.upgrade.challenge.commonservicelibrary.constant.CommonVariable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ServiceExceptionHandler {

    static Logger log = LoggerFactory.getLogger(ServiceExceptionHandler.class);

    @ExceptionHandler({ ExecutionException.class, InterruptedException.class })
    public ResponseEntity<Object> handleAsyncException(Exception ex, WebRequest request) throws Throwable {
        Throwable throwable = ex.getCause();
        if (throwable instanceof EntityNotFoundException) {
            return handleNotFoundException(new Exception(throwable), request);
        } else if (throwable instanceof ConstraintViolationException
                || throwable instanceof MethodArgumentNotValidException
                || throwable instanceof IllegalArgumentException) {
            return handleValidationException(new Exception(throwable), request);
        } else if (throwable instanceof EntityExistsException) {
            return handleEntityExistsException(new Exception(throwable), request);
        }
        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occured.", ex, request);
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(Exception ex, WebRequest request) {
        return createResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, "Method not supported.", ex, request);
    }

    @ExceptionHandler({ EntityNotFoundException.class, NoHandlerFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request) {
        return createResponseEntity(HttpStatus.NOT_FOUND, "Can't seem to find what you're looking for.", ex, request);
    }

    @ExceptionHandler({ ConstraintViolationException.class, MethodArgumentNotValidException.class,
            IllegalArgumentException.class })
    public ResponseEntity<Object> handleValidationException(Exception ex, WebRequest request) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, "A validation error occured.", ex, request);
    }

    @ExceptionHandler({ EntityExistsException.class })
    public ResponseEntity<Object> handleEntityExistsException(Exception ex, WebRequest request) {
        return createResponseEntity(HttpStatus.CONFLICT, "This request conflicts with an other ressource.", ex,
                request);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occured.", ex, request);
    }

    private ResponseEntity<Object> createResponseEntity(HttpStatus status, String message, Exception ex,
            WebRequest request) {
        log.error("Request error trackingId: {}, error: {}", request.getAttribute(CommonVariable.TRACKING_ID_HEADER, 0),
                ex.getMessage());
        ServiceError serviceError = new ServiceError(status, message);
        return new ResponseEntity<>(serviceError, serviceError.getStatus());
    }
}
