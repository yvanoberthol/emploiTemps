package com.emploiTemps.service;

import com.emploiTemps.domain.*;
import com.emploiTemps.domain.form.CallBackMessage;
import com.emploiTemps.domain.form.Lecon;
import com.emploiTemps.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoursService {

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CreneauHoraireRepository creneauHoraireRepository;

    @Autowired
    private MatiereTeacherRepository matiereTeacherRepository;


    public List<Cours> getCoursByTeacherClasse(Long idTeacher,Long idPromo){

        if (idTeacher == null || idPromo == null){
            return null;
        }

        Promo promo = promoRepository.findOne(idPromo);

        List<MatiereTeacher> matiereTeachers = matiereTeacherRepository
                .findMatiereTeacherByTeacherIdAndMatierePromo(idTeacher, promo);

        List<Cours> coursList = new ArrayList<>();

        for (MatiereTeacher matiereTeacher: matiereTeachers){
            List<Cours> cours = coursRepository.findAllByMatiereTeacher(matiereTeacher);
            coursList.addAll(cours);
        }

        return coursList;
    }

    public CallBackMessage save(Lecon lecon){

        CallBackMessage callBackMessage = new CallBackMessage();

        if (lecon.getMatiere() == null ||
                lecon.getCreneauHoraire() == null ||
                lecon.getTeacher() == null){

            callBackMessage.setKey("incorrectInfo");
            callBackMessage.setMessage("Vérifier les informations entrées dans le formulaire");
        }

        MatierTeacherId matierTeacherId = new MatierTeacherId();
        matierTeacherId.setMatiereId(lecon.getMatiere().getId());
        matierTeacherId.setTeacherId(lecon.getTeacher().getId());

        MatiereTeacher matiereTeacherByMatiere = matiereTeacherRepository
                .findMatiereTeacherByMatiereId(lecon.getMatiere().getId());

        Matiere matiere = matiereRepository.getOne(matierTeacherId.getMatiereId());
        Teacher teacher = teacherRepository.getOne(matierTeacherId.getTeacherId());


        MatiereTeacher matiereTeacherOne = matiereTeacherRepository.
                findMatiereTeacherByTeacherIdAndMatiereId(matierTeacherId.getTeacherId(),matierTeacherId.getMatiereId());
        if (matiereTeacherOne == null && matiereTeacherByMatiere == null){

            MatiereTeacher matiereTeacher = new MatiereTeacher();
            matiereTeacher.setMatiere(matiere);
            matiereTeacher.setTeacher(teacher);

            matiereTeacherOne = matiereTeacherRepository.save(matiereTeacher);
        }

        if (matiereTeacherByMatiere.getTeacher() != matiereTeacherOne.getTeacher()){
            callBackMessage.setKey("matiereDejaPris");
            callBackMessage.setMessage("Cette matière est déja prise par une autre professeur");
        }


        CreneauHoraire creneauHoraire = creneauHoraireRepository.getOne(lecon.getCreneauHoraire().getId());

        Cours coursExist = coursRepository.
                findCoursByCreneauHoraireAndJourAndMatiereTeacher_Matiere_Promo(creneauHoraire,lecon.getJour(),matiere.getPromo());
        if (coursExist != null){
            callBackMessage.setKey("coursOccupe");
            callBackMessage.setMessage("Ce créneau est déja occupé par un autre professeur");
        }

        Cours cours = new Cours();
        cours.setJour(lecon.getJour());
        cours.setCreneauHoraire(creneauHoraire);
        cours.setMatiereTeacher(matiereTeacherOne);
        coursRepository.save(cours);

        callBackMessage.setKey("success");
        callBackMessage.setMessage("Le cours a été enregistré");

        return callBackMessage;
    }

    public boolean delete(Long idCours){
        Cours cours = coursRepository.getOne(idCours);
        if (cours == null)
            return false;

        MatiereTeacher matiereTeacher =
                matiereTeacherRepository.getOne(cours.getMatiereTeacher().getId());

        if (cours.getSeances().size() > 0){
            seanceRepository.delete(cours.getSeances());
        }

        coursRepository.delete(cours);
        return true;
    }
}
