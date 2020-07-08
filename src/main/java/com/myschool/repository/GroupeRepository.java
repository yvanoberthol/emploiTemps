package com.myschool.repository;

import com.myschool.domain.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupeRepository extends JpaRepository<Groupe, Long>{

    List<Groupe> findByPromoId(Long promoId);
}