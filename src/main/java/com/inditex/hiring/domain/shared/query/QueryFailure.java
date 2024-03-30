package com.inditex.hiring.domain.shared.query;

import com.inditex.hiring.domain.shared.failure.Failure;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class QueryFailure extends Failure {
}
