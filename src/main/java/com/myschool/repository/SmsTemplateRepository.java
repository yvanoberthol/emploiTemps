
package com.myschool.repository;

import com.myschool.domain.SmsTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmsTemplateRepository extends JpaRepository<SmsTemplate, Long>{
}
