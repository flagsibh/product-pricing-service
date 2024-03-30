package com.inditex.hiring.domain.command.handler;

import com.inditex.hiring.domain.command.DeleteAllOffersCommand;
import com.inditex.hiring.domain.repository.OfferRepository;
import com.inditex.hiring.domain.shared.command.CommandFailure;
import com.inditex.hiring.domain.shared.command.handler.CommandHandler;
import com.inditex.hiring.domain.shared.exception.model.Error;
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
public class DeleteAllOffersCommandHandler implements CommandHandler<DeleteAllOffersCommand, Void> {

	private final OfferRepository repository;

	@Override
	public Either<CommandFailure, Void> handle(DeleteAllOffersCommand command) {

		return Try.of(() -> {
			repository.deleteAll();
			return Either.<CommandFailure, Void>right(null);
		}).getOrElseGet(throwable -> {
			log.error("Error deleting all offers", throwable);
			return Either.left(CommandFailure.builder()
			                                 .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
			                                 .error(Error.builder()
			                                             .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
			                                             .message(throwable.getMessage())
			                                             .build())
			                                 .build());
		});
	}
}
