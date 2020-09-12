package com.emploiTemps.rest;


import com.emploiTemps.dto.TeacherDto;
import com.emploiTemps.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing teacher.
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class TeacherResource {
    private final Logger log = LoggerFactory.getLogger(TeacherResource.class);

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/teachers")
    public Map<String, List<TeacherDto>> getAllTeachers() {
        log.debug("REST request to get Teachers");
        Map<String, List<TeacherDto>> map = new HashMap<>();
        map.put("results", teacherService.findAll());
        return map;
    }

}
