package com.myschool.dto;

import lombok.Data;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class InscriptionFormDto extends InscriptionDto{

    /*private String firstName;
    private String lastName;
    private String sexe;
    private String dateNaissance;
    private String lieuNaissance;
    private String nationalite;
    private String cni;
    private String matricule;
    private String phone1;
    private String phone2;
    private String email;
    private String fatherName;
    private String motherName;
    private String fatherPhone;
    private String motherPhone;
    private String otherInformations;*/

    private StudentDto student;

}
