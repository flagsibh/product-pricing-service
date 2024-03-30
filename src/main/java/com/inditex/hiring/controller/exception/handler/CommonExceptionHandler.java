package com.inditex.hiring.controller.exception.handler;

import com.inditex.hiring.controller.exception.ErrorResponse;
import com.inditex.hiring.domain.shared.exception.BaseException;
import com.inditex.hiring.domain.shared.exception.model.Error;
import io.vavr.control.Try;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BaseException.class)
    protected ResponseEntity<ErrorResponse> handleBaseCommonExceptions(final BaseException ex,
                                                                       final HttpServletRequest request) {

        log.error("Code: {} , Message: {}", ex.getCode(), ex.getMessage());
        final HttpStatus httpStatus = evaluateHttpCode(ex);
        ErrorResponse errorResponse = handleErrorResponse(ex, request, httpStatus);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    protected HttpStatus evaluateHttpCode(final BaseException exception) {

        return Try.of(() -> HttpStatus.valueOf(exception.getCode()))
                .getOrElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ErrorResponse handleErrorResponse(final BaseException ex, final HttpServletRequest request,
                                                final HttpStatus httpStatus) {

        return handleErrorResponse(ex, request, httpStatus, ex.getMessage(), ex.getErrors());
    }

    protected ErrorResponse handleErrorResponse(Exception ex, HttpServletRequest request, HttpStatus httpStatus,
                                                String message, List<? extends Error> errorList) {

        errorList.stream()
                .filter(error -> !StringUtils.isBlank(error.getMessage()))
                .forEach(error -> error.setMessage(error.getMessage().replace('"', '\'')));

        return new ErrorResponse(
                httpStatus.value(),
                request.getRequestURI(),
                OffsetDateTime.now(),
                httpStatus.getReasonPhrase(),
                message != null ? message.replace('"', '\'') : null,
                errorList
        );
    }
}
