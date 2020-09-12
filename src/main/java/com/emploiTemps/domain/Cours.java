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
@Table(name = "cours")
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    private int jour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_creneau")
    private CreneauHoraire creneauHoraire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiereteacher_id")
    private MatiereTeacher matiereTeacher;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cours")
    private List<Seance> seances;
}
