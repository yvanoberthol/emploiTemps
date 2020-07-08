package com.myschool.service;

import com.myschool.domain.*;
import com.myschool.domain.enumerations.Civilite;
import com.myschool.dto.ContactDto;
import com.myschool.repository.*;
import com.myschool.repository.ContactRepository;
import com.myschool.security.SecurityUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Service Implementation for managing Contact.
 */
@Service
@Transactional
public class ContactService {

    private final Logger log = LoggerFactory.getLogger(ContactService.class);

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private GroupRepository groupRepository;


    /**
     * Save a contact.
     *
     * @param contactDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<ContactDto> save(ContactDto contactDto) {
        log.debug("Request to save Contact : {}", contactDto);

        Contact contact = new Contact();

        //set created date;
        String pattern = "yyyy-MM-dd HH:mm";
        LocalDateTime date = new LocalDateTime();
        contact.setCreatedDate(date.toString(pattern));

        contact.setId(contactDto.getId());
        contact.setPhone(contactDto.getPhone());
        contact.setCivilite(Civilite.fromValue(contactDto.getCivilite()));
        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());

        ArrayList<Group> groups = new ArrayList<>();
        if (contactDto.getGroups() != null) {
            for (String groupName : contactDto.getGroups()){
                groups.add(groupRepository.findByName(groupName));
            }
        }
        contact.setGroups(groups);
        Contact result = contactRepository.save(contact);

        return new ResponseEntity<ContactDto>(new ContactDto().createDTO(result), HttpStatus.CREATED);
    }

    public ResponseEntity<ContactDto> update(ContactDto contactDto) {
        log.debug("Request to save Contact : {}", contactDto);

        Contact contact = contactRepository.findOne(contactDto.getId());

        contact.setCreatedDate(contactDto.getCreatedDate());

        contact.setId(contactDto.getId());
        contact.setPhone(contactDto.getPhone());
        contact.setCivilite(Civilite.fromValue(contactDto.getCivilite()));
        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());

        ArrayList<Group> groups = new ArrayList<>();
        if (contactDto.getGroups() != null) {
            for (String groupName : contactDto.getGroups()){
                groups.add(groupRepository.findByName(groupName));
            }
        }
        contact.setGroups(groups);

        Contact result = contactRepository.save(contact);

        return new ResponseEntity<ContactDto>(new ContactDto().createDTO(result), HttpStatus.CREATED);
    }

    /**
     *  Get all the contacts.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Contact> findAll(Pageable pageable) {
        log.debug("Request to get all Contacts");
        Page<Contact> result = contactRepository.findAll(pageable);
        return result;
    }


    public Page<ContactDto> findAll(Integer page, Integer size, String sortBy, String direction, String name, String phone, String group) {
        log.debug("Request to get all contacts per user");

        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Contact> contacts = contactRepository.findAll("%"+name+"%", "%"+phone+"%","%"+group+"%",  pageable);

        Page<ContactDto> contactDtos = contacts.map(contact -> new ContactDto().createDTO(contact));
        return contactDtos;
    }

    /**
     *  Get one contact by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ContactDto findOne(Long id) {
        log.debug("Request to get Contact : {}", id);
        Contact contact = contactRepository.findOne(id);

        return new ContactDto().createDTO(contact);
    }

    /**
     *  Delete the  contact by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Contact : {}", id);
        Contact contact = contactRepository.findOne(id);
        if(Optional.ofNullable(contact).isPresent()){
            contactRepository.delete(id);
        }

    }
}
