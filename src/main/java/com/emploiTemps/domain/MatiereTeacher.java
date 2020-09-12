package com.emploiTemps.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 *
 * @author yvano
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "matiere_teacher")
public class MatiereTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Matiere matiere;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Teacher teacher;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matiereTeacher")
    private List<Cours> cours;
}
