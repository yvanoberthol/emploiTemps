package com.myschool.repository;

import com.myschool.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContactRepository extends JpaRepository<Contact, Long>{

    //many to many!
    @Query("select Distinct contact from Contact contact "
            + "left join contact.groups g "
            + "where ( contact.firstName like ?1 OR contact.lastName like ?1) "
            + "and ( contact.phone like ?2 or ?2 = '%%') "
            + "and ( g.name like ?3 or ?3 = '%%') ")
    Page<Contact> findAll(String name, String phone, String group, Pageable pageable);

}