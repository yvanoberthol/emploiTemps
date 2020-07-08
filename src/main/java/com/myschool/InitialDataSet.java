package com.myschool;

import com.myschool.domain.Role;
import com.myschool.domain.User;
import com.myschool.repository.RoleRepository;
import com.myschool.repository.UserRepository;
import com.myschool.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * This dataset prepares initial data and persist them into database before application is launched
 */

@Component
public class InitialDataSet {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public InitializingBean load() {
        createRoles();
        createDefaultUsers();
        return null;
    }

    private void createRoles() {
        List<String> roles = Arrays.asList("ADMIN", "USER");
        for(String role : roles){
            if(roleRepository.findByName(role) == null){
                roleRepository.save(new Role(role));
            }
        }
    }

    private void createDefaultUsers() {
        createAdministrators();
    }

    private void createAdministrators() {
        if(userRepository.findByLoginOrEmail("admin") == null){
            User admin = new User();
            admin.setLogin("admin");
            admin.setEmail("admin@gradebook.com");
            admin.setActivated(true);
            admin.setLangKey("fr");

            //before saving a user we encrypt password and we can give roles
            admin.setPassword("1234");
            //BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            //admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));

            Role role = roleRepository.findByName("ADMIN");

            if(role != null){
                admin.setRole(role);
            }
            userRepository.save(admin);
        }
    }
}
