package com.inditex.hiring.controller;

import com.inditex.hiring.application.*;
import com.inditex.hiring.application.mapper.OfferDtoMapper;
import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.domain.command.DeleteAllOffersCommand;
import com.inditex.hiring.domain.command.DeleteOfferByIdCommand;
import com.inditex.hiring.domain.query.GetAllOffersQuery;
import com.inditex.hiring.domain.query.GetOfferByIdQuery;
import com.inditex.hiring.domain.query.GetOffersPricePeriodsQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * You can change this controller but please do not change ends points signatures & payloads.
 */
@RestController
@RequiredArgsConstructor
public class OfferController {

	private final GetAllOffersUseCase getAllOffersUseCase;
	private final GetOfferByIdUseCase getOfferByIdUseCase;
	private final DeleteOfferByIdUseCase deleteOfferByIdUseCase;
	private final DeleteAllOffersUseCase deleteAllOffersUseCase;
	private final CreateOfferUseCase createOfferUseCase;
	private final GetOffersPricePeriodsUseCase getOffersPricePeriodsUseCase;
	private final OfferDtoMapper mapper;

	@PostMapping(path = { "/offers", "/offer" }, consumes = APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void createNewOffer(@RequestBody @Valid Offer offer) {

		this.createOfferUseCase.execute(mapper.map(offer));
	}

	@DeleteMapping(path = { "/offers", "/offer" })
	@ResponseStatus(HttpStatus.OK)
	public void deleteAllOffers() {

		this.deleteAllOffersUseCase.execute(DeleteAllOffersCommand.builder().build());

	}

	@DeleteMapping(path = { "/offers/{id}", "/offer/{id}" })
	@ResponseStatus(HttpStatus.OK)
	public void deleteOfferById(@PathVariable @Valid Long id) {

		this.deleteOfferByIdUseCase.execute(DeleteOfferByIdCommand.builder().id(id).build());
	}

	@GetMapping(path = { "/offers", "/offer" })
	@ResponseStatus(HttpStatus.OK)
	public List<Offer> getAllOffers() {

		return this.getAllOffersUseCase.execute(GetAllOffersQuery.builder().build());
	}

	@GetMapping(path = { "/offers/{offerId}", "/offer/{offerId}" })
	@ResponseStatus(HttpStatus.OK)
	public Offer getOfferById(@PathVariable @Valid Long offerId) {

		return this.getOfferByIdUseCase.execute(GetOfferByIdQuery.builder().id(offerId).build());
	}

	@GetMapping(path = { "/offers/brand/{brandId}/partnumber/{partnumber}",
			"brand/{brandId}/partnumber/{partnumber}/offer" })
	@ResponseStatus(HttpStatus.OK)
	public List<OfferByPartNumber> getOfferByPartNumber(@PathVariable Integer brandId,
			@PathVariable String partnumber) {

		return this.getOffersPricePeriodsUseCase.execute(
				GetOffersPricePeriodsQuery.builder().brandId(brandId).partNumber(partnumber).build());
	}
}
