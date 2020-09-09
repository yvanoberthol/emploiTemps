package com.myschool.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 *
 * @author yvano
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "matiere_teacher")
public class MatiereTeacher {

    @EmbeddedId
    private MatierTeacherId matierTeacherId;

    @MapsId("matiereId")
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "matiere_id",updatable = false,insertable = false)
    private Matiere matiere;

    @MapsId("teacherId")
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "teacher_id",updatable = false,insertable = false)
    private Teacher teacher;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matiereTeacher")
    private List<Cours> cours;
}
