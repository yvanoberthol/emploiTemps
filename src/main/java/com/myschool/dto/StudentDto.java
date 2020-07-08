
package com.myschool.dto;

import com.myschool.domain.Inscription;
import com.myschool.domain.Student;
import lombok.Data;

import java.util.List;


/**
 * Created by medilox on 3/10/17.
 */
@Data
public class StudentDto{
    private Long id;
    private String createdDate;
    private String firstName;
    private String lastName;
    private String name;
    private String sexe;
    private String statut;
    private String dateNaissance;
    private String lieuNaissance;
    private String nationalite;
    private String matricule;
    private String email;
    private String classe;
    private double montantPaye;
    private double solde;
    private Boolean deletable;
    //private Map<String, List<LigneGroupeDto>> notes;
    private List<LigneTrimestreDto> notes;
    private List<MouvementDto> mouvements;

    public StudentDto createDTO(Student student) {
        StudentDto studentDto = new StudentDto();

        if(student != null){
            studentDto.setId(student.getId());
            studentDto.setFirstName(student.getFirstName());
            studentDto.setLastName(student.getLastName());
            studentDto.setName(student.getName());
            studentDto.setCreatedDate(student.getCreatedDate());

            //-------------------------------------------------
            studentDto.setSexe(String.valueOf(student.getSexe()));
            studentDto.setStatut(String.valueOf(student.getStatut()));
            studentDto.setDateNaissance(student.getDateNaissance());
            studentDto.setLieuNaissance(student.getLieuNaissance());
            studentDto.setNationalite(student.getNationalite());
            studentDto.setMatricule(student.getMatricule());

            if(student.getInscriptions() != null){
                if(student.getInscriptions().isEmpty())
                    studentDto.setDeletable(true);
            }
        }
        return studentDto;
    }
}
