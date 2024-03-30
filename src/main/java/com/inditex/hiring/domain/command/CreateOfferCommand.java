package com.inditex.hiring.domain.command;

import com.inditex.hiring.domain.shared.command.Command;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Value
@Builder
public class CreateOfferCommand implements Command {

	Long offerId;
	Integer brandId;
	OffsetDateTime startDate;
	OffsetDateTime endDate;
	Long priceListId;
	String productPartnumber;
	Integer priority;
	BigDecimal price;
	String currencyIso;
}
