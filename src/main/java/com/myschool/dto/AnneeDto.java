package com.myschool.dto;

import com.myschool.domain.Annee;
import com.myschool.domain.Promo;
import lombok.Data;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class AnneeDto {

    private Long id;
    private String label;
    private int debut;
    private int fin;
    private Boolean active;
    private int nbClasses;
    private int nbStudents;
    private String typeEtablissement;

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

    private Long bulletinId;
    private Long carteScolaireId;
    private Long tableauHonneurId;
    private Long recuId;

    public AnneeDto createDTO(Annee annee) {
        AnneeDto anneeDto = new AnneeDto();
        if(annee != null){
            anneeDto.setId(annee.getId());
            anneeDto.setDebut(annee.getDebut());
            anneeDto.setFin(annee.getFin());
            anneeDto.setTypeEtablissement(String.valueOf(annee.getTypeEtablissement()));
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

            if(annee.getBulletin() != null){
                anneeDto.setBulletinId(annee.getBulletin().getId());
            }
            if(annee.getCarteScolaire() != null){
                anneeDto.setCarteScolaireId(annee.getCarteScolaire().getId());
            }
            if(annee.getTableauHonneur() != null){
                anneeDto.setTableauHonneurId(annee.getTableauHonneur().getId());
            }
            if(annee.getRecu() != null){
                anneeDto.setRecuId(annee.getRecu().getId());
            }

            if(annee.getPromos() !=  null){
                int nbStudents = 0;
                for(Promo promo: annee.getPromos()){
                    anneeDto.setNbClasses(anneeDto.getNbClasses() + 1);
                    nbStudents += promo.getInscriptions().size();
                }
                anneeDto.setNbStudents(nbStudents);
            }

            anneeDto.setActive(annee.getActive());
            return anneeDto;
        }
        return null;
    }

}
