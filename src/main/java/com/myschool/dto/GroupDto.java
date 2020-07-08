package com.myschool.dto;

import com.myschool.domain.Group;
import lombok.Data;


/**
 * Created by medilox on 3/10/17.
 */
@Data
public class GroupDto {

    private Long id;
    private String createdDate;
    private String name;
    private String description;
    private Integer nbContacts;

    public GroupDto createDTO(Group group) {
        GroupDto groupDto = new GroupDto();

        if(group != null){
            groupDto.setId(group.getId());
            groupDto.setCreatedDate(group.getCreatedDate());
            groupDto.setName(group.getName());
            groupDto.setDescription(group.getDescription());

            if(group.getContacts() != null)
                groupDto.setNbContacts(group.getContacts().size());
        }
        return groupDto;
    }
}
