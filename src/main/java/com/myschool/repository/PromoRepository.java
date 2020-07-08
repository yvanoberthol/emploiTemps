package com.myschool.repository;

import com.myschool.domain.Promo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PromoRepository extends JpaRepository<Promo, Long>{

    List<Promo> findByAnneeId(Long anneeId);

    List<Promo> findByAnneeActiveTrue();
}