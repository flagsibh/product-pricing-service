package com.inditex.hiring.application;

import com.inditex.hiring.domain.command.CreateOfferCommand;
import com.inditex.hiring.domain.command.handler.CreateOfferCommandHandler;
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
public class CreateOfferUseCase implements UseCase<CreateOfferCommand, Void> {

	private final CreateOfferCommandHandler handler;

	@Override
	public Void execute(CreateOfferCommand input) {

		return Try.of(() -> handler.handle(input))
		          .getOrElseThrow(throwable -> new TechnicalException(HttpStatus.SERVICE_UNAVAILABLE.value(),
				          "Error creating offer",
				          List.of(Error.builder()
				                       .code(HttpStatus.SERVICE_UNAVAILABLE.toString())
				                       .message(throwable.getMessage())
				                       .build()), throwable))
		          .fold(failure -> {
			          throw new DomainException(failure);
		          }, offer -> null);
	}
}
