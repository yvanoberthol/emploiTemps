package com.emploiTemps.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatierTeacherId implements Serializable {
    private Long matiereId;
    private Long teacherId;
}
