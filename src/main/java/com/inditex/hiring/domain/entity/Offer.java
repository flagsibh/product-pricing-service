package com.inditex.hiring.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

	private Long offerId;
	private Integer brandId;
	private OffsetDateTime startDate;
	private OffsetDateTime endDate;
	private Long priceListId;
	private String productPartnumber;
	private Integer priority;
	private BigDecimal price;
	private String currencyIso;
}
