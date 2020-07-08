package com.myschool.dto;

import com.myschool.domain.Role;

/**
 * Created by medilox on 30/09/18.
 */
public class RoleDto {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoleDto createDTO(Role role) {
        RoleDto roleDto = new RoleDto();
        if(role != null){
            roleDto.setId(role.getId());
            roleDto.setName(role.getName());
        }
        return roleDto;
    }
}
