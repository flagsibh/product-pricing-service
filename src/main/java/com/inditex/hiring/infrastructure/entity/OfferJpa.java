package com.inditex.hiring.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@Entity
@Table(name = "offer")
@NoArgsConstructor
@AllArgsConstructor
public class OfferJpa {

	@Id
	@Column(name = "OFFER_ID", nullable = false)
	private Long offerId;

	@Column(name = "BRAND_ID", nullable = false)
	private Integer brandId;

	@Column(name = "START_DATE", nullable = false)
	private OffsetDateTime startDate;

	@Column(name = "END_DATE", nullable = false)
	private OffsetDateTime endDate;

	@Column(name = "PRICE_LIST_ID", nullable = false)
	private Long priceListId;

	@Column(name = "PRODUCT_PARTNUMBER", nullable = false)
	private String productPartnumber;

	@Column(name = "PRIORITY", nullable = false)
	private Integer priority;

	@Column(name = "PRICE", nullable = false)
	private BigDecimal price;

	@Column(name = "CURRENCY_ISO", nullable = false)
	private String currencyIso;
}
