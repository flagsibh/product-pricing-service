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
import java.util.*;

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
		          }, offers -> Either.right(flattenOffers(offers)));
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
	private List<OfferPeriod> flattenOffers(List<Offer> offers) {
		// there are five cases to consider:
		// 1. Left overlap: the new offer starts before the existing offer and ends within the existing offer
		// 2. Right overlap: the new offer starts within the existing offer's start and end dates and ends after the existing offer
		// 3. Contained overlap: the new offer starts within the existing offer and ends within the existing offer
		// 4. Containing overlap: the new offer starts before the existing offer and ends after the existing offer 
		// 5. No overlap: the new offer starts after the existing offer
		// All the cases must consider the priority of the offers

		// Sort the offers by start date and priority
		offers.sort(Comparator.comparing(Offer::getStartDate)
		                      .thenComparing(Offer::getPriority, Comparator.reverseOrder()));

		// Flattened intervals
		// The primary reason for choosing a TreeMap over other data structures is its ability to maintain keys in a sorted order.
		// This is particularly useful in this context as the offers need to be processed based on their start dates.
		TreeMap<OffsetDateTime, Offer> offerMap = new TreeMap<>();

		for (Offer offer : offers) {
			if (!offerMap.isEmpty()) {
				// The subMap method of TreeMap is used to get a view of the portion of the map whose keys are within the range of the current offer's start and end dates.
				Map<OffsetDateTime, Offer> subMap =
						offerMap.subMap(offerMap.firstKey(), true, offer.getEndDate(), true);
				for (Map.Entry<OffsetDateTime, Offer> entry : new ArrayList<>(subMap.entrySet())) {
					Offer existingOffer = entry.getValue();

					if (offer.getPriority() >= existingOffer.getPriority()) {
						if (offer.getStartDate().isBefore(existingOffer.getStartDate()) &&
						    offer.getEndDate().isBefore(existingOffer.getEndDate()) &&
						    offer.getEndDate().isAfter(existingOffer.getStartDate())) {
							// Left overlap
							Offer newOffer =
									existingOffer.toBuilder().startDate(offer.getEndDate()).build();
							offerMap.put(newOffer.getStartDate(), newOffer);
						} else if (offer.getStartDate().isAfter(existingOffer.getStartDate()) &&
						           offer.getStartDate().isBefore(existingOffer.getEndDate()) &&
						           offer.getEndDate().isAfter(existingOffer.getEndDate())) {
							// Right overlap
							// One approach to handle overlaps is to split the existing offer into two
							// he first period ends when the new offer starts, and the second period starts when the new offer ends.
							Offer newOffer =
									existingOffer.toBuilder().endDate(offer.getStartDate().minusSeconds(1)).build();
							offerMap.put(newOffer.getStartDate(), newOffer);

							// However, we can create three periods instead of two
							// Create a new period for the duration of the new offer
							Offer overlapOffer =
									offer.toBuilder().endDate(existingOffer.getEndDate()).build();
							offerMap.put(overlapOffer.getStartDate(), overlapOffer);

							// Create another period that starts when the new offer ends
							Offer postOverlapOffer =
									offer.toBuilder().startDate(existingOffer.getEndDate().plusSeconds(1)).build();
							offerMap.put(postOverlapOffer.getStartDate(), postOverlapOffer);
						}
						// We need to include the case where dates are equal
						else if ((offer.getStartDate().isAfter(existingOffer.getStartDate()) ||
						          offer.getStartDate().isEqual(existingOffer.getStartDate())) &&
						         (offer.getEndDate().isBefore(existingOffer.getEndDate()) ||
						          offer.getEndDate().isEqual(existingOffer.getEndDate()))) {
							// Contained overlap
							Offer newOffer1 =
									existingOffer.toBuilder().endDate(offer.getStartDate().minusSeconds(1)).build();
							offerMap.put(newOffer1.getStartDate(), newOffer1);
							Offer newOffer2 =
									existingOffer.toBuilder().startDate(offer.getEndDate()).build();
							if (newOffer2.getStartDate().isBefore(newOffer2.getEndDate()))
								offerMap.put(newOffer2.getStartDate(), newOffer2);
						} else if (offer.getStartDate().isBefore(existingOffer.getStartDate()) &&
						           offer.getEndDate().isAfter(existingOffer.getEndDate())) {
							// Containing overlap
							offerMap.remove(existingOffer.getStartDate());
						}
					} else {
						if (offer.getStartDate().isBefore(existingOffer.getStartDate()) &&
						    offer.getEndDate().isAfter(existingOffer.getEndDate())) {
							// Containing overlap with lower priority
							// Add the left edge to the map
							Offer leftEdge =
									offer.toBuilder().endDate(existingOffer.getStartDate().minusSeconds(1)).build();
							offerMap.put(leftEdge.getStartDate(), leftEdge);

							// Add the right edge to the map
							Offer rightEdge =
									offer.toBuilder().startDate(existingOffer.getEndDate()).build();
							offerMap.put(rightEdge.getStartDate(), rightEdge);

							// Check for overlaps again after adding the edges
							subMap = offerMap.subMap(offerMap.firstKey(), true, offer.getEndDate(), true);
							for (Map.Entry<OffsetDateTime, Offer> edgeEntry : new ArrayList<>(subMap.entrySet())) {
								Offer edgeOffer = edgeEntry.getValue();

								// Handle overlaps based on the priority of the offers
								if (offer.getPriority() > edgeOffer.getPriority()) {
									offerMap.remove(edgeOffer.getStartDate());
								}
							}
						} else if (offer.getStartDate().isAfter(existingOffer.getStartDate()) &&
						           offer.getEndDate().isBefore(existingOffer.getEndDate())) {
							// The current offer is entirely contained within the existing offer with lower priority
							// Split the existing offer into two
							Offer leftEdge =
									existingOffer.toBuilder().endDate(offer.getStartDate().minusSeconds(1)).build();
							offerMap.put(leftEdge.getStartDate(), leftEdge);

							Offer rightEdge =
									existingOffer.toBuilder().startDate(offer.getEndDate()).build();
							offerMap.put(rightEdge.getStartDate(), rightEdge);

							// Add the current offer with the price and priority of the existing offer
							Offer containedOffer = offer.toBuilder()
							                            .price(existingOffer.getPrice())
							                            .priority(existingOffer.getPriority())
							                            .endDate(offer.getEndDate().minusSeconds(1))
							                            .build();
							offerMap.put(containedOffer.getStartDate(), containedOffer);
						}
					}
				}
			}

			// Check if there is an existing offer with the same start date and if the current offer has a higher priority
			if (!offerMap.containsKey(offer.getStartDate()) ||
			    offer.getPriority() > offerMap.get(offer.getStartDate()).getPriority()) {
				// If there's an existing offer after the new offer, set the end date of the new offer to one second before
				if (offerMap.get(offer.getEndDate()) != null)
					offer.setEndDate(offer.getEndDate().minusSeconds(1));
				offerMap.put(offer.getStartDate(), offer);
			}
		}

		return offerMap.values().stream().map(mapper::map).toList();
	}
}