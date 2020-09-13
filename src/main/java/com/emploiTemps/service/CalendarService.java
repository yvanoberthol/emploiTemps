package com.emploiTemps.service;


import com.emploiTemps.domain.*;
import com.emploiTemps.domain.enumerations.Semaine;
import com.emploiTemps.domain.form.Calendar;
import com.emploiTemps.domain.form.CoursTeacherJour;
import com.emploiTemps.domain.form.EmploiTempsTeacherJour;
import com.emploiTemps.dto.*;
import com.emploiTemps.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CalendarService {

    @Autowired
    private CoursService coursService;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private MatiereTeacherRepository matiereTeacherRepository;

    @Autowired
    private CreneauHoraireRepository creneauHoraireRepository;

    @Autowired
    private TeacherRepository teacherRepository;


    public EmploiTempsTeacherJour generateEmploiTemps(Teacher teacher, Long idPromo, Date jour){

        Promo promo = promoRepository.getOne(idPromo);
        List<Cours> coursList = coursService.getCoursByTeacherClasse(teacher.getId(),idPromo);

        if (coursList == null || promo == null){
            return null;
        }

        List<CoursDto> coursDtos = new ArrayList<>();
        for (Cours coursLigne : coursList){
            if (coursLigne != null){
                CoursDto coursDto = new CoursDto().createDTO(coursLigne);
                coursDtos.add(coursDto);
            }

        }

        EmploiTempsTeacherJour emploiTempsTeacherJour = new EmploiTempsTeacherJour();

        PromoDto promoDto = new PromoDto().createDTO(promo);

        TeacherDto teacherDto = new TeacherDto().createDTO(teacher);

        emploiTempsTeacherJour.setPromo(promoDto);
        emploiTempsTeacherJour.setTeacher(teacherDto);

        for (CoursDto cours: coursDtos){

            emploiTempsTeacherJour.setJour(cours.getJour());
            CoursTeacherJour[] coursTeacherJours = new CoursTeacherJour[coursDtos.size()];

            for (int ci = 0; ci < coursDtos.size(); ci++) {

                // ligne i agenda
                coursTeacherJours[ci] = new CoursTeacherJour();

                // créneau horaire
                CoursDto coursLigne = coursDtos.get(ci);
                coursTeacherJours[ci].setCours(coursLigne);
            }
            emploiTempsTeacherJour.setCoursTeacherJours(coursTeacherJours);
        }
        return emploiTempsTeacherJour;
    }


    public Calendar generateCalendar(Long idTeacher, Long idPromo, Date jour){

        Calendar calendar = new Calendar();

        Promo promo = promoRepository.getOne(idPromo);
        if (promo != null){
            PromoDto promoDto = new PromoDto().createDTO(promo);
            calendar.setPromoDto(promoDto);
        }

        List<Teacher> teacherList = teacherRepository.findAll();
        List<TeacherDto> teacherDtos = new ArrayList<>();
        if (teacherList != null){
            for (Teacher teacher : teacherList){
                TeacherDto teacherDto = new TeacherDto().createDTO(teacher);
                teacherDtos.add(teacherDto);
            }
            calendar.setTeacherDtos(teacherDtos);
        }

        List<CreneauHoraire> creneauHoraires = creneauHoraireRepository.findAll();
        List<CreneauHoraireDto> creneauHoraireDtos = new ArrayList<>();
        if (creneauHoraires != null){
            for (CreneauHoraire creneauHoraire : creneauHoraires){
                CreneauHoraireDto creneauHoraireDto = new CreneauHoraireDto().createDTO(creneauHoraire);
                creneauHoraireDtos.add(creneauHoraireDto);
            }
            calendar.setCreneauHoraires(creneauHoraireDtos);
        }

        Map<Integer,String> dayWeeks = new Hashtable<>();
        int i = 1;
        for (Semaine day: Semaine.values()){
            dayWeeks.put(i,day.name());
            i++;
        }

        List<EmploiTempsTeacherJour> emploiTempsTeacherJourList = new ArrayList<>();
        if (idTeacher == 0){
            for (Teacher teacher: teacherList){
                EmploiTempsTeacherJour emploiTempsTeacherJour = generateEmploiTemps(teacher,idPromo,jour);
                if (emploiTempsTeacherJour != null)
                    emploiTempsTeacherJourList.add(emploiTempsTeacherJour);
            }
        }else{
            Teacher teacher = teacherRepository.getOne(idTeacher);
            EmploiTempsTeacherJour emploiTempsTeacherJour = generateEmploiTemps(teacher,idPromo,jour);
            emploiTempsTeacherJourList.add(emploiTempsTeacherJour);
        }

        calendar.setEmploiTempsTeacherJours(emploiTempsTeacherJourList);

        calendar.setDaysWeek(dayWeeks);
        return calendar;
    }
}
