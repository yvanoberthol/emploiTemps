package com.emploiTemps.repository;

import com.emploiTemps.domain.Cours;
import com.emploiTemps.domain.CreneauHoraire;
import com.emploiTemps.domain.MatiereTeacher;
import com.emploiTemps.domain.Promo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoursRepository extends JpaRepository<Cours,Long> {

    List<Cours> findAllByMatiereTeacher(MatiereTeacher matiereTeacher);

    Cours findCoursByCreneauHoraireAndJourAndMatiereTeacher_Matiere_Promo(CreneauHoraire creneauHoraire, int jour, Promo promo);
}