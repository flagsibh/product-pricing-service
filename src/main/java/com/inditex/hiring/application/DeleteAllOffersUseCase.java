package com.inditex.hiring.application;

import com.inditex.hiring.domain.command.DeleteAllOffersCommand;
import com.inditex.hiring.domain.command.handler.DeleteAllOffersCommandHandler;
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
public class DeleteAllOffersUseCase implements UseCase<DeleteAllOffersCommand, Void> {

	private final DeleteAllOffersCommandHandler handler;

	@Override
	public Void execute(DeleteAllOffersCommand input) {

		return Try.of(() -> handler.handle(input))
		          .getOrElseThrow(throwable -> new TechnicalException(HttpStatus.SERVICE_UNAVAILABLE.value(),
				          "Error deleting all offers",
				          List.of(Error.builder()
				                       .code(HttpStatus.SERVICE_UNAVAILABLE.toString())
				                       .message(throwable.getMessage())
				                       .build()), throwable))
		          .fold(failure -> {
			          throw new DomainException(failure);
		          }, (Void v) -> null);
	}
}
