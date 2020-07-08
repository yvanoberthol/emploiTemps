package com.myschool.repository;

import com.myschool.domain.Reglement;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReglementRepository extends JpaRepository<Reglement, Long> {

    /*@Query("select reglement from Reglement reglement where reglement.inscription.student.login = ? or reglement.inscription.student.email = ?")
    List<Reglement> findByUserIsCurrentUser(String currentStudentLogin);*/

    /*@Query("select reglement from Reglement reglement "
            + "where reglement.inscription.student.login = ?1 or reglement.inscription.student.email = ?1 "
            + "and reglement.paymentValidated = true")
    Page<Reglement> findByUserIsCurrentUser(String currentStudentLogin, Pageable pageable);*/

    List<Reglement> findAllByPaymentValidatedIsFalseAndCreatedDateBefore(LocalDateTime date);

    @Query("select reglement from Reglement reglement "
            + "where reglement.paymentValidated is null or reglement.paymentValidated = false")
    List<Reglement> findAllByPaymentNotValidated();

    @Query("SELECT reglement FROM  Reglement reglement "
            + "WHERE reglement.createdDate between ?1 and ?2 ")
    Page<Reglement> findAll(LocalDateTime cdf, LocalDateTime cdt, Pageable pageable);

    @Query("SELECT reglement FROM  Reglement reglement "
            + "WHERE reglement.inscription.promo.annee.id = ?1 "
            + "AND reglement.createdDate between ?2 and ?3 ")
    Page<Reglement> findByAnnee(Long anneeId, LocalDateTime cdf, LocalDateTime cdt, Pageable pageable);

    @Query("SELECT reglement FROM  Reglement reglement "
            + "WHERE reglement.inscription.promo.annee.id = ?1 "
            + "AND reglement.createdDate between ?2 and ?3 "
            + "AND reglement.inscription.promo.id = ?4 ")
    Page<Reglement> findByAnneeAndPromoId(Long anneeId, LocalDateTime cdf, LocalDateTime cdt, Long promoId, Pageable pageable);
}