package com.emploiTemps.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 *
 * @author yvano
 */
@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 20)
    @Column(name = "sexe")
    private String sexe;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "domicile")
    private String domicile;

    @JoinColumn(name = "promo_id", referencedColumnName = "id")
    @ManyToMany
    private List<Promo> promos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacher")
    private List<MatiereTeacher> matiereTeachers;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "teacher")
    private TeacherPrincipal teacherPrincipal;


    public String getCompleteName(){
        return firstName +' '+lastName;
    }
}
