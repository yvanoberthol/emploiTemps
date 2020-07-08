package com.myschool.dto;

import com.myschool.domain.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by medilox on 3/10/17.
 */
@Data
public class UserDto {

    private Long id;
    private String login;
    private String email;
    private String password;
    private String name;

    private Boolean activated;
    private String langKey;
    private String activationKey;
    private String resetKey;
    private String createdDate;
    private String resetDate;
    private String role;
    private String badge;

    private Boolean validated;
    private Boolean credentialsSent;

    public UserDto() {
    }

    public UserDto(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.activated = true;
    }

    public UserDto createDTO(User user) {
        if(user != null){
            UserDto userDto = new UserDto();

            userDto.setId(user.getId());
            userDto.setLogin(user.getLogin());
            userDto.setEmail(user.getEmail());
            userDto.setLangKey(user.getLangKey());
            userDto.setActivated(user.getActivated());
            userDto.setCreatedDate(user.getCreatedDate());
            userDto.setActivationKey(user.getActivationKey());
            userDto.setResetDate(user.getResetDate());
            userDto.setResetKey(user.getResetKey());

            userDto.setLangKey(user.getLangKey());

            if(user.getRole() != null){
                userDto.setRole(user.getRole().getName());
                if(userDto.getRole().equals("ADMIN"))
                    userDto.setBadge("success");
                if(userDto.getRole().equals("MANAGER"))
                    userDto.setBadge("warning");
                if(userDto.getRole().equals("DRIVER"))
                    userDto.setBadge("primary");
            }
            return userDto;
        }
        return null;
    }
}
