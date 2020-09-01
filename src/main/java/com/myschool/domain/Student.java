package com.myschool.domain;

import com.myschool.domain.enumerations.Sexe;
import com.myschool.domain.enumerations.Statut;
import lombok.*;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by medilox on 11/5/17.
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

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

    @Size(max = 20)
    @Column(name = "statut")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String statut;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "dateNaissance")
    private LocalDate dateNaissance;

    @Column(name = "lieuNaissance")
    private String lieuNaissance;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "matricule")
    private String matricule;

    @Column(name = "fatherName")
    private String fatherName;

    @Column(name = "motherName")
    private String motherName;

    @Column(name = "fatherPhone")
    private String fatherPhone;

    @Column(name = "motherPhone")
    private String motherPhone;

    @Column(name = "fatherProfession")
    private String fatherProfession;

    @Column(name = "motherProfession")
    private String motherProfession;

    @Column(name = "otherInfos")
    private String otherInfos;

    @Column(name = "domicile")
    private String domicile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private List<Inscription> inscriptions;

    @Transient
    public Sexe getSexe() {
        return Sexe.fromValue(sexe);
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe.toValue();
    }

    @Transient
    public Statut getStatut() {
        return Statut.fromValue(statut);
    }

    public void setStatut(Statut statut) {
        this.statut = statut.toValue();
    }

    public String getCreatedDate() {
        String pattern = "yyyy-MM-dd";
        if(createdDate != null) {
            return createdDate.toString(pattern);
        }
        return null;
    }

    public void setCreatedDate(String createdDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate cd = null;
        if(createdDate!=null)
            cd = formatter.parseLocalDate(createdDate);
        this.createdDate = cd;
    }

    public String getDateNaissance() {
        String pattern = "ddMMyyyy";
        if(dateNaissance != null) {
            return dateNaissance.toString(pattern);
        }
        return null;
    }

    public String getDateNaissanceFormatted() {
        String pattern = "dd/MM/yyyy";
        if(dateNaissance != null) {
            return dateNaissance.toString(pattern);
        }
        return null;
    }

    public void setDateNaissance(String dateNaissance) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("ddMMyyyy");
        LocalDate cd = null;
        if(dateNaissance!=null)
            cd = formatter.parseLocalDate(dateNaissance);
        this.dateNaissance = cd;
    }

    public String getName() {
        String name = "";
        if(this.lastName != null)
            name += this.lastName;

        if(this.firstName != null)
            name += " " + this.firstName;

        return name;
    }
}

