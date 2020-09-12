package com.emploiTemps.rest;


import com.emploiTemps.domain.form.SeanceCours;
import com.emploiTemps.dto.SeanceDto;
import com.emploiTemps.service.SeanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * REST controller for managing Promo.
 */
@CrossOrigin
@RestController
public class SeanceResource {

    private final Logger log = LoggerFactory.getLogger(SeanceResource.class);

    @Autowired
    private SeanceService seanceService;

    @PostMapping("/api/seances/save")
    public void saveSeance(@RequestBody SeanceDto seanceDto){
        seanceService.save(seanceDto);
    }

    @GetMapping("/api/seances-by-teacher/{idTeacher}/{debut}/{fin}")
    public SeanceCours getSeanceByTeachers(
            @PathVariable Long idTeacher,
            @PathVariable String debut,
            @PathVariable String fin
    ){


        // on vérifie la date
        Date jourDebut = null;
        Date jourFin = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            jourDebut = sdf.parse(debut);
            jourFin = sdf.parse(fin);
        } catch (ParseException e) {
            System.out.println(String.format("jour [%s,%s] invalide", jourDebut,jourFin));
        }

        return seanceService.getSeanceTeachers(idTeacher,jourDebut,jourFin);
    }
}
