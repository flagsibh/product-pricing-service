package com.inditex.hiring.domain.shared.exception;

import com.inditex.hiring.domain.shared.exception.model.Error;
import com.inditex.hiring.domain.shared.failure.Failure;

import java.util.List;

public class TechnicalException extends BaseException {

    public TechnicalException(String code, String message, List<? extends Error> errors, Throwable cause) {

        super(code, message, errors, cause);
    }

    public TechnicalException(Failure failure) {

        super(failure);
    }
}
