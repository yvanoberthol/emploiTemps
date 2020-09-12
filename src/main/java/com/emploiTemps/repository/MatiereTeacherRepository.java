package com.emploiTemps.repository;

import com.emploiTemps.domain.MatiereTeacher;
import com.emploiTemps.domain.Promo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatiereTeacherRepository extends JpaRepository<MatiereTeacher,Long> {

    MatiereTeacher findMatiereTeacherByTeacherIdAndMatiereId(Long idTeacher, Long idMatiere);
    List<MatiereTeacher> findMatiereTeacherByTeacherIdAndMatierePromo(Long idTeacher, Promo promo);
    List<MatiereTeacher> findAllByMatierePromo(Promo promo);
}
