package com.emploiTemps.service;

import com.emploiTemps.domain.Teacher;
import com.emploiTemps.domain.enumerations.Sexe;
import com.emploiTemps.dto.TeacherDto;
import com.emploiTemps.repository.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeacherService {

    private final Logger log = LoggerFactory.getLogger(TeacherService.class);

    @Autowired
    private TeacherRepository teacherRepository;

    public List<TeacherDto> findByMc(String mc) {
        List<Teacher> teachers = teacherRepository.findByMc("%"+mc+"%");
        List<TeacherDto> teacherDtos = new ArrayList<>();
        for (Teacher teacher : teachers)
            teacherDtos.add(new TeacherDto().createDTO(teacher));
        return teacherDtos;
    }

    /**
     * Save a teacher.
     *
     * @param teacherDto the entity to save
     * @return the persisted entity
     */
    public Teacher save(TeacherDto teacherDto) {
        log.debug("Request to save Teacher : {}", teacherDto);

        Teacher teacher = new Teacher();
        teacher.setId(teacherDto.getId());
        teacher.setFirstName(teacherDto.getFirstName());
        teacher.setLastName(teacherDto.getLastName());


        teacher.setSexe(Sexe.fromValue(teacherDto.getSexe()).toValue());
        teacher.setNationalite(teacherDto.getNationalite());
        teacher.setTelephone(teacherDto.getTelephone());

        return teacherRepository.save(teacher);
    }

    public TeacherDto update(TeacherDto teacherDto) {
        log.debug("Request to save Teacher : {}", teacherDto);

        Teacher teacher = teacherRepository.findOne(teacherDto.getId());

        teacher.setId(teacherDto.getId());
        teacher.setFirstName(teacherDto.getFirstName());
        teacher.setLastName(teacherDto.getLastName());

        teacher.setSexe(Sexe.fromValue(teacherDto.getSexe()).toValue());
        teacher.setNationalite(teacherDto.getNationalite());

        teacher.setTelephone(teacherDto.getTelephone());

        Teacher result = teacherRepository.save(teacher);
        return new TeacherDto().createDTO(result);
    }


    /**
     *  Get all the teachers.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TeacherDto> findAll(){
        log.debug("Request to get all Teachers");
        List<Teacher> teachers = teacherRepository.findAll();
        List<TeacherDto> teacherDtos = new ArrayList<>();

        for (Teacher teacher : teachers)
            teacherDtos.add(new TeacherDto().createDTO(teacher));
        return teacherDtos;
    }

    /**
     *  Get one teacher by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ResponseEntity<TeacherDto> findOne(Long id) {
        log.debug("Request to get Teacher : {}", id);
        Teacher teacher = teacherRepository.findOne(id);

        TeacherDto teacherDto = new TeacherDto().createDTO(teacher);
        return Optional.ofNullable(teacherDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public TeacherDto findOne(Long id, Long anneeId) {
        log.debug("Request to get Teacher : {}", id);
        Teacher teacher = teacherRepository.findOne(id);

        if(teacher != null){
            return new TeacherDto().createDTO(teacher);
        }
        return null;
    }

    public List<TeacherDto> findAll(Long anneeId, Long promoId) {
        List<TeacherDto> teacherDtos = new ArrayList<>();
        List<Teacher> teachers = teacherRepository.findByPromoId(promoId);
        for(Teacher teacher: teachers){
            teacherDtos.add(findOne(teacher.getId(), anneeId));
        }
        return teacherDtos;
    }
}
