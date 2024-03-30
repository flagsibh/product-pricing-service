package com.inditex.hiring.application.mapper;

import com.inditex.hiring.controller.dto.Offer;
import org.mapstruct.Mapper;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper
public interface OfferMapper {

	Offer map(com.inditex.hiring.domain.entity.Offer offer);

	List<Offer> map(List<com.inditex.hiring.domain.entity.Offer> offers);

	default String map(OffsetDateTime offsetDateTime) {

		if (offsetDateTime == null) {
			return null;
		}

		return offsetDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH.mm.ss'Z'"));
	}
}
