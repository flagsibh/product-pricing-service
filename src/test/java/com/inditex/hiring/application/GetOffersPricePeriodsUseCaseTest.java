package com.inditex.hiring.application;

import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetOffersPricePeriodsUseCaseTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	public void setUp() {

		// call the endpoint /offers to delete all offers
		restTemplate.delete("/offers");
	}

	@Test
	void shouldReturnEmptyListOfOffers() {
		// Call the endpoint /offers to get all offers
		ResponseEntity<List<Offer>> response = restTemplate.exchange(
				"/offers",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {

				});

		// Convert the response to a list of offers
		List<Offer> offers = response.getBody();

		// Assert that the list is not empty
		assertNotNull(offers);
		assertTrue(offers.isEmpty());
	}

	@Test
	void testCaseOne() {
		// Create the offers
		createOffer(1L, "2022-01-01T00.00.00Z", "2022-06-30T23.59.59Z", 1L, 1, BigDecimal.valueOf(45.50));
		createOffer(2L, "2022-02-01T15.00.00Z", "2022-02-01T18.30.00Z", 2L, 2, BigDecimal.valueOf(35.45));
		createOffer(3L, "2022-03-01T00.00.00Z", "2022-03-01T11.00.00Z", 3L, 0, BigDecimal.valueOf(40.50));
		createOffer(4L, "2022-04-01T16.00.00Z", "2022-12-31T23.59.59Z", 4L, 1, BigDecimal.valueOf(48.95));

		// Call the endpoint /offers/brand/{brandId}/partnumber/{partnumber} to get offers by part number
		ResponseEntity<List<OfferByPartNumber>> response = restTemplate.exchange(
				"/offers/brand/1/partnumber/0001002",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {

				});

		// Get the response
		List<OfferByPartNumber> offers = response.getBody();

		// Validate that the response is not empty
		assertNotNull(offers);
		assertFalse(offers.isEmpty());

		// Validate the values of the offers in the response
		assertEquals(6, offers.size());
		assertEquals("2022-01-01T00:00:00Z", offers.get(0).getStartDate());
		assertEquals("2022-02-01T14:59:59Z", offers.get(0).getEndDate());
		assertEquals(0, BigDecimal.valueOf(45.50).compareTo(offers.get(0).getPrice()));

		assertEquals("2022-02-01T15:00:00Z", offers.get(1).getStartDate());
		assertEquals("2022-02-01T18:29:59Z", offers.get(1).getEndDate());
		assertEquals(0, BigDecimal.valueOf(35.45).compareTo(offers.get(1).getPrice()));

		assertEquals("2022-02-01T18:30:00Z", offers.get(2).getStartDate());
		assertEquals("2022-02-28T23:59:59Z", offers.get(2).getEndDate());
		assertEquals(0, BigDecimal.valueOf(45.50).compareTo(offers.get(2).getPrice()));

		assertEquals("2022-03-01T00:00:00Z", offers.get(3).getStartDate());
		assertEquals("2022-03-01T10:59:59Z", offers.get(3).getEndDate());
		assertEquals(0, BigDecimal.valueOf(45.50).compareTo(offers.get(3).getPrice()));

		assertEquals("2022-03-01T11:00:00Z", offers.get(4).getStartDate());
		assertEquals("2022-04-01T15:59:59Z", offers.get(4).getEndDate());
		assertEquals(0, BigDecimal.valueOf(45.50).compareTo(offers.get(4).getPrice()));

		assertEquals("2022-04-01T16:00:00Z", offers.get(5).getStartDate());
		assertEquals("2022-12-31T23:59:59Z", offers.get(5).getEndDate());
		assertEquals(0, BigDecimal.valueOf(48.95).compareTo(offers.get(5).getPrice()));

		printOffers(offers);
	}

	@Test
	void testCaseTwo() {
		// Create the offers
		createOffer(1L, "2020-06-14T00.00.00Z", "2020-12-31T23.59.59Z", 1L, 0, BigDecimal.valueOf(35.50));
		createOffer(2L, "2020-06-14T15.00.00Z", "2020-06-14T18.30.00Z", 2L, 1, BigDecimal.valueOf(25.45));
		createOffer(3L, "2020-06-15T00.00.00Z", "2020-06-15T11.00.00Z", 3L, 1, BigDecimal.valueOf(30.50));
		createOffer(4L, "2020-06-15T16.00.00Z", "2020-12-31T23.59.59Z", 4L, 1, BigDecimal.valueOf(38.95));

		// Call the endpoint /offers/brand/{brandId}/partnumber/{partnumber} to get offers by part number
		ResponseEntity<List<OfferByPartNumber>> response = restTemplate.exchange(
				"/offers/brand/1/partnumber/0001002",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {

				});

		// Get the response
		List<OfferByPartNumber> offers = response.getBody();

		// Validate that the response is not empty
		assertNotNull(offers);
		assertFalse(offers.isEmpty());
		
		// Validate the values of the offers in the response
		assertEquals(6, offers.size());

		assertEquals("2020-06-14T00:00:00Z", offers.get(0).getStartDate());
		assertEquals("2020-06-14T14:59:59Z", offers.get(0).getEndDate());
		assertEquals(0, BigDecimal.valueOf(35.50).compareTo(offers.get(0).getPrice()));

		assertEquals("2020-06-14T15:00:00Z", offers.get(1).getStartDate());
		assertEquals("2020-06-14T18:29:59Z", offers.get(1).getEndDate());
		assertEquals(0, BigDecimal.valueOf(25.45).compareTo(offers.get(1).getPrice()));

		assertEquals("2020-06-14T18:30:00Z", offers.get(2).getStartDate());
		assertEquals("2020-06-14T23:59:59Z", offers.get(2).getEndDate());
		assertEquals(0, BigDecimal.valueOf(35.50).compareTo(offers.get(2).getPrice()));

		assertEquals("2020-06-15T00:00:00Z", offers.get(3).getStartDate());
		assertEquals("2020-06-15T10:59:59Z", offers.get(3).getEndDate());
		assertEquals(0, BigDecimal.valueOf(30.50).compareTo(offers.get(3).getPrice()));

		assertEquals("2020-06-15T11:00:00Z", offers.get(4).getStartDate());
		assertEquals("2020-06-15T15:59:59Z", offers.get(4).getEndDate());
		assertEquals(0, BigDecimal.valueOf(35.50).compareTo(offers.get(4).getPrice()));

		assertEquals("2020-06-15T16:00:00Z", offers.get(5).getStartDate());
		assertEquals("2020-12-31T23:59:59Z", offers.get(5).getEndDate());
		assertEquals(0, BigDecimal.valueOf(38.95).compareTo(offers.get(5).getPrice()));

		printOffers(offers);

	}

	private void printOffers(List<OfferByPartNumber> offers) {

		offers.forEach(
				offer -> log.info("startDate: {}, endDate: {}, price: {}",
						offer.getStartDate(), offer.getEndDate(), offer.getPrice()));
	}

	private void createOffer(Long offerId, String startDate, String endDate, Long priceListId,
			int priority, BigDecimal price) {

		Offer offer = new Offer();
		offer.setOfferId(offerId);
		offer.setBrandId(1);
		offer.setStartDate(startDate);
		offer.setEndDate(endDate);
		offer.setPriceListId(priceListId);
		offer.setProductPartnumber("0001002");
		offer.setPriority(priority);
		offer.setPrice(price);
		offer.setCurrencyIso("EUR");

		ResponseEntity<Void> response = restTemplate.postForEntity("/offers", offer, Void.class);
		// Assert that the offer was created successfully
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
}
