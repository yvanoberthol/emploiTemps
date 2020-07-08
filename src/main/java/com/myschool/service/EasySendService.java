/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myschool.service;

import com.myschool.domain.Sms;
import com.myschool.domain.User;
import com.myschool.dto.SmsDto;
import com.myschool.repository.SmsRepository;
import com.myschool.repository.UserRepository;
import com.myschool.utils.CustomErrorType;
import com.myschool.utils.Partition;
import com.myschool.utils.SSLUtil;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class EasySendService {

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${easySend.username}")
    private String USERNAME;

    @Value("${easySend.password}")
    private String PASSWORD;

    // Server constants
    public static final String IP = "127.0.0.1";
    public static final String SERVER  = "https://www.easysendsms.com/sms/bulksms-api/";


    public ResponseEntity send(SmsDto smsDto) throws KeyManagementException, NoSuchAlgorithmException, UnsupportedEncodingException {

        SSLUtil.turnOffSslChecking();
        //User user = userRepository.findOne(smsDto.getUserId());
        Boolean error = false;

        Partition<String> partition = Partition.ofSize(smsDto.getRecipients(), 30);
        for(List<String> recipients: partition){
            StringBuilder s = new StringBuilder();
            s.append(SERVER).append("bulksms-api?").append("username=").append(URLEncoder.encode(USERNAME, "UTF-8"))
                    .append("&password=").append(URLEncoder.encode(PASSWORD, "UTF-8"))
                    .append("&from=").append(URLEncoder.encode(smsDto.getSender(), "UTF-8"))
                    .append("&to=").append(URLEncoder.encode(String.join(",", prepend(recipients, "237")), "UTF-8"))
                    .append("&text=").append(URLEncoder.encode(smsDto.getMessage(), "UTF-8"))
                    .append("&type=").append(0);
            String url = s.toString();
            //System.out.println(url);

            try {
                try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
                    String response = org.apache.commons.io.IOUtils.toString(buffer);
                    //System.out.println(response);
                    //response = "OK: 5fb46866-16e4-4394-ad2d-86296cc86a9d";
                    //response = "OK: b45d4b6d-e3e9-4ac6-87ad-efe77a7b879a,OK: 641363d3-dfbd-4e4e-a3ef-5f9ec1737cc4";

                    List<String> items = Arrays.asList(response.split("\\s*,\\s*"));
                    //System.out.println(items);
                    for(int i=0; i<items.size(); i++){
                        if(items.get(i).startsWith("OK")){
                            Sms sms = new Sms();
                            //set created date;
                            String pattern = "yyyy-MM-dd HH:mm";
                            LocalDateTime date = new LocalDateTime(DateTimeZone.forOffsetHours(1));
                            sms.setCreatedDate(date.toString(pattern));

                            sms.setId(smsDto.getId());
                            sms.setSender(smsDto.getSender());
                            sms.setMessage(smsDto.getMessage());
                            sms.setRecipient(recipients.get(i));
                            sms.setMessageId(items.get(i).split(":")[1].trim());
                            //sms.setUser(user);
                            //user.setSolde(user.getSolde() - smsDto.getNbPages());
                            //System.out.println(sms);
                            smsRepository.save(sms);
                        }
                        else{
                            error = true;
                            /*
                            if (items.get(i).startsWith("1004"))
                                return new ResponseEntity(new CustomErrorType("Le contenu du message est invalide"), HttpStatus.BAD_REQUEST);

                            if (items.get(i).startsWith("1005"))
                                return new ResponseEntity(new CustomErrorType("Le numéro du destinataire est invalide"), HttpStatus.BAD_REQUEST);

                            if (items.get(i).startsWith("1006"))
                                return new ResponseEntity(new CustomErrorType("L'emetteur est invalide"), HttpStatus.BAD_REQUEST);

                            if (items.get(i).startsWith("1002") ||items.get(i).startsWith("1007") || items.get(i).startsWith("1008") || items.get(i).startsWith("1009"))
                                return new ResponseEntity(new CustomErrorType("Une erreur est survenue, veuillez réessayer dans quelques instants"), HttpStatus.BAD_REQUEST);
                            */
                        }
                    }
                    if(error == false)
                        return new ResponseEntity(HttpStatus.OK);
                    else
                        return new ResponseEntity(new CustomErrorType("Une erreur est survenue, veuillez réessayer dans quelques instants"), HttpStatus.BAD_REQUEST);

                }
            }
            catch (MalformedURLException e) {
                System.out.println(e);
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }
        SSLUtil.turnOnSslChecking();
        return null;
    }

    public List<String> prepend(List<String> input, String prepend) {
        List<String> output = new ArrayList<>();
        for (int index = 0; index < input.size(); index++) {
            output.add("" + prepend + input.get(index));
        }
        return output;
    }

    /**
    * Pings a HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in
    * the 200-399 range.
    * @param url The HTTP URL to be pinged.
    * @param timeout The timeout in millis for both the connection timeout and the response read timeout. Note that
    * the total timeout is effectively two times the given timeout.
    * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
    * given timeout, otherwise <code>false</code>.
    */
   public static boolean ping(String url, int timeout) {
       // Otherwise an exception may be thrown on invalid SSL certificates:
       url = url.replaceFirst("^https", "http");

       try {
           HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
           connection.setConnectTimeout(timeout);
           connection.setReadTimeout(timeout);
           connection.setRequestMethod("HEAD");
           int responseCode = connection.getResponseCode();
           //return (200 <= responseCode && responseCode <= 399);
           return (responseCode == 400);
       } catch (IOException exception) {
           return false;
       }
   }
}
