package com.inditex.hiring.domain.query.handler;

import com.inditex.hiring.domain.entity.Offer;
import com.inditex.hiring.domain.mapper.OfferMapper;
import com.inditex.hiring.domain.query.GetOffersPricePeriodsQuery;
import com.inditex.hiring.domain.repository.OfferRepository;
import com.inditex.hiring.domain.shared.exception.model.Error;
import com.inditex.hiring.domain.shared.query.QueryFailure;
import com.inditex.hiring.domain.shared.query.handler.QueryHandler;
import com.inditex.hiring.domain.vo.OfferPeriod;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class GetOffersPricePeriodsQueryHandler implements QueryHandler<GetOffersPricePeriodsQuery, List<OfferPeriod>> {

	private final OfferRepository repository;
	private final OfferMapper mapper;

	@Override
	public Either<QueryFailure, List<OfferPeriod>> handle(GetOffersPricePeriodsQuery query) {

		return Try.of(() -> repository.findByBrandIdAndProductPartnumber(query.getBrandId(), query.getPartNumber()))
		          .fold(throwable -> {
			          log.error("Error getting offers price periods", throwable);
			          return Either.left(QueryFailure.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
			                                         .error(Error.builder()
			                                                     .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
			                                                     .message(throwable.getMessage())
			                                                     .build())
			                                         .build());
		          }, offers -> Either.right(flattenIntervalsWithPriority(offers)));
	}

	/**
	 * This method is responsible for processing the list of offers. It first sorts the offers by start date and
	 * priority. Then it iterates over the sorted offers and checks for overlaps with existing offers. If an overlap is
	 * found, the existing offer is split into two, or the new offer is added to the map, depending on the priority of
	 * the offers and the end date of the new offer.
	 *
	 * @param offers The list of offers to process
	 * @return A list of OfferPeriod with the flattened intervals
	 */
	private List<OfferPeriod> flattenIntervalsWithPriority(List<Offer> offers) {

		// Sort the offers by start date and priority
		offers.sort(
				Comparator.comparing(Offer::getStartDate).thenComparing(Offer::getPriority, Comparator.reverseOrder()));

		// Flatten the intervals
		// The primary reason for choosing a TreeMap over other data structures is its ability to maintain keys in a sorted order. 
		// This is particularly useful in this context as the offers need to be processed based on their start dates. 
		TreeMap<OffsetDateTime, Offer> offerMap = new TreeMap<>();
		offers.forEach(offer -> {
			// If there's an existing offer that overlaps with the current offer
			// The headMap method of TreeMap is used to get a view of the portion of the map whose keys are less than 
			// the current offer's start date. This is used to check if there's an existing offer that overlaps with the current offer.
			if (!offerMap.headMap(offer.getStartDate()).isEmpty() &&
			    offerMap.get(offerMap.headMap(offer.getStartDate()).lastKey())
			            .getEndDate()
			            .isAfter(offer.getStartDate())) {
				Offer existingOffer = offerMap.get(offerMap.headMap(offer.getStartDate()).lastKey());

				// If the existing offer has lower priority or ends before the new offer, split the existing offer
				if (existingOffer.getPriority() < offer.getPriority() ||
				    offer.getEndDate().isBefore(existingOffer.getEndDate())) {
					splitExistingOffer(offer, existingOffer, offerMap);
				}
			}

			// If the current offer doesn't overlap with an existing offer or has higher priority, add it to the map
			addOfferToMap(offer, offerMap);
		});

		// Convert the map to a list of OfferPeriod
		return offerMap.values().stream().map(mapper::map).toList();
	}

	private void splitExistingOffer(Offer offer, Offer existingOffer, TreeMap<OffsetDateTime, Offer> offerMap) {

		// Create a new offer that ends one second before the new offer starts
		Offer newOffer = existingOffer.toBuilder().endDate(offer.getStartDate().minusSeconds(1)).build();
		offerMap.put(newOffer.getStartDate(), newOffer);

		// If the new offer ends before the existing one, update the existing offer's start date
		if (offer.getEndDate().isBefore(existingOffer.getEndDate())) {
			existingOffer.setStartDate(offer.getEndDate());
			offerMap.put(existingOffer.getStartDate(), existingOffer);
		}
	}

	private void addOfferToMap(Offer offer, TreeMap<OffsetDateTime, Offer> offerMap) {

		// If there's no existing offer for the start date or the new offer has higher priority, add it to the map
		if (!offerMap.containsKey(offer.getStartDate()) ||
		    offer.getPriority() > offerMap.get(offer.getStartDate()).getPriority()) {
			// If there's an existing offer after the new offer, set the end date of the new offer to one second before
			if (offerMap.get(offer.getEndDate()) != null)
				offer.setEndDate(offer.getEndDate().minusSeconds(1));
			offerMap.put(offer.getStartDate(), offer);
		}
	}
}