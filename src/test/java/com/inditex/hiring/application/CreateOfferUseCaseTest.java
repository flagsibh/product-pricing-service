package com.inditex.hiring.application;

import com.inditex.hiring.domain.command.CreateOfferCommand;
import com.inditex.hiring.domain.command.handler.CreateOfferCommandHandler;
import com.inditex.hiring.domain.entity.Offer;
import com.inditex.hiring.domain.shared.command.CommandFailure;
import com.inditex.hiring.domain.shared.exception.DomainException;
import com.inditex.hiring.domain.shared.exception.model.Error;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateOfferUseCaseTest {

	private CreateOfferCommandHandler handler;
	private CreateOfferUseCase useCase;

	@BeforeEach
	public void setUp() {

		this.handler = Mockito.mock(CreateOfferCommandHandler.class);
		this.useCase = new CreateOfferUseCase(handler);
	}

	@Test
	void shouldCreateOfferWhenInputIsValid() {
		// Arrange
		CreateOfferCommand input = CreateOfferCommand.builder()
		                                             .offerId(1L)
		                                             .brandId(1)
		                                             .startDate(OffsetDateTime.now())
		                                             .endDate(OffsetDateTime.now().plusDays(7))
		                                             .priceListId(1L)
		                                             .productPartnumber("ABC123")
		                                             .priority(1)
		                                             .price(BigDecimal.valueOf(9.99))
		                                             .currencyIso("USD")
		                                             .build();

		when(handler.handle(input)).thenReturn(Either.right(new Offer()));

		// Act
		Void result = useCase.execute(input);

		// Assert
		assertNull(result);
		verify(handler).handle(input);
	}

	@Test
	void shouldThrowDomainExceptionWhenHandlerReturnsCommandFailure() {
		// Arrange
		CreateOfferCommand input = CreateOfferCommand.builder()
		                                             .offerId(1L)
		                                             .brandId(1)
		                                             .startDate(OffsetDateTime.now())
		                                             .endDate(OffsetDateTime.now().plusDays(7))
		                                             .priceListId(1L)
		                                             .productPartnumber("ABC123")
		                                             .priority(1)
		                                             .price(BigDecimal.valueOf(9.99))
		                                             .currencyIso("USD")
		                                             .build();

		when(handler.handle(input)).thenReturn(Either.left(CommandFailure.builder()
		                                                                 .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
		                                                                 .error(Error.builder()
		                                                                             .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
		                                                                             .message("Error creating offer")
		                                                                             .build())
		                                                                 .build()));

		// Act & Assert
		assertThrows(DomainException.class, () -> useCase.execute(input));
		verify(handler).handle(input);
	}
}
