package com.inditex.hiring.controller.exception;

import com.inditex.hiring.domain.shared.exception.model.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse {

    int status;
    String path;
    OffsetDateTime timestamp;
    String error;
    String message;
    List<? extends Error> errors;

}
