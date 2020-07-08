package com.myschool.dto;


import com.myschool.domain.Inscription;
import com.myschool.domain.Reglement;
import lombok.Data;

/**
 * Created by medilox on 2/15/17.
 */
@Data
public class MouvementDto {

    private Long id;
    private double amount;
    private String createdDate;
    private String classe;
    private String type;

    public MouvementDto createDTO(Inscription i) {
        MouvementDto mouvementDto = new MouvementDto();
        mouvementDto.setType("Inscription");
        mouvementDto.setAmount(i.getPromo().getMontantScolarite() * -1);
        mouvementDto.setCreatedDate(i.getCreatedDate());
        mouvementDto.setClasse(i.getPromo().getClasse());
        return mouvementDto;
    }

    public MouvementDto createDTO(Reglement reglement) {
        MouvementDto mouvementDto = new MouvementDto();
        mouvementDto.setType("Reglement");
        mouvementDto.setAmount(reglement.getAmount());
        mouvementDto.setCreatedDate(reglement.getCreatedDate());
        mouvementDto.setClasse(reglement.getInscription().getPromo().getClasse());
        return mouvementDto;
    }
}
