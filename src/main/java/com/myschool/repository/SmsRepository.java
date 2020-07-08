package com.myschool.repository;

import com.myschool.domain.Sms;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SmsRepository extends JpaRepository<Sms, Long>{

    @Query("select sms from Sms sms " +
            "where sms.createdDate between ?1 and ?2 "
            + "And ( sms.sender like ?3 ) "
            + "And ( sms.recipient like ?4 ) ")
    Page<Sms> findAll(LocalDateTime cdf, LocalDateTime cdt, String sender, String recipient, Pageable pageable);

    Sms findByMessageId(String messageid);
}