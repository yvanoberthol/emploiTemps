package com.myschool.domain;

import com.myschool.domain.enumerations.Civilite;
import lombok.*;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author medilox
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contacts")
public class Contact implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Size(max = 20)
    @Column(name = "civilite")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String civilite;

    @JoinTable(name = "contact_group", joinColumns = {
            @JoinColumn(name = "contact_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "group_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Group> groups;

    @Transient
    public Civilite getCivilite() {
        return Civilite.fromValue(civilite);
    }

    public void setCivilite(Civilite civilite) {
        this.civilite = civilite.toValue();
    }

    public String getCreatedDate() {
        String pattern = "yyyy-MM-dd HH:mm";
        if(createdDate != null) {
            return createdDate.toString(pattern);
        }
        return null;
    }

    public void setCreatedDate(String createdDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cd = null;
        if(createdDate!=null)
            cd = LocalDateTime.parse(createdDate, formatter);
        this.createdDate = cd;
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
