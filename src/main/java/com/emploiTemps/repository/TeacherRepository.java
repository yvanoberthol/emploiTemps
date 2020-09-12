package com.emploiTemps.repository;

import com.emploiTemps.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    @Query("SELECT t FROM Teacher t "
            + "where t.lastName like ?1 or t.firstName like ?1")
    List<Teacher> findByMc(String mc);

    @Query("SELECT t FROM Teacher t "
            + "where t.lastName like ?1 or t.firstName like ?1")
    List<Teacher> findByPromoId(Long id);
}
