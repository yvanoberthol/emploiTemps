package com.myschool.dto;

import com.myschool.domain.Inscription;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class InscriptionDto {

    private String createdDate;
    private Long promoId;
    private String annee;
    private String classe;
    private double solde;
    private Long studentId;
    private StudentDto student;
    private Long staffId;
    private String staff;
    private double montantScolarite;


    public InscriptionDto createDTO(Inscription inscription) {
        InscriptionDto inscriptionDto = new InscriptionDto();
        if(inscription != null){
            inscriptionDto.setCreatedDate(inscription.getCreatedDate());
            inscriptionDto.setMontantScolarite(inscription.getMontantScolarite());
            inscriptionDto.setSolde(inscription.getSolde());

            if(inscription.getPromo() != null){
                inscriptionDto.setPromoId(inscription.getPromo().getId());
                //inscriptionDto.setMontantScolarite(inscription.getPromo().getMontantScolarite());

                if(inscription.getPromo().getAnnee() != null){
                    inscriptionDto.setAnnee(inscription.getPromo().getAnnee().toString());
                }

                if(inscription.getPromo().getClasse() != null){
                    inscriptionDto.setClasse(inscription.getPromo().getClasse());
                }
            }

            if(inscription.getStudent() != null){
                inscriptionDto.setStudentId(inscription.getStudent().getId());
                inscriptionDto.setStudent(new StudentDto().createDTO(inscription.getStudent()));
            }

            if(inscription.getStaff() != null){
                inscriptionDto.setStaffId(inscription.getStaff().getId());
                inscriptionDto.setStaff(inscription.getStaff().getName());
            }

            return inscriptionDto;
        }
        return null;
    }

    /*public InscriptionDto createDTOWithAttendance(Inscription inscription, boolean isPresent) {
        InscriptionDto inscriptionDto = new InscriptionDto().createDTO(inscription);
        inscriptionDto.setIsPresent(isPresent);
        return inscriptionDto;
    }*/
}
