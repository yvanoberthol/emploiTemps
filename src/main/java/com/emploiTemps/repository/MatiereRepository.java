package com.emploiTemps.repository;

import com.emploiTemps.domain.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatiereRepository extends JpaRepository<Matiere, Long>{

    List<Matiere> findByPromoId(Long promoId);
}