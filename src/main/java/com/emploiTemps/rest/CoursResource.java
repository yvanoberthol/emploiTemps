package com.emploiTemps.rest;

import com.emploiTemps.domain.form.Lecon;
import com.emploiTemps.service.CoursService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing Matiere.
 */
@CrossOrigin
@RestController
public class CoursResource {

    private final Logger log = LoggerFactory.getLogger(CoursResource.class);

    @Autowired
    private CoursService coursService;

    @PostMapping("/api/cours/save")
    public Map<String, Boolean> save(@RequestBody Lecon lecon){

        boolean saved = coursService.save(lecon);
        Map<String, Boolean> map = new HashMap<>();
        map.put("result", saved);
        return map;
    }

    @DeleteMapping("/api/cours/delete/{idCours}")
    public Map<String, Boolean> save(@PathVariable Long idCours){

        boolean deleted = coursService.delete(idCours);
        Map<String, Boolean> map = new HashMap<>();
        map.put("result", deleted);
        return map;
    }
}
