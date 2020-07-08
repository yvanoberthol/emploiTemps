package com.myschool.repository;

import com.myschool.domain.StudentNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentNoteRepository extends JpaRepository<StudentNote, Long>{

    List<StudentNote> findBySequenceTrimestreIdAndMatiereIdAndInscriptionStudentId(Long trimestreId, Long matiereId, Long studentId);

    StudentNote findByInscriptionStudentIdAndInscriptionPromoIdAndSequenceIdAndMatiereId(Long studentId, Long promoId, Long sequenceId, Long matiereId);

    List<StudentNote> findBySequenceTrimestreIdAndInscriptionStudentId(Long trimestreId, Long studentId);

    List<StudentNote> findBySequenceIdAndInscriptionStudentId(Long sequenceId, Long studentId);

    List<StudentNote> findBySequenceTrimestreIdAndMatiereGroupeIdAndInscriptionStudentId(Long trimestreId, Long groupeId, Long studentId);
}