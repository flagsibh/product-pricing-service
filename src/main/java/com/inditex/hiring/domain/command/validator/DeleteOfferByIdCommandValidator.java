package com.inditex.hiring.domain.command.validator;

import com.inditex.hiring.domain.command.DeleteOfferByIdCommand;
import com.inditex.hiring.domain.repository.OfferRepository;
import com.inditex.hiring.domain.shared.command.CommandFailure;
import com.inditex.hiring.domain.shared.command.validator.CommandValidator;
import com.inditex.hiring.domain.shared.exception.model.Error;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteOfferByIdCommandValidator implements CommandValidator<DeleteOfferByIdCommand> {

	private final OfferRepository repository;

	@Override
	public Either<CommandFailure, DeleteOfferByIdCommand> acceptOrReject(DeleteOfferByIdCommand command) {

		return Option.ofOptional(repository.findById(command.getId()))
		             .fold(() -> Either.left(CommandFailure.builder()
		                                                   .code(HttpStatus.NOT_FOUND.value())
		                                                   .error(Error.builder()
		                                                               .code(HttpStatus.NOT_FOUND.toString())
		                                                               .message("Offer not found")
		                                                               .build())
		                                                   .build()), offer -> Either.right(command));
	}
}
