package com.myschool.repository;

import com.myschool.domain.Inscription;
import com.myschool.domain.InscriptionPK;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Long>{

    Inscription findByInscriptionPK(InscriptionPK inscriptionPK);

    @Query("SELECT inscription FROM  Inscription inscription "
            + "WHERE inscription.promo.annee.id = ?1 "
            + "AND inscription.createdDate between ?2 and ?3 ")
    Page<Inscription> findByPromoAnneeIdAndCreatedDateBetween(Long anneeId, LocalDateTime cdf, LocalDateTime cdt, Pageable pageable);

    @Query("SELECT inscription FROM  Inscription inscription "
            + "WHERE inscription.promo.annee.id = ?1 "
            + "AND ( inscription.student.matricule like ?2 OR inscription.student.firstName like ?2 OR inscription.student.lastName like ?2)")
    Page<Inscription> findByPromoAnneeIdAndStudent(Long anneeId, String s, Pageable pageable);

    Page<Inscription> findByPromoId(Long promoId, Pageable pageable);

    @Query("SELECT inscription FROM  Inscription inscription "
            + "WHERE inscription.promo.annee.id = ?2 "
            + "AND inscription.student.id = ?1 ")
    Inscription findByStudentIdAndAnneeId(Long studentId, Long anneeId);

    @Query("SELECT inscription FROM  Inscription inscription "
            + "WHERE inscription.promo.id = ?1 "
            + "AND ( inscription.student.firstName like ?2 OR inscription.student.lastName like ?2)")
    Page<Inscription> findByPromoId(Long promoId, String s, Pageable pageable);

    void deleteByInscriptionPK(InscriptionPK inscriptionPK);

    List<Inscription> findByStudentId(Long id);
}