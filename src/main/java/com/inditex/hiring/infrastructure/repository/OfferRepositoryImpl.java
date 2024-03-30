package com.inditex.hiring.infrastructure.repository;

import com.inditex.hiring.domain.entity.Offer;
import com.inditex.hiring.domain.repository.OfferRepository;
import com.inditex.hiring.infrastructure.mapper.OfferJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OfferRepositoryImpl implements OfferRepository {

	private final OfferJpaRepository repository;
	private final OfferJpaMapper mapper;

	@Override
	public Optional<Offer> findById(Long id) {

		return Optional.empty();
	}

	@Override
	public List<Offer> findAll() {

		return mapper.map(repository.findAll());
	}

	@Override
	public boolean deleteById(Long id) {

		return false;
	}

	@Override
	public boolean deleteAll() {

		return false;
	}

	@Override
	public Offer create(Offer offer) {

		return null;
	}
}
