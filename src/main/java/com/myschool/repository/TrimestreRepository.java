package com.myschool.repository;

import com.myschool.domain.Trimestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrimestreRepository extends JpaRepository<Trimestre, Long>{

    @Query("SELECT  s FROM  Trimestre s WHERE s.name = ?1" )
    Trimestre findByName(String name);

    List<Trimestre> findByAnneeActiveTrue();

    List<Trimestre> findByAnneeIdOrderByRankAsc(Long anneeId);
}