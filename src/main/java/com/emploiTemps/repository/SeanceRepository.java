package com.emploiTemps.repository;

import com.emploiTemps.domain.Cours;
import com.emploiTemps.domain.MatiereTeacher;
import com.emploiTemps.domain.Seance;
import com.emploiTemps.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SeanceRepository extends JpaRepository<Seance, Long> {

    List<Seance> findAllByCoursMatiereTeacherAndJour(MatiereTeacher matiereTeacher, Date date);

    Seance findSeanceByCoursAndJour(Cours cours, Date jour);

    List<Seance> findSeancesByCours_MatiereTeacher_TeacherAndEnabledAndJourBetweenOrderByJour(Teacher teacher, boolean enabled,Date debut,Date fin);
}
