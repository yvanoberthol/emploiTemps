package com.myschool.repository;

import com.myschool.domain.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface GroupRepository extends JpaRepository<Group, Long>{

    Group findByName(String groupName);
}