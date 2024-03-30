package com.inditex.hiring.infrastructure.mapper;

import com.inditex.hiring.domain.entity.Offer;
import com.inditex.hiring.infrastructure.entity.OfferJpa;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OfferJpaMapper {

	Offer map(OfferJpa offerJpa);

	List<Offer> map(Iterable<OfferJpa> offers);
}
