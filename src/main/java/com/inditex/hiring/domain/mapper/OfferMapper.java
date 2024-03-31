package com.inditex.hiring.domain.mapper;

import com.inditex.hiring.domain.command.CreateOfferCommand;
import com.inditex.hiring.domain.entity.Offer;
import com.inditex.hiring.domain.vo.OfferPeriod;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OfferMapper {

	Offer map(CreateOfferCommand command);

	default OfferPeriod map(Offer offer) {

		return OfferPeriod.of(offer.getStartDate(), offer.getEndDate(), offer.getPrice(), offer.getCurrencyIso());
	}

	List<OfferPeriod> map(List<Offer> offers);
}
