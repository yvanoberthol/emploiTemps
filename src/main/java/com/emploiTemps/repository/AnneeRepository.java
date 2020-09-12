package com.emploiTemps.repository;

import com.emploiTemps.domain.Annee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AnneeRepository extends JpaRepository<Annee, Long>{

    @Query("select a from Annee a where (a.active = true)")
    Annee findActive();
}