package com.myschool.dto;

import com.myschool.domain.Sms;
import com.myschool.domain.enumerations.DeliveryStatus;
import lombok.Data;

import java.util.List;


/**
 * Created by medilox on 3/10/17.
 */
@Data
public class SmsDto {

    private Long id;
    private String createdDate;
    private String scheduledDate;
    private String sender;
    private String message;
    private String deliveryStatus;
    private String messageId;
    //private Long userId;
    //private String user;
    private String recipient;
    private List<String> recipients;
    private String badge;
    private Integer nbPages;

    public SmsDto createDTO(Sms sms) {
        SmsDto smsDto = new SmsDto();

        if(sms != null){
            smsDto.setId(sms.getId());
            smsDto.setCreatedDate(sms.getCreatedDate());
            smsDto.setScheduledDate(sms.getScheduledDate());
            smsDto.setSender(sms.getSender());
            smsDto.setMessage(sms.getMessage());
            smsDto.setMessageId(sms.getMessageId());
            smsDto.setDeliveryStatus(String.valueOf(sms.getDeliveryStatus()));
            smsDto.setRecipient(sms.getRecipient());

            /*if(sms.getUser() != null){
                smsDto.setUserId(sms.getUser().getId());
                smsDto.setUser(sms.getUser().getLogin());
            }*/

            if(sms.getDeliveryStatus() != null){
                if(sms.getDeliveryStatus().equals(DeliveryStatus.DELIVRD))
                    smsDto.setBadge("success");
                if(sms.getDeliveryStatus().equals(DeliveryStatus.PENDING))
                    smsDto.setBadge("primary");
                if(sms.getDeliveryStatus().equals(DeliveryStatus.EXPIRED))
                    smsDto.setBadge("danger");
                if(sms.getDeliveryStatus().equals(DeliveryStatus.UNDELIV))
                    smsDto.setBadge("danger");
            }
        }
        return smsDto;
    }
}
