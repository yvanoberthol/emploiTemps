package com.emploiTemps.dto;

import com.emploiTemps.domain.Annee;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class AnneeDto implements Serializable {

    private Long id;
    private String label;
    private int debut;
    private int fin;
    private Boolean active;

    private String nomEtablissement;
    private String nameEtablissement;
    private String slogan;
    private String pays;
    private String region;
    private String departement;
    private String ville;
    private String adresse;
    private String phone;
    private String senderId;

    public AnneeDto createDTO(Annee annee) {

        if(annee != null){
            AnneeDto anneeDto = new AnneeDto();
            anneeDto.setId(annee.getId());
            anneeDto.setDebut(annee.getDebut());
            anneeDto.setFin(annee.getFin());
            anneeDto.setLabel(annee.toString());

            anneeDto.setNomEtablissement(annee.getNomEtablissement());
            anneeDto.setNameEtablissement(annee.getNameEtablissement());
            anneeDto.setSlogan(annee.getSlogan());
            anneeDto.setPays(annee.getPays());
            anneeDto.setRegion(annee.getRegion());
            anneeDto.setDepartement(annee.getDepartement());
            anneeDto.setVille(annee.getVille());
            anneeDto.setAdresse(annee.getAdresse());
            anneeDto.setPhone(annee.getPhone());
            anneeDto.setSenderId(annee.getSenderId());
            anneeDto.setActive(annee.getActive());
            return anneeDto;
        }
        return null;
    }

    public String getSessionYear() {
        return this.debut + " - " + this.fin;
    }

}
