package com.inditex.hiring.controller;

import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * You can change this controller but please do not change ends points signatures & payloads.
 */
@RestController
public class OfferController {

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

  @RequestMapping(value = "/offer", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public List<Offer> getAllOffers() {

    //TODO implement it!.
    return new ArrayList<>();

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
