package com.myschool.repository;

import com.myschool.domain.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SequenceRepository extends JpaRepository<Sequence, Long>{

    @Query("SELECT  s FROM  Sequence s WHERE s.name = ?1" )
    Sequence findByName(String name);

    List<Sequence> findByTrimestreId(Long trimestreId);

    List<Sequence> findByTrimestreIdOrderByRankAsc(Long trimestreId);
}