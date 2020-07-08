package com.myschool.rest;

import com.myschool.domain.Sms;
import com.myschool.domain.enumerations.DeliveryStatus;
import com.myschool.dto.SmsDto;
import com.myschool.repository.SmsRepository;
import com.myschool.service.EasySendService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by medilox on 2/22/17.
 */
@RestController
public class EasySendResource {

    @Autowired
    private EasySendService easySendService;

    @Autowired
    private SmsRepository smsRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/api/send")
    public void send(SmsDto smsDto) throws JSONException, NoSuchAlgorithmException, KeyManagementException, UnsupportedEncodingException {
        //System.out.println(coordinates);
        easySendService.send(smsDto);
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.HEAD}, value = "/public/dlr")
    public ResponseEntity<?> deliveryReport(HttpServletRequest request) {

        Enumeration<String> parameterNames = request.getParameterNames();
        List<String> parameterNames2 = new ArrayList<String>(request.getParameterMap().keySet());
        System.out.println(parameterNames2);

        String messageid = null;
        String status = null;

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if(paramValues != null){
                if(paramValues.length != 0){
                    if(paramName.equals("messageid") || paramName.equals("msgid")){
                        messageid = paramValues[0];
                        //System.out.println(messageid);

                    }
                    if(paramName.equals("status")){
                        status = paramValues[0];
                        //System.out.println(status);
                    }
                }
            }
        }

        if(messageid != null && status != null){
            Sms sms = smsRepository.findByMessageId(messageid);
            if(sms != null){
                //System.out.println(sms);
                sms.setDeliveryStatus(DeliveryStatus.fromValue(status));
                /*if(status.equals("DELIVRD"))
                    sms.getUser().setSolde(sms.getUser().getSolde() - 1);*/
                smsRepository.save(sms);
            }
        }

        return new ResponseEntity<SmsDto>(HttpStatus.OK);
    }
}
