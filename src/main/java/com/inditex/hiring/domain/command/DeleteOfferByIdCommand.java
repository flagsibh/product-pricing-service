package com.inditex.hiring.domain.command;

import com.inditex.hiring.domain.shared.command.Command;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeleteOfferByIdCommand implements Command {

	Long id;
}
