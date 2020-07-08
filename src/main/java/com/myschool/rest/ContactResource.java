package com.myschool.rest;

import com.myschool.domain.Contact;
import com.myschool.dto.ContactDto;
import com.myschool.repository.ContactRepository;
import com.myschool.repository.UserRepository;
import com.myschool.security.SecurityUtils;
import com.myschool.service.ContactService;
import com.myschool.utils.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * REST controller for managing Contact.
 */
@RestController
public class ContactResource {

    private final Logger log = LoggerFactory.getLogger(ContactResource.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * POST  /contacts : Create a new contact.
     *
     * @param contactDto the contact to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contact, or with status 400 (Bad Request) if the contact has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/contacts")
    public ResponseEntity<ContactDto> createContact(@Valid @RequestBody ContactDto contactDto) throws URISyntaxException {
        log.debug("REST request to save Contact : {}", contactDto);
        if (contactDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A contact with id " +
                    contactDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        return contactService.save(contactDto);
    }

    /**
     * PUT  /contacts : Updates an existing contact.
     *
     * @param contactDto the contact to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contact,
     * or with status 400 (Bad Request) if the contact is not valid,
     * or with status 500 (Internal Server Error) if the contact couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/contacts")
    public ResponseEntity<ContactDto> updateContact(@Valid @RequestBody ContactDto contactDto) throws URISyntaxException {
        log.debug("REST request to update Contact : {}", contactDto);
        if (contactDto.getId() == null) {
            return createContact(contactDto);
        }
        return contactService.update(contactDto);
    }

    @GetMapping("/api/contacts")
    public Page<ContactDto> getAllContacts(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                     @RequestParam(name = "size", defaultValue = "5") Integer size,
                                     @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                     @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                     @RequestParam(name = "name", defaultValue = "") String name,
                                     @RequestParam(name = "phone", defaultValue = "") String phone,
                                     @RequestParam(name = "group", defaultValue = "") String group
    ) {
        log.debug("REST request to get contacts");
        return contactService.findAll(page, size, sortBy, direction, name, phone, group);
    }

    /**
     * GET  /contacts/:id : get the "id" contact.
     *
     * @param id the id of the contact to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contact, or with status 404 (Not Found)
     */
    @GetMapping("/api/contacts/{id}")
    public ResponseEntity<ContactDto> getContact(@PathVariable Long id) {
        log.debug("REST request to get Contact : {}", id);

        ContactDto contactDto = contactService.findOne(id);
        return Optional.ofNullable(contactDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * DELETE  /contacts/:id : delete the "id" contact.
     *
     * @param id the id of the contact to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/contacts/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        log.debug("REST request to delete Contact : {}", id);
        Contact contact = contactRepository.findOne(id);
        if(Optional.ofNullable(contact).isPresent()){
            contactService.delete(id);
        }
        return new ResponseEntity<ContactDto>(HttpStatus.NO_CONTENT);
    }

}
