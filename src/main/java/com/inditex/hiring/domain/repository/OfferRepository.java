package com.inditex.hiring.domain.repository;

import com.inditex.hiring.domain.entity.Offer;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {

    Optional<Offer> findById(Long id);

    List<Offer> findAll();

    boolean deleteById(final Long id);

    boolean deleteAll();

    Offer create(final Offer offer);
}
