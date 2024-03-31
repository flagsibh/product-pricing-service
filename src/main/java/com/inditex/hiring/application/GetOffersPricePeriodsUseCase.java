package com.inditex.hiring.application;

import com.inditex.hiring.application.mapper.OfferDtoMapper;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.domain.query.GetOffersPricePeriodsQuery;
import com.inditex.hiring.domain.query.handler.GetOffersPricePeriodsQueryHandler;
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
public class GetOffersPricePeriodsUseCase implements UseCase<GetOffersPricePeriodsQuery, List<OfferByPartNumber>> {

	private final GetOffersPricePeriodsQueryHandler handler;
	private final OfferDtoMapper mapper;

	@Override
	public List<OfferByPartNumber> execute(GetOffersPricePeriodsQuery input) {

		return Try.of(() -> handler.handle(input))
		          .getOrElseThrow(throwable -> new TechnicalException(HttpStatus.SERVICE_UNAVAILABLE.value(),
				          "Error getting offers price periods",
				          List.of(Error.builder()
				                       .code(HttpStatus.SERVICE_UNAVAILABLE.toString())
				                       .message(throwable.getMessage())
				                       .build()), throwable))
		          .fold(failure -> {
			          throw new DomainException(failure);
		          }, mapper::mapOfferPeriods);
	}
}
