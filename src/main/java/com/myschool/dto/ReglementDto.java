package com.myschool.dto;


import com.myschool.domain.Reglement;
import lombok.Data;

/**
 * Created by medilox on 2/15/17.
 */
@Data
public class ReglementDto {

    private Long id;
    private double amount;
    private String createdDate;

    //private Long inscriptionId;

    private Long studentId;
    private String student;

    private Long promoId;
    private String promo;

    private String paymentMethod;
    private Boolean paymentValidated;
    private String paymentValidatedDate;

    private Long staffId;
    private String staff;

    public ReglementDto createDTO(Reglement reglement) {
        ReglementDto reglementDto = new ReglementDto();
        if(reglement != null){
            reglementDto.setId(reglement.getId());
            reglementDto.setAmount(reglement.getAmount());
            reglementDto.setCreatedDate(reglement.getCreatedDate());
            reglementDto.setPaymentMethod(reglement.getPaymentMethod().toValue());
            reglementDto.setPaymentValidated(reglement.getPaymentValidated());
            reglementDto.setPaymentValidatedDate(reglement.getPaymentValidatedDate());

            if(reglement.getInscription() != null){
                if(reglement.getInscription().getStudent() != null){
                    reglementDto.setStudentId(reglement.getInscription().getStudent().getId());
                    reglementDto.setStudent(reglement.getInscription().getStudent().getName());
                }
                if(reglement.getInscription().getPromo() != null){
                    reglementDto.setPromoId(reglement.getInscription().getPromo().getId());
                    reglementDto.setPromo(reglement.getInscription().getPromo().toString());
                }
            }

            if(reglement.getStaff() != null){
                reglementDto.setStaffId(reglement.getStaff().getId());
                reglementDto.setStaff(reglement.getStaff().getLogin());
            }
        }
        return reglementDto;
    }

}
