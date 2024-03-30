package com.inditex.hiring.domain.shared.failure;

import com.inditex.hiring.domain.shared.exception.model.Error;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class Failure {
    
    final String code;
    @Singular
    final List<Error> errors;
    @Singular
    final List<String> arguments;
}
