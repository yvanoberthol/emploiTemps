package com.myschool.service;

import com.myschool.domain.StudentNote;
import com.myschool.dto.StudentNoteDto;
import com.myschool.repository.StudentNoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing StudentNote.
 */
@Service
@Transactional
public class StudentNoteService {

    private final Logger log = LoggerFactory.getLogger(StudentNoteService.class);

    @Autowired
    private StudentNoteRepository studentNoteRepository;


    public List<StudentNoteDto> findBySequenceTrimestreIdAndInscriptionStudentId(Long trimestreId, Long studentId) {
        List<StudentNote> studentNotes = studentNoteRepository
                .findBySequenceTrimestreIdAndInscriptionStudentId(trimestreId, studentId);
        List<StudentNoteDto> studentNoteDtos = new ArrayList<>();
        for(StudentNote note: studentNotes){
            studentNoteDtos.add(new StudentNoteDto().createDTO(note));
        }
        return studentNoteDtos;
    }

    public List<StudentNoteDto> findBySequenceIdAndInscriptionStudentId(Long sequenceId, Long studentId) {
        List<StudentNote> studentNotes = studentNoteRepository
                .findBySequenceIdAndInscriptionStudentId(sequenceId, studentId);
        List<StudentNoteDto> studentNoteDtos = new ArrayList<>();
        for(StudentNote note: studentNotes){
            studentNoteDtos.add(new StudentNoteDto().createDTO(note));
        }
        return studentNoteDtos;
    }

    public List<StudentNoteDto> findBySequenceTrimestreIdAndMatiereIdAndInscriptionStudentId(Long trimestreId, Long matiereId, Long studentId) {
        List<StudentNote> studentNotes = studentNoteRepository
                .findBySequenceTrimestreIdAndMatiereIdAndInscriptionStudentId(trimestreId, matiereId, studentId);
        List<StudentNoteDto> studentNoteDtos = new ArrayList<>();
        for(StudentNote note: studentNotes){
            studentNoteDtos.add(new StudentNoteDto().createDTO(note));
        }
        return studentNoteDtos;
    }

    public List<StudentNoteDto> findBySequenceTrimestreIdAndMatiereGroupeIdAndInscriptionStudentId(Long trimestreId, Long groupeId, Long studentId) {
        List<StudentNote> studentNotes = studentNoteRepository
                .findBySequenceTrimestreIdAndMatiereGroupeIdAndInscriptionStudentId(trimestreId, groupeId, studentId);
        List<StudentNoteDto> studentNoteDtos = new ArrayList<>();
        for(StudentNote note: studentNotes){
            studentNoteDtos.add(new StudentNoteDto().createDTO(note));
        }
        return studentNoteDtos;
    }
}
