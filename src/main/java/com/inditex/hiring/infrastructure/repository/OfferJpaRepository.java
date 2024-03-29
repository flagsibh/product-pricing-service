package com.inditex.hiring.infrastructure.repository;

import com.inditex.hiring.infrastructure.entity.OfferJpa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferJpaRepository extends CrudRepository<OfferJpa, Long> {

}
