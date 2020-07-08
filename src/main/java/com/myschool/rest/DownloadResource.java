package com.myschool.rest;

import com.myschool.dto.DownloadRequestDto;
import com.myschool.service.DownloadService;
import com.myschool.utils.CustomErrorType;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by medilox on 2/22/17.
 */
@RestController
public class DownloadResource {

    @Autowired
    private DownloadService downloadService;

    @Value("${dir.myschool}")
    private String MYSCHOOL_FOLDER;

    @GetMapping("/api/bulletin")
    public ResponseEntity<byte[]> getBulletin(@RequestParam(name = "student") Long studentId,
                                              @RequestParam(name = "annee") Long anneId,
                                              @RequestParam(name = "trimestre") Long trimestreId) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        String FILE_PATH = downloadService.createBulletin(studentId, anneId, trimestreId);
        headers.setContentType(MediaType.parseMediaType("application/pdf"));

        Path path = Paths.get(FILE_PATH);
        byte[] data = Files.readAllBytes(path);
        String filename = StringUtils.stripAccents(path.getFileName().toString());
        filename = filename.replace(" ", "_");
        //String filename = path.getFileName().toString();

        headers.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + filename.toLowerCase());
        //headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
        return response;
    }

    @GetMapping("/api/bulletins")
    public ResponseEntity<byte[]> getAllBulletins(@RequestParam(name = "promo") Long promoId,
                                                  @RequestParam(name = "annee") Long anneId,
                                                  @RequestParam(name = "trimestre") Long trimestreId) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        String FILE_PATH = downloadService.createBulletins(promoId, anneId, trimestreId);
        headers.setContentType(MediaType.parseMediaType("application/pdf"));

        Path path = Paths.get(FILE_PATH);
        byte[] data = Files.readAllBytes(path);
        String filename = StringUtils.stripAccents(path.getFileName().toString());
        filename = filename.replace(" ", "_");
        //String filename = path.getFileName().toString();

        headers.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + filename.toLowerCase());
        //headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
        return response;
    }

}

