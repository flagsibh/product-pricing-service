package com.inditex.hiring.domain.shared.query.handler;

import com.inditex.hiring.domain.shared.query.Query;
import com.inditex.hiring.domain.shared.query.QueryFailure;
import io.vavr.control.Either;

public interface QueryHandler<Q extends Query, R> {

    Either<QueryFailure, R> handle(Q query);
}
