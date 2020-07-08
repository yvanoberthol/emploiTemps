/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myschool.dto;

import com.myschool.domain.StudentNote;
import lombok.Data;

/**
 *
 * @author medilox
 */

@Data
public class StudentNoteDto {

    private Long id;
    private Long studentId;
    private Long promoId;
    private String matiere;
    private Long matiereId;
    private String groupe;
    private Long groupeId;
    private Long sequenceId;
    private String sequence;
    private Long trimestreId;
    private String trimestre;

    private Double note;
    private String mention;
    private Double weight;
    private Double coef;
    private Integer noteSur;

    public StudentNoteDto createDTO(StudentNote studentNote) {
        StudentNoteDto studentNoteDto = new StudentNoteDto();
        if(studentNote != null){
            studentNoteDto.setId(studentNote.getId());
            studentNoteDto.setNote(studentNote.getNote());

            if(studentNote.getNote() != null){
                studentNoteDto.setNote(studentNote.getNote());
            }

            if(studentNote.getInscription() != null){
                studentNoteDto.setStudentId(studentNote.getInscription().getStudent().getId());
                studentNoteDto.setPromoId(studentNote.getInscription().getPromo().getId());
            }

            if(studentNote.getSequence() != null){
                studentNoteDto.setSequenceId(studentNote.getSequence().getId());
                studentNoteDto.setSequence(studentNote.getSequence().getName());
                studentNoteDto.setWeight(studentNote.getSequence().getWeight());

                studentNoteDto.setTrimestreId(studentNote.getSequence().getTrimestre().getId());
                studentNoteDto.setTrimestre(studentNote.getSequence().getTrimestre().getName());
            }

            if(studentNote.getMatiere() != null){
                studentNoteDto.setMatiere(studentNote.getMatiere().getName());
                studentNoteDto.setMatiereId(studentNote.getMatiere().getId());

                studentNoteDto.setCoef((double) 1);
                if(studentNote.getMatiere().getCoef() != null)
                    studentNoteDto.setCoef(studentNote.getMatiere().getCoef());

                studentNoteDto.setNoteSur(20);
                if(studentNote.getMatiere().getNoteSur() != null){
                    studentNoteDto.setNoteSur(studentNote.getMatiere().getNoteSur());
                    //studentNoteDto.setCoef((studentNote.getMatiere().getNoteSur() / 20.0));
                }


                if(studentNote.getMatiere().getGroupe() != null){
                    studentNoteDto.setGroupe(studentNote.getMatiere().getGroupe().getName());
                    studentNoteDto.setGroupeId(studentNote.getMatiere().getGroupe().getId());
                }
            }

            return studentNoteDto;
        }
        return null;
    }

    public static String findMention(Double note) {
        /*
        if(note <= 9) return "Mediocre";
        if(note <= 8) return "Insuffisant";
        if(note <= 6) return "Faible";
        if(note <= 4) return "Très faible";
        if(note > 10) return"Passable";
        if(note > 11.9) return"Assez Bien";
        if(note > 13.9) return"Bien";
        if(note > 15.9) return "Très Bien";
        if(note > 17.9) return "Excellent";
        if(note > 19.9) return "Parfait";
        */

        if(note <= 9.99) return "Non Acquis";
        if(note > 17.9) return "Expert";
        if(note > 13.9) return "Acquis";
        if(note > 9.99) return "En Cours d'Acquisition";
        return null;
    }

}
