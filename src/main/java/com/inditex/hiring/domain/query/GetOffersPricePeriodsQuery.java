package com.inditex.hiring.domain.query;

import com.inditex.hiring.domain.shared.query.Query;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetOffersPricePeriodsQuery implements Query {

	Integer brandId;
	String partNumber;
}
