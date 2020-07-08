package com.myschool.rest.enumerations;

import com.myschool.domain.enumerations.Sexe;
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
public class SexeResource {

    @RequestMapping(value = "/sexes", method = RequestMethod.GET)
    public List<SexeDto> getSexes( ) {
        List<SexeDto> result = new ArrayList<>();

        List<Sexe> sexes = Arrays.asList(Sexe.values());

        for(Sexe sexe : sexes){
            result.add(new SexeDto(sexe.name(), sexe.toValue()));
        }

        return result;
    }

    private class SexeDto {
        private String name;
        private String value;

        public SexeDto(String name, String value) {
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