package com.emploiTemps.domain.form;

import com.emploiTemps.dto.CreneauHoraireDto;
import com.emploiTemps.dto.PromoDto;
import com.emploiTemps.dto.TeacherDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Data
public class Calendar implements Serializable {

    private List<EmploiTempsTeacherJour> emploiTempsTeacherJours;
    private List<CreneauHoraireDto> creneauHoraires;
    private Map<Integer,String> daysWeek = new Hashtable<>();
    private PromoDto promoDto;
    private List<TeacherDto> teacherDtos;
}
