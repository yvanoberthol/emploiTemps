package com.myschool.rest;

import com.myschool.domain.Group;
import com.myschool.dto.GroupDto;
import com.myschool.repository.GroupRepository;
import com.myschool.repository.UserRepository;
import com.myschool.security.SecurityUtils;
import com.myschool.service.GroupService;
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
 * REST controller for managing Group.
 */
@RestController
public class GroupResource {

    private final Logger log = LoggerFactory.getLogger(GroupResource.class);

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * POST  /groups : Create a new group.
     *
     * @param groupDto the group to create
     * @return the ResponseEntity with status 201 (Created) and with body the new group, or with status 400 (Bad Request) if the group has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/groups")
    public ResponseEntity<GroupDto> createGroup(@Valid @RequestBody GroupDto groupDto) throws URISyntaxException {
        log.debug("REST request to save Group : {}", groupDto);
        if (groupDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A group with id " +
                    groupDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        return groupService.save(groupDto);
    }

    /**
     * PUT  /groups : Updates an existing group.
     *
     * @param groupDto the group to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated group,
     * or with status 400 (Bad Request) if the group is not valid,
     * or with status 500 (Internal Server Error) if the group couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/groups")
    public ResponseEntity<GroupDto> updateGroup(@Valid @RequestBody GroupDto groupDto) throws URISyntaxException {
        log.debug("REST request to update Group : {}", groupDto);
        if (groupDto.getId() == null) {
            return createGroup(groupDto);
        }
        return groupService.update(groupDto);
    }

    @GetMapping("/api/groups")
    public Page<GroupDto> getAllGroups(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                           @RequestParam(name = "size", defaultValue = "5") Integer size,
                                           @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                           @RequestParam(name = "direction", defaultValue = "desc") String direction) {
        log.debug("REST request to get groups");
        return groupService.findByUserIsCurrentUser(page, size, sortBy, direction);
    }

    /**
     * GET  /groups/:id : get the "id" group.
     *
     * @param id the id of the group to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the group, or with status 404 (Not Found)
     */
    @GetMapping("/api/groups/{id}")
    public ResponseEntity<GroupDto> getGroup(@PathVariable Long id) {
        log.debug("REST request to get Group : {}", id);

        GroupDto groupDto = groupService.findOne(id);
        return Optional.ofNullable(groupDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * DELETE  /groups/:id : delete the "id" group.
     *
     * @param id the id of the group to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/groups/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        log.debug("REST request to delete Group : {}", id);
        Group group = groupRepository.findOne(id);
        if(Optional.ofNullable(group).isPresent()){
            groupService.delete(id);
        }
        return new ResponseEntity<GroupDto>(HttpStatus.NO_CONTENT);
    }

}
