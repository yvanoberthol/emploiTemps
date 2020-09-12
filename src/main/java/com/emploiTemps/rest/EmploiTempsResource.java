package com.emploiTemps.rest;

import com.emploiTemps.domain.form.Calendar;
import com.emploiTemps.service.CalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class EmploiTempsResource {

    private final Logger log = LoggerFactory.getLogger(MatiereResource.class);

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/emploitemps/classe/{idClasse}/{idTeacher}/{jour}")
    public Map<String, Calendar> getEmploiTempsClasse(
            @PathVariable Long idClasse,
            @PathVariable Long idTeacher,
            @PathVariable String jour) {

        // on vérifie la date
        Date jourAgenda = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            jourAgenda = sdf.parse(jour);
            System.out.println(jourAgenda.toString());
        } catch (ParseException e) {
            System.out.println(String.format("jour [%s] invalide", jour));
        }

        System.out.println(jour);

        Map<String, Calendar> map = new HashMap<>();
        map.put("results", calendarService.generateCalendar(idTeacher,idClasse,jourAgenda));

        return map;
    }
}
