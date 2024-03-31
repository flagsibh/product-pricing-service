package com.inditex.hiring.application.mapper;

import com.inditex.hiring.controller.dto.Offer;
import com.inditex.hiring.controller.dto.OfferByPartNumber;
import com.inditex.hiring.domain.command.CreateOfferCommand;
import org.apache.commons.lang.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper
public interface OfferDtoMapper {

	String INPUT_DATETIME_PATTERN = "yyyy-MM-dd'T'HH.mm.ssX";

	String OUTPUT_DATETIME_PATTERN = "yyyy-MM-dd'T'HH.mm.ss'Z'";

	String FLATTENED_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	Offer map(com.inditex.hiring.domain.entity.Offer offer);

	List<Offer> map(List<com.inditex.hiring.domain.entity.Offer> offers);

	CreateOfferCommand map(Offer offer);

	@Mapping(target = "startDate", source = "startDate", qualifiedByName = "mapFlattened")
	@Mapping(target = "endDate", source = "endDate", qualifiedByName = "mapFlattened")
	OfferByPartNumber map(com.inditex.hiring.domain.vo.OfferPeriod offerPeriod);

	List<OfferByPartNumber> mapOfferPeriods(List<com.inditex.hiring.domain.vo.OfferPeriod> offerPeriods);

	default String map(OffsetDateTime offsetDateTime) {

		if (offsetDateTime == null) {
			return null;
		}

		LocalDateTime localDateTime = offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
		return localDateTime.format(DateTimeFormatter.ofPattern(OUTPUT_DATETIME_PATTERN));
	}

	@Named("mapFlattened")
	default String mapFlattened(OffsetDateTime offsetDateTime) {

		if (offsetDateTime == null) {
			return null;
		}
		LocalDateTime localDateTime = offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
		return localDateTime.format(DateTimeFormatter.ofPattern(FLATTENED_DATETIME_PATTERN));
	}

	default OffsetDateTime map(String offsetDateTime) {

		if (StringUtils.isBlank(offsetDateTime)) {
			return null;
		}

		return OffsetDateTime.parse(offsetDateTime, DateTimeFormatter.ofPattern(INPUT_DATETIME_PATTERN));
	}

}
