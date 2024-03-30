package com.inditex.hiring.domain.shared.exception;

import com.inditex.hiring.domain.shared.exception.model.Error;
import com.inditex.hiring.domain.shared.failure.Failure;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class BaseException extends RuntimeException {

	protected final int code;
	protected final transient List<? extends Error> errors;

	protected BaseException(int code, String message, List<? extends Error> errors, Throwable cause) {

		super(message, cause);
		this.code = code;
		this.errors = errors;
	}

	protected BaseException(Failure failure) {

		this(failure.getCode(), String.valueOf(failure.getCode()), failure.getErrors(), null);
	}
}
