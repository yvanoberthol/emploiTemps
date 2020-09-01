package com.myschool.dto;

import com.myschool.domain.CarteScolaire;
import com.myschool.domain.Promo;
import lombok.Data;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class CarteScolaireDto {

    private Long id;
    private String name;
    private String typeCarte;
    private Boolean selected;

    public CarteScolaireDto createDTO(CarteScolaire carteScolaire) {
        CarteScolaireDto carteScolaireDto = new CarteScolaireDto();
        if(carteScolaire != null){
            carteScolaireDto.setId(carteScolaire.getId());
            carteScolaireDto.setName(carteScolaire.getName());
            carteScolaireDto.setTypeCarte(carteScolaire.getTypeCarte().toValue());
            return carteScolaireDto;
        }
        return null;
    }

}
