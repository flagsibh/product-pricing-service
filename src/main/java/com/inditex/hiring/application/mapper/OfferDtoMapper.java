package com.inditex.hiring.application.mapper;

import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.domain.command.CreateOfferCommand;
import org.apache.commons.lang.StringUtils;
import org.mapstruct.Mapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper
public interface OfferDtoMapper {

	String INPUT_DATETIME_PATTERN = "yyyy-MM-dd'T'HH.mm.ssX";

	String OUTPUT_DATETIME_PATTERN = "yyyy-MM-dd'T'HH.mm.ss'Z'";

	Offer map(com.inditex.hiring.domain.entity.Offer offer);

	List<Offer> map(List<com.inditex.hiring.domain.entity.Offer> offers);

	CreateOfferCommand map(Offer offer);

	default String map(OffsetDateTime offsetDateTime) {

		if (offsetDateTime == null) {
			return null;
		}

		return offsetDateTime.withOffsetSameInstant(ZoneOffset.UTC)
		                     .format(DateTimeFormatter.ofPattern(OUTPUT_DATETIME_PATTERN));
	}

	default OffsetDateTime map(String offsetDateTime) {

		if (StringUtils.isBlank(offsetDateTime)) {
			return null;
		}

		return OffsetDateTime.parse(offsetDateTime, DateTimeFormatter.ofPattern(INPUT_DATETIME_PATTERN));
	}

}
