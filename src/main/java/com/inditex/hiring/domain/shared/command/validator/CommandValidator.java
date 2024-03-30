package com.inditex.hiring.domain.shared.command.validator;

import com.inditex.hiring.domain.shared.command.Command;
import com.inditex.hiring.domain.shared.command.CommandFailure;
import io.vavr.control.Either;

public interface CommandValidator<C extends Command> {

	Either<CommandFailure, C> acceptOrReject(C command);

}
