package com.emploiTemps.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
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
@Table(name = "creneau")
public class CreneauHoraire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private Date heureDebut;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private Date heureFin;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creneauHoraire")
    private List<Cours> cours;

    public String getCreneauString(){
        return heureDebut+" "+heureFin;
    }
}
