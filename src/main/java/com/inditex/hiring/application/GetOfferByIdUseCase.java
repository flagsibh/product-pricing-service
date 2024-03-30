package com.inditex.hiring.application;

import com.inditex.hiring.application.mapper.OfferMapper;
import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.domain.query.GetOfferByIdQuery;
import com.inditex.hiring.domain.query.handler.GetOfferByIdQueryHandler;
import com.inditex.hiring.domain.shared.exception.DomainException;
import com.inditex.hiring.domain.shared.exception.TechnicalException;
import com.inditex.hiring.domain.shared.exception.model.Error;
import com.inditex.hiring.domain.shared.usecase.UseCase;
import io.vavr.control.Try;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class GetOfferByIdUseCase implements UseCase<GetOfferByIdQuery, Offer> {

	private final OfferMapper mapper;

	private final GetOfferByIdQueryHandler handler;

	@Override
	public Offer execute(GetOfferByIdQuery query) {

		return Try.of(() -> handler.handle(query))
		          .getOrElseThrow(throwable -> new TechnicalException(HttpStatus.SERVICE_UNAVAILABLE.value(),
				          "Error getting offer by id",
				          List.of(Error.builder()
				                       .code(HttpStatus.SERVICE_UNAVAILABLE.toString())
				                       .message(throwable.getMessage())
				                       .build()), throwable))
		          .fold(failure -> {
			          throw new DomainException(failure);
		          }, mapper::map);
	}

}
