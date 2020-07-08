package com.myschool.repository;

import com.myschool.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by medilox on 1/23/17.
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s INNER JOIN s.inscriptions i "
            + "where i.promo.annee.id = ?1 "
            + "AND (s.firstName like ?2 OR s.lastName like ?2)")
    Page<Student> findByAnnee(Long anneeId, String name, Pageable pageable);

    @Query("SELECT s FROM Student s INNER JOIN s.inscriptions i "
            + "WHERE i.promo.id = ?1 "
            + "AND (s.firstName like ?2 OR s.lastName like ?2)")
    Page<Student> findByPromoId(Long promoId, String name, Pageable pageable);

    @Query("SELECT s FROM Student s INNER JOIN s.inscriptions i "
            + "WHERE i.promo.annee.id = ?1 "
            + "AND i.promo.id = ?2 "
            + "AND (s.firstName like ?3 OR s.lastName like ?3)")
    Page<Student> findByAnneeAndPromoId(Long anneeId, Long promoId, String name, Pageable pageable);

    @Query("SELECT s FROM Student s "
            + "where s.lastName like ?2 or s.firstName like ?2")
    List<Student> findByMc(String mc);

    @Query("SELECT s FROM Student s INNER JOIN s.inscriptions i "
            + "WHERE i.promo.id = ?1 "
            + "AND (s.firstName like ?2 OR s.lastName like ?2)")
    List<Student> findByPromoId(Long promoId, String s);

    @Query("SELECT s FROM Student s INNER JOIN s.inscriptions i "
            + "WHERE i.promo.id = ?1 ")
    List<Student> findByPromoId(Long promoId);
}

