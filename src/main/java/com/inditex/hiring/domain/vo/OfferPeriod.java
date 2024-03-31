package com.inditex.hiring.domain.vo;

import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Value(staticConstructor = "of")
public class OfferPeriod {

	OffsetDateTime startDate;
	OffsetDateTime endDate;
	BigDecimal price;
	String currencyIso;
}
