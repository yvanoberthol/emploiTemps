
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
    private String dateNaissanceFormatted;
    private String lieuNaissance;
    private String nationalite;
    private String matricule;
    private String email;
    private String classe;
    private double montantPaye;
    private double solde;
    private Boolean deletable;

    private List<LigneTrimestreDto> notes;
    private List<MouvementDto> mouvements;

    private int age;
    private String fatherName;
    private String motherName;
    private String fatherPhone;
    private String motherPhone;
    private String fatherProfession;
    private String motherProfession;
    private String otherInfos;

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
            studentDto.setDateNaissanceFormatted(student.getDateNaissanceFormatted());
            studentDto.setLieuNaissance(student.getLieuNaissance());
            studentDto.setNationalite(student.getNationalite());
            studentDto.setMatricule(student.getMatricule());

            studentDto.setFatherName(student.getFatherName());
            studentDto.setFatherPhone(student.getFatherPhone());
            studentDto.setFatherProfession(student.getFatherProfession());
            studentDto.setMotherName(student.getMotherName());
            studentDto.setMotherPhone(student.getMotherPhone());
            studentDto.setMotherProfession(student.getMotherProfession());
            studentDto.setOtherInfos(student.getOtherInfos());

            if(student.getInscriptions() != null){
                if(student.getInscriptions().isEmpty())
                    studentDto.setDeletable(true);
            }
        }
        return studentDto;
    }
}
