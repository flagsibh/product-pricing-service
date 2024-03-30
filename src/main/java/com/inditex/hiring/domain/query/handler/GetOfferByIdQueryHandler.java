package com.inditex.hiring.domain.query.handler;

import com.inditex.hiring.domain.entity.Offer;
import com.inditex.hiring.domain.query.GetOfferByIdQuery;
import com.inditex.hiring.domain.repository.OfferRepository;
import com.inditex.hiring.domain.shared.exception.model.Error;
import com.inditex.hiring.domain.shared.query.QueryFailure;
import com.inditex.hiring.domain.shared.query.handler.QueryHandler;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class GetOfferByIdQueryHandler implements QueryHandler<GetOfferByIdQuery, Offer> {

	private final OfferRepository repository;

	@Override
	public Either<QueryFailure, Offer> handle(GetOfferByIdQuery query) {

		return Try.of(() -> repository.findById(query.getId()))
		          .fold(throwable -> {
			          log.error("Error getting offer by id {}", query.getId(), throwable);
			          return Either.left(QueryFailure.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
			                                         .error(Error.builder()
			                                                     .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
			                                                     .message(throwable.getMessage())
			                                                     .build())
			                                         .build());
		          }, offer -> offer.map(Either::<QueryFailure, Offer>right)
		                           .orElseGet(() -> {
			                           log.error("Offer with id {} not found", query.getId());
			                           return Either.left(
					                           QueryFailure.builder().code(HttpStatus.NOT_FOUND.value())
					                                       .error(Error.builder()
					                                                   .code(HttpStatus.NOT_FOUND.toString())
					                                                   .message("Offer not found")
					                                                   .build())
					                                       .build());
		                           }));
	}
}
