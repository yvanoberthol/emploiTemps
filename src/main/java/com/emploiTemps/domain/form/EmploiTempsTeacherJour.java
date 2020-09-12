package com.emploiTemps.domain.form;

import com.emploiTemps.dto.PromoDto;
import com.emploiTemps.dto.TeacherDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data @NoArgsConstructor @AllArgsConstructor
public class EmploiTempsTeacherJour implements Serializable {

    private TeacherDto teacher;

    private PromoDto promo;

    private int jour;
    private String jourString;

    private CoursTeacherJour[] coursTeacherJours;
}
