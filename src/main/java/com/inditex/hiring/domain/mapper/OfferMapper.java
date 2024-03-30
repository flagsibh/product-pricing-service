package com.inditex.hiring.domain.mapper;

import com.inditex.hiring.domain.command.CreateOfferCommand;
import com.inditex.hiring.domain.entity.Offer;
import org.mapstruct.Mapper;

@Mapper
public interface OfferMapper {

	Offer map(CreateOfferCommand command);
}
