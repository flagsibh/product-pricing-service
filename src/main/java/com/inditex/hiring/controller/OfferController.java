package com.inditex.hiring.controller;

import com.inditex.hiring.application.GetAllOffersUseCase;
import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.domain.query.GetAllOffersQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * You can change this controller but please do not change ends points signatures & payloads.
 */
@RestController
@RequiredArgsConstructor
public class OfferController {

	private final GetAllOffersUseCase getAllOffersUseCase;

	@RequestMapping(value = "/offer", method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createNewOffer(@RequestBody @Valid Offer offer) {

		//TODO implement it!.

	}

	@RequestMapping(value = "/offer", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteAllOffers() {

		//TODO implement it!.

	}

	@RequestMapping(value = "/offer/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteOfferById(@RequestParam Long id) {

		//TODO implement it!.

	}

	@GetMapping(path = "/offer")
	@ResponseStatus(HttpStatus.OK)
	public List<Offer> getAllOffers() {

		return this.getAllOffersUseCase.execute(GetAllOffersQuery.builder().build());
	}

	@RequestMapping(value = "/offer/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Offer getOfferById(Long offerId) {

		//TODO implement it!.
		return new Offer();
	}

	@RequestMapping(value = "brand/{brandId}/partnumber/{partnumber}/offer", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<OfferByPartNumber> getOfferByPartNumber(Integer brandId, String partnumber) {

		//TODO implement it!.
		return new ArrayList<>();
	}
}
