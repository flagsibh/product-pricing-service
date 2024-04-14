package com.inditex.hiring.domain.query.handler;

import com.inditex.hiring.domain.entity.Offer;
import com.inditex.hiring.domain.mapper.OfferMapper;
import com.inditex.hiring.domain.mapper.OfferMapperImpl;
import com.inditex.hiring.domain.query.GetOffersPricePeriodsQuery;
import com.inditex.hiring.domain.repository.OfferRepository;
import com.inditex.hiring.domain.vo.OfferPeriod;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
class GetOffersPricePeriodsQueryHandlerTest {

	@Mock
	private OfferRepository repository;

	private GetOffersPricePeriodsQueryHandler handler;

	@BeforeEach
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		OfferMapper mapper = new OfferMapperImpl();
		handler = new GetOffersPricePeriodsQueryHandler(repository, mapper);
	}

	@Test
	void shouldReturnOffersWithNoOverlapWhenOffersDoNotOverlap() {
		// Given
		GetOffersPricePeriodsQuery query =
				GetOffersPricePeriodsQuery.builder().brandId(1).partNumber("0001002").build();
		Offer offer1 = Offer.builder()
		                    .startDate(OffsetDateTime.parse("2020-06-14T00:00:00Z"))
		                    .endDate(OffsetDateTime.parse("2020-06-14T14:59:59Z"))
		                    .price(BigDecimal.valueOf(45.50))
		                    .priority(1)
		                    .build();
		Offer offer2 = Offer.builder()
		                    .startDate(OffsetDateTime.parse("2020-06-14T15:00:00Z"))
		                    .endDate(OffsetDateTime.parse("2020-06-14T18:30:00Z"))
		                    .price(BigDecimal.valueOf(50.50))
		                    .priority(2)
		                    .build();
		List<Offer> offers = Arrays.asList(offer1, offer2);

		when(repository.findByBrandIdAndProductPartnumber(1, "0001002")).thenReturn(offers);

		// When
		List<OfferPeriod> result = handler.handle(query).get();

		// Then
		assertEquals(2, result.size());
		assertEquals(offer1.getStartDate(), result.get(0).getStartDate());
		assertEquals(offer1.getEndDate(), result.get(0).getEndDate());
		assertEquals(offer2.getStartDate(), result.get(1).getStartDate());
		assertEquals(offer2.getEndDate(), result.get(1).getEndDate());
	}

	@Test
	void shouldReturnFlattenedOffersWhenOffersOverlapWithSamePriority() {
		// Given
		GetOffersPricePeriodsQuery query =
				GetOffersPricePeriodsQuery.builder().brandId(1).partNumber("0001002").build();
		Offer offer1 = Offer.builder()
		                    .startDate(OffsetDateTime.parse("2020-06-14T00:00:00Z"))
		                    .endDate(OffsetDateTime.parse("2020-06-14T18:30:00Z"))
		                    .price(BigDecimal.valueOf(45.50))
		                    .priority(1)
		                    .build();
		Offer offer2 = Offer.builder()
		                    .startDate(OffsetDateTime.parse("2020-06-14T15:00:00Z"))
		                    .endDate(OffsetDateTime.parse("2020-06-14T18:30:00Z"))
		                    .price(BigDecimal.valueOf(50.50))
		                    .priority(1)
		                    .build();
		List<Offer> offers = Arrays.asList(offer1, offer2);

		when(repository.findByBrandIdAndProductPartnumber(1, "0001002")).thenReturn(offers);

		// When
		List<OfferPeriod> result = handler.handle(query).get();

		// Then
		assertEquals(2, result.size());
		assertEquals(offer1.getStartDate(), result.get(0).getStartDate());
		assertEquals(offer2.getStartDate().minusSeconds(1), result.get(0).getEndDate());
		assertEquals(offer2.getStartDate(), result.get(1).getStartDate());
		assertEquals(offer2.getEndDate(), result.get(1).getEndDate());
	}

	@Test
	void shouldReturnFlattenedOffersWhenOffersOverlapWithDifferentPriority() {
		// Given
		GetOffersPricePeriodsQuery query =
				GetOffersPricePeriodsQuery.builder().brandId(1).partNumber("0001002").build();
		Offer offer1 = Offer.builder()
		                    .startDate(OffsetDateTime.parse("2020-06-14T00:00:00Z"))
		                    .endDate(OffsetDateTime.parse("2020-06-14T18:30:00Z"))
		                    .price(BigDecimal.valueOf(45.50))
		                    .priority(1)
		                    .build();
		Offer offer2 = Offer.builder()
		                    .startDate(OffsetDateTime.parse("2020-06-14T15:00:00Z"))
		                    .endDate(OffsetDateTime.parse("2020-06-20T23:59:00Z"))
		                    .price(BigDecimal.valueOf(50.50))
		                    .priority(2)
		                    .build();
		List<Offer> offers = Arrays.asList(offer1, offer2);

		when(repository.findByBrandIdAndProductPartnumber(1, "0001002")).thenReturn(offers);

		// When
		List<OfferPeriod> result = handler.handle(query).get();

		printOffers(result);

		// Then
		assertEquals(3, result.size());
		assertEquals(offer1.getStartDate(), result.get(0).getStartDate());
		assertEquals(offer1.getPrice(), result.get(0).getPrice());
		assertEquals(offer2.getStartDate().minusSeconds(1), result.get(0).getEndDate());
		assertEquals(offer2.getStartDate(), result.get(1).getStartDate());
		assertEquals(offer2.getPrice(), result.get(1).getPrice());
		assertEquals(offer1.getEndDate().minusSeconds(1), result.get(1).getEndDate());
		assertEquals(offer1.getEndDate(), result.get(2).getStartDate());
		assertEquals(offer2.getPrice(), result.get(2).getPrice());
		assertEquals(offer2.getEndDate(), result.get(2).getEndDate());
	}

	private void printOffers(List<OfferPeriod> result) {

		result.forEach(
				offer -> log.info("startDate: {}, endDate: {}, price: {}",
						offer.getStartDate(), offer.getEndDate(), offer.getPrice()));
	}
}
