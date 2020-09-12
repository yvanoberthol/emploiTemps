package com.emploiTemps.domain;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 *
 * @author yvano
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seance")
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date jour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cours")
    private Cours cours;

    private boolean enabled = false;
}
