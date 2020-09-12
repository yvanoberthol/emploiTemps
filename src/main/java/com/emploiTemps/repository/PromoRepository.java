package com.emploiTemps.repository;

import com.emploiTemps.domain.Promo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromoRepository extends JpaRepository<Promo, Long>{

    List<Promo> findByAnneeId(Long anneeId);

    Promo findPromoByClasse(String classeName);

    List<Promo> findByAnneeActiveTrue();
}