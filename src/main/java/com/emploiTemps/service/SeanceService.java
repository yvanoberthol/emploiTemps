package com.emploiTemps.service;


import com.emploiTemps.domain.Cours;
import com.emploiTemps.domain.Promo;
import com.emploiTemps.domain.Seance;
import com.emploiTemps.domain.Teacher;
import com.emploiTemps.domain.form.SeanceCours;
import com.emploiTemps.dto.PromoDto;
import com.emploiTemps.dto.SeanceDto;
import com.emploiTemps.repository.CoursRepository;
import com.emploiTemps.repository.PromoRepository;
import com.emploiTemps.repository.SeanceRepository;
import com.emploiTemps.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SeanceService {

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public void save(SeanceDto seanceDto){

        Cours cours = coursRepository.getOne(seanceDto.getIdCours());
        if (cours == null){
            return ;
        }

        // on vérifie la date
        Date jourSeance = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            jourSeance = sdf.parse(seanceDto.getJour());
        } catch (ParseException e) {
            System.out.println(String.format("jour [%s] invalide", seanceDto.getJour()));
        }

        Seance seanceUpdated = seanceRepository.findSeanceByCoursAndJour(cours, jourSeance);
        if (seanceUpdated != null){
            System.out.println("cours"+seanceDto.getJour()+seanceDto.getId());
            seanceUpdated.setEnabled(seanceDto.isEnabled());
            seanceRepository.save(seanceUpdated);
        }else{
            Seance seance = new Seance();
            seance.setJour(jourSeance);
            seance.setCours(cours);
            seance.setEnabled(seanceDto.isEnabled());
            seanceRepository.save(seance);
        }

    }

    public SeanceCours getSeanceTeachers(Long idTeacher,Date debut, Date fin){

        Teacher teacher = teacherRepository.getOne(idTeacher);

        if (teacher == null)
            return null;

        List<Seance> seanceList = seanceRepository
                .findSeancesByCours_MatiereTeacher_TeacherAndEnabledAndJourBetweenOrderByJour(teacher,true,debut,fin);

        if (seanceList == null)
            return null;

        SeanceCours seanceCours = new SeanceCours();

        List<SeanceDto> seanceDtos = new ArrayList<>();
        for (Seance seance: seanceList)
        {
            SeanceDto seanceDto = new SeanceDto().createDTO(seance);
            seanceDtos.add(seanceDto);
        }
        seanceCours.setSeanceDtos(seanceDtos);


        List<Promo> promos = promoRepository.findAll();

        List<PromoDto> promoDtos = new ArrayList<>();
        for (Promo promo: promos)
        {
            PromoDto promoDto = new PromoDto().createDTO(promo);
            promoDtos.add(promoDto);
        }
        seanceCours.setPromoDtos(promoDtos);

        return seanceCours;
    }
}
