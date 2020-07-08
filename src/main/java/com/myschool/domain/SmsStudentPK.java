/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myschool.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author medilox
 */
@Embeddable
public class SmsStudentPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "sms_id")
    private long smsId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "student_id")
    private long studentId;

    public SmsStudentPK() {
    }

    public SmsStudentPK(Long smsId, Long studentId) {
        this.smsId = smsId;
        this.studentId = studentId;
    }

    public long getSmsId() {
        return smsId;
    }

    public void setSmsId(long smsId) {
        this.smsId = smsId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) smsId;
        hash += (int) studentId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SmsStudentPK)) {
            return false;
        }
        SmsStudentPK other = (SmsStudentPK) object;
        if (this.smsId != other.smsId) {
            return false;
        }
        if (this.studentId != other.studentId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.SmsStudentPK[ smsId=" + smsId + ", studentId=" + studentId + " ]";
    }
    
}
