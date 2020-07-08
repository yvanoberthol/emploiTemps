package com.myschool.dto;

import com.myschool.domain.SmsTemplate;
import lombok.Data;

/**
 * Created by medilox on 30/09/18.
 */

@Data
public class SmsTemplateDto {

    private Long id;
    private String title;
    private String message;

    public SmsTemplateDto createDTO(SmsTemplate smsTemplate) {
        SmsTemplateDto smsTemplateDto = new SmsTemplateDto();
        if(smsTemplate != null){
            smsTemplateDto.setId(smsTemplate.getId());
            smsTemplateDto.setTitle(smsTemplate.getTitle());
            smsTemplateDto.setMessage(smsTemplate.getMessage());

            return smsTemplateDto;
        }
        return null;
    }

}

