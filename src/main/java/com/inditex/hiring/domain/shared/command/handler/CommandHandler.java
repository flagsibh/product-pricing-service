package com.inditex.hiring.domain.shared.command.handler;

import com.inditex.hiring.domain.shared.command.Command;
import com.inditex.hiring.domain.shared.command.CommandFailure;
import io.vavr.control.Either;

public interface CommandHandler<C extends Command, E> {

    Either<CommandFailure, E> handle(C command);
}
