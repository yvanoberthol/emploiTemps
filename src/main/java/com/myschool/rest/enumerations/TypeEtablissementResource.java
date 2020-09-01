package com.myschool.rest.enumerations;

import com.myschool.domain.enumerations.TypeEtablissement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by medilox on 11/7/17.
 */

@RestController
@RequestMapping("/api")
public class TypeEtablissementResource {

    @RequestMapping(value = "/types-etablissement", method = RequestMethod.GET)
    public List<TypeEtablissementDto> getTypesEtablissement( ) {
        List<TypeEtablissementDto> result = new ArrayList<>();

        List<TypeEtablissement> typeEtablissements = Arrays.asList(TypeEtablissement.values());

        for(TypeEtablissement typeEtablissement : typeEtablissements){
            result.add(new TypeEtablissementDto(typeEtablissement.name(), typeEtablissement.toValue()));
        }

        return result;
    }

    private class TypeEtablissementDto {
        private String name;
        private String value;

        public TypeEtablissementDto(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}