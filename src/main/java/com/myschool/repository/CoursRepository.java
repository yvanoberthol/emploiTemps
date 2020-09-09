package com.myschool.repository;

import com.myschool.domain.Cours;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursRepository extends JpaRepository<Cours,Long> {
}
