package com.inditex.hiring.domain.shared.exception;

import com.inditex.hiring.domain.shared.exception.model.Error;
import com.inditex.hiring.domain.shared.failure.Failure;

import java.util.List;

public class DomainException extends BaseException {

    public DomainException(int code, String message, List<? extends Error> errors, Throwable cause) {

        super(code, message, errors, cause);
    }

    public DomainException(Failure failure) {

        super(failure);
    }
}
