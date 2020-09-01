package com.myschool.service;

import com.myschool.domain.*;
import com.myschool.domain.enumerations.TypeCarte;
import com.myschool.dto.CarteScolaireDto;
import com.myschool.repository.*;
import com.myschool.utils.CustomErrorType;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing CarteScolaire.
 */
@Service
@Transactional
public class CarteScolaireService {

    private final Logger log = LoggerFactory.getLogger(CarteScolaireService.class);

    @Autowired
    private CarteScolaireRepository carteScolaireRepository;

    @Autowired
    private AnneeRepository anneeRepository;


    @Value("${dir.cards}")
    private String FOLDER;



    /*public CarteScolaire addNewCarteScolaire(String name) {
        CarteScolaire result = carteScolaireRepository.save(new CarteScolaire(name));
        return result;
    }*/

    /**
     * Save a carteScolaire.
     *
     * @param carteScolaireDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<?> save(CarteScolaireDto carteScolaireDto, MultipartFile recto, MultipartFile verso) {
        log.debug("Request to save CarteScolaire : {}", carteScolaireDto);

        CarteScolaire carteScolaire = new CarteScolaire();
        carteScolaire.setName(carteScolaireDto.getName());
        carteScolaire.setTypeCarte(TypeCarte.fromValue(carteScolaireDto.getTypeCarte()));

        CarteScolaire result = carteScolaireRepository.save(carteScolaire);
        if(result != null){
            if (!Files.exists(Paths.get(FOLDER))) {
                File folder = new File(FOLDER);
                if(! folder.mkdirs()) {
                    return new ResponseEntity(new CustomErrorType("Unable to create folder ${dir.cards}"), HttpStatus.CONFLICT);
                }
            }

            if(recto != null){
                if(!recto.isEmpty()){
                    try {
                        recto.transferTo(new File(FOLDER + "recto_" + carteScolaire.getId() + ".png"));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        return new ResponseEntity(new CustomErrorType("Error while saving carteScolaire image"), HttpStatus.NO_CONTENT);
                    }
                }
            }

            if(verso != null){
                if(!verso.isEmpty()){
                    try {
                        verso.transferTo(new File(FOLDER + "verso_" + carteScolaire.getId() + ".png"));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        return new ResponseEntity(new CustomErrorType("Error while saving carteScolaire image"), HttpStatus.NO_CONTENT);
                    }
                }
            }
        }
        return new ResponseEntity<CarteScolaireDto>(new CarteScolaireDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<CarteScolaireDto> update(CarteScolaireDto carteScolaireDto, MultipartFile recto, MultipartFile verso) {
        log.debug("Request to save CarteScolaire : {}", carteScolaireDto);

        CarteScolaire carteScolaire = carteScolaireRepository.findOne(carteScolaireDto.getId());
        carteScolaire.setName(carteScolaireDto.getName());
        carteScolaire.setTypeCarte(TypeCarte.fromValue(carteScolaireDto.getTypeCarte()));

        CarteScolaire result = carteScolaireRepository.save(carteScolaire);

        if(result != null){
            if (!Files.exists(Paths.get(FOLDER))) {
                File folder = new File(FOLDER);
                if(! folder.mkdirs()) {
                    return new ResponseEntity(new CustomErrorType("Unable to create folder ${dir.cards}"), HttpStatus.CONFLICT);
                }
            }
            if(recto != null){
                if(!recto.isEmpty()){
                    try {
                        recto.transferTo(new File(FOLDER + "recto_" + carteScolaire.getId() + ".png"));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        return new ResponseEntity(new CustomErrorType("Error while saving carteScolaire image"), HttpStatus.NO_CONTENT);
                    }
                }
            }

            if(verso != null){
                if(!verso.isEmpty()){
                    try {
                        verso.transferTo(new File(FOLDER + "verso_" + carteScolaire.getId() + ".png"));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        return new ResponseEntity(new CustomErrorType("Error while saving carteScolaire image"), HttpStatus.NO_CONTENT);
                    }
                }
            }
        }
        return new ResponseEntity<CarteScolaireDto>(new CarteScolaireDto().createDTO(result), HttpStatus.CREATED);
    }


    /**
     *  Get all the carteScolaires.
     *
     *  @return the list of entities
     * @param anneeId
     */

    @Transactional(readOnly = true)
    public List<CarteScolaireDto> findAll(Long anneeId) {
        log.debug("Request to get all CarteScolaires");
        Annee annee = anneeRepository.getOne(anneeId);

        List<CarteScolaire> carteScolaires = carteScolaireRepository.findAll();

        List<CarteScolaireDto> carteScolaireDtos = new ArrayList<>();
        for(CarteScolaire cs: carteScolaires){
            CarteScolaireDto carteScolaireDto = new  CarteScolaireDto().createDTO(cs);
            if(annee.getCarteScolaire() != null){
                if(annee.getCarteScolaire().getId() == cs.getId())
                    carteScolaireDto.setSelected(true);
            }
            carteScolaireDtos.add(carteScolaireDto);
        }
        return carteScolaireDtos;
    }


    /**
     *  Get one carteScolaire by id.
     *
     *  @param carteScolaireId the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CarteScolaireDto findOne(Long carteScolaireId) {
        log.debug("Request to get CarteScolaire : {}");
        CarteScolaire carteScolaire = carteScolaireRepository.findOne(carteScolaireId);
        return new CarteScolaireDto().createDTO(carteScolaire);
    }
    

    public byte[] getImage(Long carteScolaireId, String name) throws IOException {

        File f = new File(FOLDER  + name + "_" + carteScolaireId + ".png");
        if(f.exists() && !f.isDirectory()) {
            return IOUtils.toByteArray(new FileInputStream(f));
        }
        return null;
    }
}
