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

		return repository.findById(id).map(mapper::map);
	}

	@Override
	public List<Offer> findAll() {

		return mapper.map(repository.findAll());
	}

	@Override
	public void deleteById(Long id) {

		repository.deleteById(id);
	}

	@Override
	public void deleteAll() {

		repository.deleteAll();
	}

	@Override
	public Offer create(Offer offer) {

		return mapper.map(repository.save(mapper.map(offer)));
	}
}
