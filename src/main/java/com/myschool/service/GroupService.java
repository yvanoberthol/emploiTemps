package com.myschool.service;

import com.myschool.domain.Group;
import com.myschool.dto.GroupDto;
import com.myschool.repository.GroupRepository;
import com.myschool.repository.UserRepository;
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

import java.util.Optional;

/**
 * Service Implementation for managing Group.
 */
@Service
@Transactional
public class GroupService {

    private final Logger log = LoggerFactory.getLogger(GroupService.class);

    @Autowired
    private GroupRepository groupRepository;

    /**
     * Save a group.
     *
     * @param groupDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<GroupDto> save(GroupDto groupDto) {
        log.debug("Request to save Group : {}", groupDto);

        Group group = new Group();

        //set created date;
        String pattern = "yyyy-MM-dd HH:mm";
        LocalDateTime date = new LocalDateTime();
        group.setCreatedDate(date.toString(pattern));

        group.setId(groupDto.getId());
        group.setName(groupDto.getName());
        group.setDescription(groupDto.getDescription());

        Group result = groupRepository.save(group);

        return new ResponseEntity<GroupDto>(new GroupDto().createDTO(result), HttpStatus.CREATED);
    }

    public ResponseEntity<GroupDto> update(GroupDto groupDto) {
        log.debug("Request to save Group : {}", groupDto);

        Group group = groupRepository.findOne(groupDto.getId());

        group.setCreatedDate(groupDto.getCreatedDate());

        group.setId(groupDto.getId());
        group.setName(groupDto.getName());
        group.setDescription(groupDto.getDescription());

        Group result = groupRepository.save(group);

        return new ResponseEntity<GroupDto>(new GroupDto().createDTO(result), HttpStatus.CREATED);
    }

    /**
     *  Get all the groups.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Group> findAll(Pageable pageable) {
        log.debug("Request to get all Groups");
        Page<Group> result = groupRepository.findAll(pageable);
        return result;
    }


    public Page<GroupDto> findByUserIsCurrentUser(Integer page, Integer size, String sortBy, String direction) {
        log.debug("Request to get all Users");

        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Group> groups = groupRepository.findAll(pageable);

        Page<GroupDto> groupDtos = groups.map(group -> new GroupDto().createDTO(group));
        return groupDtos;
    }


    /**
     *  Get one group by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public GroupDto findOne(Long id) {
        log.debug("Request to get Group : {}", id);
        Group group = groupRepository.findOne(id);

        return new GroupDto().createDTO(group);
    }

    /**
     *  Delete the  group by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Group : {}", id);
        Group group = groupRepository.findOne(id);
        groupRepository.delete(id);
    }
}
