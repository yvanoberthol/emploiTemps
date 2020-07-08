package com.myschool.rest;

import com.myschool.domain.Student;
import com.myschool.dto.StudentDto;
import com.myschool.dto.StudentNoteDto;
import com.myschool.dto.StudentWithNotesDto;
import com.myschool.dto.StudentWithNotesForMatiereDto;
import com.myschool.repository.StudentRepository;
import com.myschool.service.StudentService;
import com.myschool.utils.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing Student.
 */
@CrossOrigin
@RestController
//@RequestMapping("/api")
public class StudentResource {

    private final Logger log = LoggerFactory.getLogger(StudentResource.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;


    /**
     * POST  /students : Create a new student.
     *
     * @param studentDto the student to create
     * @return the ResponseEntity with status 201 (Created) and with body the new student, or with status 400 (Bad Request) if the student has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    /*@PostMapping("/api/students")
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto studentDto,
                                                  HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Student : {}", studentDto);
        HashMap<String, String> error = new HashMap<>();
        if (studentDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A student with id " +
                    studentDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        if(studentRepository.findByLogin(studentDto.getLogin()) != null){
            error.put("error", "login already in use");
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
        if(studentRepository.findByEmail(studentDto.getEmail()) != null){
            error.put("error", "email already in use");
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }

        StudentDto result = studentService.save(studentDto);

        String baseUrl = request.getScheme() + // "http"
        "://" +                                // "://"
        request.getServerName() +              // "myhost"
        ":" +                                  // ":"
        request.getServerPort() +              // "80"
        request.getContextPath();              // "/myContextPath" or "" if deployed in root context

        //mailService.sendActivationEmail(result, baseUrl);
        return new ResponseEntity<StudentDto>(result, HttpStatus.CREATED);
    }*/

    /**
     * PUT  /students : Updates an existing student.
     *
     * @param studentDto the student to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated student,
     * or with status 400 (Bad Request) if the student is not valid,
     * or with status 500 (Internal Server Error) if the student couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @CrossOrigin
    @PutMapping("/api/students")
    public ResponseEntity<StudentDto> updateStudent(@Valid @RequestBody StudentDto studentDto) throws URISyntaxException {
        log.debug("REST request to update Student : {}", studentDto);
        if (studentDto.getId() == null) {
            return new ResponseEntity(new CustomErrorType("Unable to update. A student id can not be null."), HttpStatus.BAD_REQUEST);
        }
        StudentDto result = studentService.update(studentDto);
        return new ResponseEntity<StudentDto>(result, HttpStatus.OK);
    }


    /**
     * GET  /students : get all the students.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of students in body
     */
    /*
    @GetMapping("/api/students")
    public List<StudentDto> getAllStudents() {
        log.debug("REST request to get all Students");
        return studentService.findAll();
    }
    */

    @PostMapping("/api/students-save-note")
    public ResponseEntity<StudentNoteDto> saveNote(@Valid @RequestBody StudentNoteDto studentNoteDto) throws URISyntaxException {
        log.debug("REST request to update Student : {}", studentNoteDto);

        if(studentNoteDto.getNote() != null){
            StudentNoteDto result = studentService.saveNote(studentNoteDto);
            return new ResponseEntity<StudentNoteDto>(result, HttpStatus.OK);
        }
        else{
            studentService.deleteNote(studentNoteDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/api/students-by-annee/{anneeId}")
    public Page<StudentDto> getStudentsByAnnee(@PathVariable Long anneeId,
                                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                                               @RequestParam(name = "size", defaultValue = "10") Integer size,
                                               @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                               @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                               @RequestParam(name = "name", defaultValue="") String name,
                                               @RequestParam(name = "promo", defaultValue="0") Long promoId) {
        log.debug("REST request to get all students");
        return studentService.findByAnnee(page, size, sortBy, direction, anneeId, name, promoId);
    }

    @GetMapping("/api/students-by-promo/{promoId}")
    public List<StudentDto> getStudentsByPromo(@PathVariable Long promoId,
                                               @RequestParam(name = "name", defaultValue="") String name) {
        log.debug("REST request to get all students");
        return studentService.findByPromo(promoId, name);
    }

    @GetMapping("/api/students-by-promo-with-notes/{promoId}")
    public List<StudentWithNotesDto> getStudentsByPromoWithNotes(@PathVariable Long promoId,
                                                                 @RequestParam(name = "trimestreId", defaultValue = "0") Long trimestreId,
                                                                 @RequestParam(name = "name", defaultValue="") String name) {
        log.debug("REST request to get all students");
        return studentService.findByPromoWithNotes(name, promoId, trimestreId);
    }

    @GetMapping("/api/students-by-promo-with-notes-for-matiere-and-trimestre/{promoId}")
    public Page<StudentWithNotesForMatiereDto> getStudentsByPromoWithNotesForMatiereAndNote(@PathVariable Long promoId,
                                                                                            @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                                            @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                                                            @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                                                                            @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                                                                            @RequestParam(name = "matiereId", defaultValue = "0") Long matiereId,
                                                                                            @RequestParam(name = "trimestreId", defaultValue = "0") Long trimestreId,
                                                                                            @RequestParam(name = "name", defaultValue="") String name) {
        log.debug("REST request to get all students");
        return studentService.findByPromoWithNotesForMatiereAndNoteId(page, size, sortBy, direction, name, promoId, matiereId, trimestreId);
    }


    @GetMapping("/api/students-search")
    public Map<String, List<StudentDto>> getAllStudents(@RequestParam(name = "mc") String mc) {
        log.debug("REST request to get Students");
        Map<String, List<StudentDto>> map = new HashMap<>();
        map.put("results", studentService.findByMc(mc));
        return map;
    }


    /**
     * GET  /students/:id : get the "id" student.
     *
     * @param id the id of the student to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the student, or with status 404 (Not Found)
     */
    @GetMapping("/api/students/{id}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable Long id) {
        log.debug("REST request to get Student : {}", id);
        return studentService.findOne(id);
    }

    @GetMapping("/api/student-by-annee/{id}")
    public StudentDto getStudentByAnnee(@PathVariable Long id, @RequestParam(name = "anneeId") Long anneeId) {
        log.debug("REST request to get Student : {}", id);
        return studentService.findOne(id, anneeId);
    }

    @GetMapping("/api/students-by-annee")
    public List<StudentDto> getStudentsByAnnee(@RequestParam(name = "anneeId") Long anneeId,
                                               @RequestParam(name = "promoId") Long promoId) {
        log.debug("REST request to get Student : {}");
        return studentService.findAll(anneeId, promoId);
    }

    /**
     * DELETE  /students/:id : delete the "id" student.
     *
     * @param id the id of the student to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        log.debug("REST request to delete Student : {}", id);
        studentService.delete(id);
        return new ResponseEntity<Student>(HttpStatus.NO_CONTENT);
    }

}
