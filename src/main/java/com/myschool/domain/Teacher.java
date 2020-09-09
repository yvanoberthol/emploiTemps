package com.myschool.domain;

import lombok.*;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 *
 * @author yvano
 */
@Entity
@Data
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

    @Column(name = "created_date")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private LocalDate createdDate;

    @Size(max = 20)
    @Column(name = "sexe")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String sexe;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "domicile")
    private String domicile;

    @JoinColumn(name = "promo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Promo promo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacher")
    private List<MatiereTeacher> matiereTeachers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacher")
    private TeacherPrincipal teacherPrincipal;


    public String getCompleteName(){
        return firstName +' '+lastName;
    }
}
