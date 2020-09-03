package com.myschool.dto;

import com.myschool.domain.Groupe;
import com.myschool.domain.Inscription;
import com.myschool.domain.Matiere;
import com.myschool.domain.Promo;
import com.myschool.domain.enumerations.Sexe;
import com.myschool.domain.enumerations.Statut;
import lombok.Data;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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

    private int nbMales;
    private int nbFemales;
    private InscriptionDto minAge;
    private InscriptionDto maxAge;
    private int nbNews;
    private int nbRedoublants;

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

                DateTimeFormatter formatter = DateTimeFormat.forPattern("ddMMyyyy");
                int nbMales = 0, nbFemales = 0, nbNews = 0, nbRedoublants = 0;
                Inscription minAge = null;
                Inscription maxAge = null;

                if(promo.getInscriptions().size() != 0){
                    minAge = promo.getInscriptions().get(0);
                    maxAge = promo.getInscriptions().get(0);
                }


                for(Inscription inscription: promo.getInscriptions()){
                    if(inscription.getStudent().getSexe().equals(Sexe.Masculin))
                        nbMales++;
                    else
                        nbFemales++;

                    if(inscription.getStudent().getStatut().equals(Statut.Nouveau))
                        nbNews++;
                    else
                        nbRedoublants++;

                    if(promo.getInscriptions().size() != 0){
                        if(formatter.parseLocalDate(inscription.getStudent().getDateNaissance())
                                .isAfter( formatter.parseLocalDate(minAge.getStudent().getDateNaissance()) )){
                            minAge = inscription;
                        }
                        if(formatter.parseLocalDate(inscription.getStudent().getDateNaissance())
                                .isBefore( formatter.parseLocalDate(maxAge.getStudent().getDateNaissance()) )){
                            maxAge = inscription;
                        }
                    }

                }

                if(promo.getInscriptions().size() != 0){
                    promoDto.setNbMales(nbMales);
                    promoDto.setNbFemales(nbFemales);
                    promoDto.setNbNews(nbNews);
                    promoDto.setNbRedoublants(nbRedoublants);

                    InscriptionDto minAgeDto = new InscriptionDto().createDTO(minAge);
                    minAgeDto.getStudent().setAge(promo.getAnnee().getFin() - formatter.parseLocalDate(minAgeDto.getStudent().getDateNaissance()).getYear());
                    promoDto.setMinAge(minAgeDto);

                    InscriptionDto maxAgeDto = new InscriptionDto().createDTO(maxAge);
                    maxAgeDto.getStudent().setAge(promo.getAnnee().getFin() - formatter.parseLocalDate(maxAgeDto.getStudent().getDateNaissance()).getYear());
                    promoDto.setMaxAge(maxAgeDto);
                }
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

