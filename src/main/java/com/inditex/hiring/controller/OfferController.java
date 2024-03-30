package com.inditex.hiring.controller;

import com.inditex.hiring.application.*;
import com.inditex.hiring.application.mapper.OfferDtoMapper;
import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.domain.command.DeleteAllOffersCommand;
import com.inditex.hiring.domain.command.DeleteOfferByIdCommand;
import com.inditex.hiring.domain.query.GetAllOffersQuery;
import com.inditex.hiring.domain.query.GetOfferByIdQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
	private final OfferDtoMapper mapper;

	@PostMapping(path = "/offer", consumes = APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void createNewOffer(@RequestBody @Valid Offer offer) {

		this.createOfferUseCase.execute(mapper.map(offer));
	}

	@DeleteMapping(path = "/offer")
	@ResponseStatus(HttpStatus.OK)
	public void deleteAllOffers() {

		this.deleteAllOffersUseCase.execute(DeleteAllOffersCommand.builder().build());

	}

	@DeleteMapping(path = "/offer/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteOfferById(@PathVariable @Valid Long id) {

		this.deleteOfferByIdUseCase.execute(DeleteOfferByIdCommand.builder().id(id).build());
	}

	@GetMapping(path = "/offer")
	@ResponseStatus(HttpStatus.OK)
	public List<Offer> getAllOffers() {

		return this.getAllOffersUseCase.execute(GetAllOffersQuery.builder().build());
	}

	@GetMapping(path = "/offer/{offerId}")
	@ResponseStatus(HttpStatus.OK)
	public Offer getOfferById(@PathVariable @Valid Long offerId) {

		return this.getOfferByIdUseCase.execute(GetOfferByIdQuery.builder().id(offerId).build());
	}

	@RequestMapping(value = "brand/{brandId}/partnumber/{partnumber}/offer", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<OfferByPartNumber> getOfferByPartNumber(Integer brandId, String partnumber) {

		//TODO implement it!.
		return new ArrayList<>();
	}
}
