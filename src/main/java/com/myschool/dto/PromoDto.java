package com.myschool.dto;

import com.myschool.domain.Groupe;
import com.myschool.domain.Matiere;
import com.myschool.domain.Promo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class PromoDto {

    private Long id;
    private Long anneeId;
    private String annee;
    private Long userId;
    private String classe;
    private String profPrincipal;
    private double montantScolarite;
    private int capacite;
    private Integer nbStudents;
    private Integer nbMatieres;
    private List<MatiereDto> matieres = new ArrayList<>();
    private List<GroupeDto> groupes = new ArrayList<>();

    public PromoDto createDTO(Promo promo) {
        PromoDto promoDto = new PromoDto();
        if(promo != null){
            promoDto.setId(promo.getId());
            promoDto.setMontantScolarite(promo.getMontantScolarite());
            promoDto.setCapacite(promo.getCapacite());
            promoDto.setClasse(promo.getClasse());
            promoDto.setProfPrincipal(promo.getProfPrincipal());

            if(promo.getAnnee() != null){
                promoDto.setAnneeId(promo.getAnnee().getId());
                promoDto.setAnnee(promo.getAnnee().toString());
            }

            if (promo.getInscriptions() != null) {
                promoDto.setNbStudents(promo.getInscriptions().size());
            }

            ArrayList<MatiereDto> matiereDtos = new ArrayList<>();
            ArrayList<GroupeDto> groupeDtos = new ArrayList<>();
            promoDto.setNbMatieres(0);
            if (promo.getGroupes() != null) {
                for(Groupe groupe: promo.getGroupes()){
                    groupeDtos.add(new GroupeDto().createDTO(groupe));
                }
            }
            promoDto.setGroupes(groupeDtos);

            if(promo.getMatieres() != null){
                for (Matiere matiere : promo.getMatieres()){
                    promoDto.setNbMatieres(promoDto.getNbMatieres() + 1);
                    matiereDtos.add(new MatiereDto().createDTO(matiere));
                }
            }
            promoDto.setMatieres(matiereDtos);

            return promoDto;
        }
        return null;
    }

    @Data
    private class StudentDto {
        private Long id;
        private String name;

        public StudentDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

}

