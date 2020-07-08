package com.myschool.dto;

import com.myschool.domain.Contact;
import com.myschool.domain.Group;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by medilox on 3/10/17.
 */
@Data
public class ContactDto {

    private Long id;
    private String createdDate;
    private String phone;
    private String civilite;
    private String firstName;
    private String lastName;
    private String name;
    private List<String> groups;

    public ContactDto createDTO(Contact contact) {
        ContactDto contactDto = new ContactDto();

        if(contact != null){
            contactDto.setId(contact.getId());
            contactDto.setCreatedDate(contact.getCreatedDate());
            contactDto.setPhone(contact.getPhone());
            contactDto.setCivilite(String.valueOf(contact.getCivilite()));
            contactDto.setFirstName(contact.getFirstName());
            contactDto.setLastName(contact.getLastName());
            contactDto.setName(contact.getName());

            ArrayList<String> groups = new ArrayList<>();
            if(contact.getGroups() != null){
                for (Group group : contact.getGroups())
                    groups.add(group.getName());
            }
            contactDto.setGroups(groups);
        }
        return contactDto;
    }
}
