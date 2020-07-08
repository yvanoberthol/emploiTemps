package com.myschool.repository;

import com.myschool.domain.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatiereRepository extends JpaRepository<Matiere, Long>{
    List<Matiere> findByGroupePromoId(Long promoId);

    List<Matiere> findByPromoId(Long promoId);
}