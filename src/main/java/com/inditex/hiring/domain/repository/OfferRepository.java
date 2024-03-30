package com.inditex.hiring.domain.repository;

import com.inditex.hiring.domain.entity.Offer;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {

	Optional<Offer> findById(Long id);

	List<Offer> findAll();

	void deleteById(final Long id);

	void deleteAll();

	Offer create(final Offer offer);
}
