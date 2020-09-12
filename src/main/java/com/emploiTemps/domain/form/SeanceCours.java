package com.emploiTemps.domain.form;

import com.emploiTemps.dto.PromoDto;
import com.emploiTemps.dto.SeanceDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SeanceCours implements Serializable {

    private List<SeanceDto> seanceDtos;

    private List<PromoDto> promoDtos;
}
