package com.myschool.repository;

import com.myschool.domain.MatierTeacherId;
import com.myschool.domain.MatiereTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatiereTeacherRepository extends JpaRepository<MatiereTeacher,MatierTeacherId> {
}
