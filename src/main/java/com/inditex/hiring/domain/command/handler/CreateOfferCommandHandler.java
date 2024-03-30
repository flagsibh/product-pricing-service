package com.inditex.hiring.domain.command.handler;

import com.inditex.hiring.domain.command.CreateOfferCommand;
import com.inditex.hiring.domain.entity.Offer;
import com.inditex.hiring.domain.mapper.OfferMapper;
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
public class CreateOfferCommandHandler implements CommandHandler<CreateOfferCommand, Offer> {

	private final OfferRepository repository;
	private final OfferMapper mapper;

	@Override
	public Either<CommandFailure, Offer> handle(CreateOfferCommand command) {

		return Try.of(() -> repository.create(mapper.map(command)))
		          .fold(throwable -> {
			          log.error("Error creating offer", throwable);
			          return Either.left(CommandFailure.builder()
			                                           .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
			                                           .error(Error.builder()
			                                                       .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
			                                                       .message(throwable.getMessage())
			                                                       .build())
			                                           .build());
		          }, Either::right);
	}
}
