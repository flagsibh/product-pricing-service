package com.inditex.hiring.domain.query.handler;

import com.inditex.hiring.domain.entity.Offer;
import com.inditex.hiring.domain.query.GetAllOffersQuery;
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

import java.util.List;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class GetAllOffersQueryHandler implements QueryHandler<GetAllOffersQuery, List<Offer>> {

	private final OfferRepository repository;

	@Override
	public Either<QueryFailure, List<Offer>> handle(GetAllOffersQuery query) {

		return Try.of(repository::findAll)
		          .fold(throwable -> {
			          log.error("Error getting offers", throwable);
			          return Either.left(QueryFailure.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
			                                         .error(Error.builder()
			                                                     .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
			                                                     .message(throwable.getMessage())
			                                                     .build())
			                                         .build());
		          }, Either::right);
	}
}
