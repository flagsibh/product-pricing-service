package com.inditex.hiring.domain.shared.exception.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Error {

    private String field;
    private String code;
    private String message;
}
