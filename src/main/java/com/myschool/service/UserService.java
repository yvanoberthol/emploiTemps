package com.myschool.service;

import com.myschool.domain.*;
import com.myschool.dto.UserDto;
import com.myschool.repository.*;
import com.myschool.security.SecurityUtils;
import com.myschool.utils.RandomUtil;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service Implementation for managing UserBase.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional(readOnly = true)
    public UserDto findByUserIsCurrentUser() {
        User user = userRepository.findByLoginOrEmail(SecurityUtils.getCurrentUserLogin());
        return new UserDto().createDTO(user);
    }

    public void changePassword(String password) {
        User user = userRepository.findByLoginOrEmail(SecurityUtils.getCurrentUserLogin());
        log.debug("Changed password for UserBase: {}", user);
        if(user != null){
            //user.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(user);
        }
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
                .map(user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    user.setActivationKey(null);
                    userRepository.save(user);
                    //userSearchRepository.save(user);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime sdf = null;
        return userRepository.findOneByResetKey(key)
                .filter(user -> {
                    LocalDateTime oneDayAgo = new LocalDateTime().minusHours(24);
                    LocalDateTime ldt = null;
                    if(user.getResetDate() != null)
                        ldt = LocalDateTime.parse(user.getResetDate(), formatter);
                    return ldt.isAfter(oneDayAgo);
                })
                .map(user -> {
                    //user.setPassword(passwordEncoder.encode(newPassword));
                    user.setPassword(newPassword);
                    user.setResetKey(null);
                    user.setResetDate(null);
                    userRepository.save(user);
                    return user;
                });
    }

    public Optional<User> requestPasswordReset(String mail) {
        String pattern = "yyyy-MM-dd HH:mm";
        LocalDateTime datetime = new LocalDateTime();

        return userRepository.findOneByEmail(mail)
                .filter(User::getActivated)
                .map(user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(datetime.toString(pattern));
                    userRepository.save(user);
                    return user;
                });
    }

    /**
     * Save a user.
     *
     * @param userDto the entity to save
     * @return the persisted entity
     */
    /*public UserDto save(UserDto userDto, String roleName) {
        log.debug("Request to save User : {}", userDto);

        User user = new User();
        user.setId(userDto.getId());
        user.setLogin(userDto.getLogin());
        if(userDto.getEmail() != null)
        user.setEmail(userDto.getEmail().toLowerCase());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setLangKey(userDto.getLangKey());
        user.setActivated(false);

        //set created date;
        String pattern = "yyyy-MM-dd";
        LocalDate date = new LocalDate();
        user.setCreatedDate(date.toString(pattern));

        //before saving a user we encrypt password and we can give roles
        user.setPassword(userDto.getPassword());
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //user.setRoles(new HashSet<>(roleRepository.findAll()));

        user.setRoles(new ArrayList<>());
        Role role = roleRepository.findByName(roleName);
        if(role != null){user.getRoles().add(role); }

        User result =  userRepository.save(user);
        return new UserDto().createDTO(result);
    }*/


    /*public UserDto update(UserDto userDto) {
        log.debug("Request to save User : {}", userDto);

        User user = userRepository.findOne(userDto.getId());

        user.setId(userDto.getId());
        //currentUser.setPassword(userDto.getPassword());
        //user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setLangKey(userDto.getLangKey());
        user.setActivated(userDto.getActivated());

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());

        user.setRoles(new ArrayList<>());
        for(String roleName : userDto.getRoles()){
            Role role = roleRepository.findByName(roleName);
            if(role != null){user.getRoles().add(role); }
        }

        User result = userRepository.save(user);
        return new UserDto().createDTO(result);
    }*/

    /**
     *  Get all the users.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserDto>> findAll() {
        log.debug("Request to get all Users");
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        for (User user : users)
            userDtos.add(new UserDto().createDTO(user));
        return new ResponseEntity<List<UserDto>>(userDtos, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAllByRole(String roleName){
        log.debug("Request to get all Users by Role");
        List<User> users = userRepository.findAllByRole(roleName);
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users)
            userDtos.add(new UserDto().createDTO(user));

        //return new ResponseEntity<List<UserDto>>(userDtos, HttpStatus.OK);
        return userDtos;
    }

    public Page<UserDto> findAll(Integer page, Integer size, String sortBy, String direction, String name) {
        log.debug("Request to get all Users");

        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<User> users = userRepository.findAll("%"+name+"%", pageable);

        Page<UserDto> userDtos = users.map(user -> new UserDto().createDTO(user));
        return userDtos;
    }

    /*public Page<UserDto> findAll(Integer page, Integer size, String sortBy, String direction, String login, String email, String[] roles) {
        log.debug("Request to get all Users");

        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<User> users = userRepository.findAll("%"+login+"%", "%"+email+"%", roles, pageable);

        Page<UserDto> userDtos = users.map(user -> new UserDto().createDTO(user));
        return userDtos;
    }*/


    /**
     *  Get one user by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ResponseEntity<UserDto> findOne(Long id) {
        log.debug("Request to get User : {}", id);
        User user = userRepository.findOne(id);

        UserDto userDto = new UserDto().createDTO(user);
        return Optional.ofNullable(userDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     *  Delete the  user by id.
     *
     *  @param id the id of the entity
     */
    @Secured(value = {"ROLE_ADMIN"})
    public void delete(Long id) {
        log.debug("Request to delete User : {}", id);
        User user = userRepository.findOne(id);
        if(Optional.ofNullable(user).isPresent()){
        }
        userRepository.delete(id);
    }

   /*public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }*/
}

