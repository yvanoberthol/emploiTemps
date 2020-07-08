package com.myschool.domain;

import com.myschool.domain.enumerations.DeliveryStatus;
import lombok.*;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author medilox
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sms")
public class Sms implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "sender")
    private String sender;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(unique = true)
    private String messageId;

    @Column(name = "recipient")
    private String recipient;

    /*@ElementCollection
    private List<String> recipients;*/

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sms")
    private Collection<SmsStudent> smsStudents;

    @Size(max = 20)
    @Column(name = "deliveryStatus")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String deliveryStatus;

    @Transient
    public DeliveryStatus getDeliveryStatus() {
        return DeliveryStatus.fromValue(deliveryStatus);
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus.toValue();
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

    public String getScheduledDate() {
        String pattern = "yyyy-MM-dd HH:mm";
        if(scheduledDate != null) {
            return scheduledDate.toString(pattern);
        }
        return null;
    }

    public void setScheduledDate(String scheduledDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cd = null;
        if(scheduledDate!=null)
            cd = LocalDateTime.parse(scheduledDate, formatter);
        this.scheduledDate = cd;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", messageId='" + messageId + '\'' +
                ", recipient='" + recipient + '\'' +
                ", createdDate=" + createdDate +
                //", user=" + user.getName() +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                '}';
    }
}
