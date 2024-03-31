package com.inditex.hiring.application;

import com.inditex.hiring.application.mapper.OfferDtoMapper;
import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.domain.query.GetOffersPricePeriodsQuery;
import com.inditex.hiring.domain.query.handler.GetOffersPricePeriodsQueryHandler;
import com.inditex.hiring.domain.shared.exception.TechnicalException;
import com.inditex.hiring.domain.vo.OfferPeriod;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class GetOffersPricePeriodsUseCaseTest {

	private GetOffersPricePeriodsQueryHandler handler;
	private OfferDtoMapper mapper;
	private GetOffersPricePeriodsUseCase useCase;

	@BeforeEach
	public void setUp() {

		this.handler = Mockito.mock(GetOffersPricePeriodsQueryHandler.class);
		this.mapper = Mappers.getMapper(OfferDtoMapper.class);
		this.useCase = new GetOffersPricePeriodsUseCase(handler, mapper);
	}

	@Test
	void shouldReturnListOfOffersWhenInputIsValid() {
		// Given
		GetOffersPricePeriodsQuery query = GetOffersPricePeriodsQuery.builder()
		                                                             .brandId(1)
		                                                             .partNumber("1234")
		                                                             .build();

		var offerPeriod = OfferPeriod.of(OffsetDateTime.parse("2020-06-15T16:00:00Z"),
				OffsetDateTime.parse("2020-06-15T18:00:00Z"), BigDecimal.valueOf(10.0), "EUR");
		var offer = new Offer(1L, 1, "2020-06-15T16:00:00Z", "2020-06-15T18:00:00Z", 1L, "1234", 1,
				BigDecimal.valueOf(10.0), "EUR");

		List<OfferByPartNumber> expected = Collections.singletonList(mapper.map(offerPeriod));

		when(handler.handle(query)).thenReturn(Either.right(Collections.singletonList(offerPeriod)));

		// When
		List<OfferByPartNumber> actual = useCase.execute(query);

		// Then
		assertEquals(expected.size(), actual.size());
		assertEquals(expected.get(0).getStartDate(), actual.get(0).getStartDate());
		assertEquals(expected.get(0).getEndDate(), actual.get(0).getEndDate());
	}

	@Test
	void shouldThrowTechnicalExceptionWhenHandlerThrowsException() {
		// Given
		GetOffersPricePeriodsQuery query = GetOffersPricePeriodsQuery.builder()
		                                                             .brandId(1)
		                                                             .partNumber("1234")
		                                                             .build();

		RuntimeException exception = new RuntimeException("Handler exception");

		when(handler.handle(query)).thenThrow(exception);

		// When
		TechnicalException throwable = assertThrows(TechnicalException.class, () -> useCase.execute(query));

		// Then
		assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), throwable.getCode());
		assertEquals("Error getting offers price periods", throwable.getMessage());
		assertEquals(1, throwable.getErrors().size());
	}
}
