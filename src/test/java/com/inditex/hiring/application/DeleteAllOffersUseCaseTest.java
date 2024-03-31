package com.inditex.hiring.application;

import com.inditex.hiring.domain.command.DeleteAllOffersCommand;
import com.inditex.hiring.domain.command.handler.DeleteAllOffersCommandHandler;
import com.inditex.hiring.domain.shared.command.CommandFailure;
import com.inditex.hiring.domain.shared.exception.DomainException;
import com.inditex.hiring.domain.shared.exception.TechnicalException;
import com.inditex.hiring.domain.shared.exception.model.Error;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeleteAllOffersUseCaseTest {

	private DeleteAllOffersCommandHandler handler;
	private DeleteAllOffersUseCase useCase;

	@BeforeEach
	public void setUp() {

		this.handler = Mockito.mock(DeleteAllOffersCommandHandler.class);
		this.useCase = new DeleteAllOffersUseCase(handler);
	}

	@Test
	void shouldDeleteAllOffersWhenCommandIsValid() {
		// Given
		DeleteAllOffersCommand command = DeleteAllOffersCommand.builder().build();

		when(handler.handle(command)).thenReturn(Either.right(null));

		// When
		Void result = useCase.execute(command);

		// Then	
		assertNull(result);
		verify(handler).handle(command);
	}

	@Test
	void shouldThrowTechnicalExceptionWhenHandlerThrowsException() {
		// Given
		DeleteAllOffersCommand command = DeleteAllOffersCommand.builder().build();

		RuntimeException exception = new RuntimeException("Handler exception");

		when(handler.handle(command)).thenThrow(exception);

		// When & Then
		TechnicalException thrownException = assertThrows(TechnicalException.class, () -> useCase.execute(command));

		assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), thrownException.getCode());
		assertEquals("Error deleting all offers", thrownException.getMessage());
		Error error = thrownException.getErrors().get(0);
		assertEquals(HttpStatus.SERVICE_UNAVAILABLE.toString(), error.getCode());
		assertEquals(exception.getMessage(), error.getMessage());
	}

	@Test
	void shouldThrowDomainExceptionWhenHandlerReturnsFailure() {
		// Given
		DeleteAllOffersCommand command = DeleteAllOffersCommand.builder().build();

		when(handler.handle(command)).thenReturn(Either.left(CommandFailure.builder()
		                                                                   .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
		                                                                   .error(Error.builder()
		                                                                               .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
		                                                                               .message(
				                                                                               "Error deleting all offers")
		                                                                               .build())
		                                                                   .build()));

		// When & Then
		assertThrows(DomainException.class, () -> useCase.execute(command));
	}
}