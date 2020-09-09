package com.myschool.service;

import com.myschool.domain.*;
import com.myschool.domain.enumerations.TypeEtablissement;
import com.myschool.dto.*;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Annee.
 */
@Service
@Transactional
public class AnneeService {

    private final Logger log = LoggerFactory.getLogger(AnneeService.class);

    @Autowired
    private AnneeRepository anneeRepository;

    @Autowired
    private PromoService promoService;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private TrimestreRepository trimestreRepository;

    @Autowired
    private TrimestreService trimestreService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BulletinRepository bulletinRepository;

    @Autowired
    private CarteScolaireRepository carteScolaireRepository;

    @Value("${dir.myschool}")
    private String FOLDER;

    @Value("${dir.cards}")
    private String FOLDER_CARDS;

    /**
     * Save a annee.
     *
     * @param anneeDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<AnneeDto> save(AnneeDto anneeDto) {
        log.debug("Request to save Annee : {}", anneeDto);

        Annee annee = new Annee();

        annee.setId(anneeDto.getId());
        annee.setDebut(anneeDto.getDebut());
        annee.setFin(anneeDto.getFin());
        annee.setActive(anneeDto.getActive());
        annee.setTypeEtablissement(TypeEtablissement.Secondaire);

        Annee result = anneeRepository.save(annee);
        if (result != null){
            List<Trimestre> trimestres = trimestreRepository.findByAnneeActiveTrue();
            for(Trimestre trimestre: trimestres){
                TrimestreDto trimestreDto = new TrimestreDto();
                trimestreDto.setName(trimestre.getName());
                trimestreDto.setAnneeId(result.getId());
                trimestreDto.setRank(trimestre.getRank());

                //add trimestre sequences
                ArrayList<SequenceDto> sequenceDtos = new ArrayList<>();
                for(Sequence sequence : trimestre.getSequences()){
                    sequenceDtos.add(new SequenceDto().createDTO(sequence));
                }
                trimestreDto.setSequences(sequenceDtos);
                trimestreService.save(trimestreDto);
            }

            //create year promos for each existing classe
            List<Promo> promos = promoRepository.findByAnneeActiveTrue();
            for (Promo promo : promos ){
                PromoDto promoDto = new PromoDto();
                promoDto.setClasse(promo.getClasse());
                promoDto.setTeacherPrincipal(promo.getTeacherPrincipal());
                promoDto.setAnneeId(result.getId());

                //add promo groupes
                ArrayList<GroupeDto> groupeDtos = new ArrayList<>();
                for(Groupe groupe : promo.getGroupes()){
                    groupeDtos.add(new GroupeDto().createDTO(groupe));
                }
                promoDto.setGroupes(groupeDtos);

                //add promo matieres
                ArrayList<MatiereDto> matiereDtos = new ArrayList<>();
                for(Matiere matiere : promo.getMatieres()){
                    matiereDtos.add(new MatiereDto().createDTO(matiere));
                }
                promoDto.setMatieres(matiereDtos);

                promoService.save(promoDto);
            }
        }
        return new ResponseEntity<AnneeDto>(new AnneeDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<AnneeDto> update(AnneeDto anneeDto, MultipartFile logo, MultipartFile cachet) throws IOException {
        log.debug("Request to save Annee : {}", anneeDto);

        Annee annee = anneeRepository.findOne(anneeDto.getId());

        annee.setId(anneeDto.getId());
        annee.setDebut(anneeDto.getDebut());
        annee.setFin(anneeDto.getFin());
        annee.setActive(anneeDto.getActive());

        annee.setNomEtablissement(anneeDto.getNomEtablissement());
        annee.setNameEtablissement(anneeDto.getNameEtablissement());
        annee.setSlogan(anneeDto.getSlogan());
        annee.setPays(anneeDto.getPays());
        annee.setRegion(anneeDto.getRegion());
        annee.setDepartement(anneeDto.getDepartement());
        annee.setVille(anneeDto.getVille());
        annee.setAdresse(anneeDto.getAdresse());
        annee.setPhone(anneeDto.getPhone());
        annee.setSenderId(anneeDto.getSenderId());
        annee.setTypeEtablissement(TypeEtablissement.fromValue(anneeDto.getTypeEtablissement()));

        if(anneeDto.getBulletinId() != null){
            Bulletin bulletin = bulletinRepository.getOne(anneeDto.getBulletinId());
            annee.setBulletin(bulletin);
        }

        if(anneeDto.getCarteScolaireId() != null){
            CarteScolaire carteScolaire = carteScolaireRepository.getOne(anneeDto.getCarteScolaireId());
            annee.setCarteScolaire(carteScolaire);
        }

        Annee result = anneeRepository.save(annee);

        if (!Files.exists(Paths.get(FOLDER))) {
            File annees = new File(FOLDER);
            if(! annees.mkdir()) {
                return new ResponseEntity(new CustomErrorType("Unable to create folder ${dir.myschool}"), HttpStatus.CONFLICT);
            }
        }

        if (!Files.exists(Paths.get(FOLDER + result.getId()))) {
            File anneeFolder = new File(FOLDER + result.getId());
            if (!anneeFolder.mkdir()) {
                return new ResponseEntity(new CustomErrorType("Unable to create folder for this annee"), HttpStatus.CONFLICT);
            }
        }

        if(logo != null){
            if(!logo.isEmpty())
                logo.transferTo(new File(FOLDER + result.getId() + "/logo.png"));
        }
        if(cachet != null){
            if(!cachet.isEmpty())
                cachet.transferTo(new File(FOLDER + result.getId() + "/cachet.png"));
        }
        anneeRepository.save(result);
        return new ResponseEntity<AnneeDto>(new AnneeDto().createDTO(result), HttpStatus.CREATED);
    }


    /**
     *  Get all the annees.
     *
     *  @return the list of entities
     */

    @Transactional(readOnly = true)
    public List<AnneeDto> findAll() {
        log.debug("Request to get all Annees");
        List<Annee> annees = anneeRepository.findAll();
        List<AnneeDto> anneeDtos = new ArrayList<>();
        for(Annee annee: annees){
            AnneeDto anneeDto = new AnneeDto().createDTO(annee);
            //anneeDto.setNbTeachers(teacherRepository.findByAnnee(annee.getId()).size());
            anneeDtos.add(anneeDto);
        }
        return anneeDtos;
    }


    /**
     *  Get one annee by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AnneeDto findOne(Long id) {
        log.debug("Request to get Annee : {}", id);
        Annee annee = anneeRepository.findOne(id);
        return new AnneeDto().createDTO(annee);
    }

    @Transactional(readOnly = true)
    public AnneeDto findActive() {
        Annee annee = anneeRepository.findActive();
        AnneeDto result = new AnneeDto().createDTO(annee);
        /*if(result != null){
            List<Teacher> teachers = teacherRepository.findByAnnee(result.getId());
            result.setNbTeachers(teachers.size());
        }*/
        return result;
    }

    public void setActive(Long id) {
        log.debug("Request to set active Annee : {}", id);
        Annee annee = anneeRepository.findOne(id);
        Annee anneeActive = anneeRepository.findActive();
        if(Optional.ofNullable(annee).isPresent()){
            if(anneeActive != null)
                anneeActive.setActive(false);
            annee.setActive(true);
            anneeRepository.save(annee);
        }
    }

    public void updateCarteScolaire(Long anneeId, Long studentCardId) {
        log.debug("Request to update student card : {}", anneeId);
        Annee annee = anneeRepository.findOne(anneeId);
        if(Optional.ofNullable(annee).isPresent()){
            CarteScolaire carteScolaire = carteScolaireRepository.findOne(studentCardId);
            annee.setCarteScolaire(carteScolaire);
            anneeRepository.save(annee);

            //replace carte scolaire recto verso in annee folder
            try {
                Path path = Paths.get(FOLDER  + "/" + annee.getId()  + "/" + "recto.png");
                Files.deleteIfExists(path);
                File source = new File(FOLDER_CARDS  + "recto_" + carteScolaire.getId() + ".png");
                File dest = new File(FOLDER  + "/" + annee.getId()  + "/" + "recto.png");
                Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Path path = Paths.get(FOLDER  + "/" + annee.getId()  + "/" + "verso.png");
                Files.deleteIfExists(path);
                File source = new File(FOLDER_CARDS  + "verso_" + carteScolaire.getId() + ".png");
                File dest = new File(FOLDER  + "/" + annee.getId()  + "/" + "verso.png");
                Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    /**
     *  Delete the  annee by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Annee : {}", id);
        Annee annee = anneeRepository.findOne(id);
        if(Optional.ofNullable(annee).isPresent()){
            anneeRepository.delete(id);
        }
    }

    public byte[] getImage(Long id, String name) throws IOException {
        Annee annee = anneeRepository.findOne(id);

        if (annee == null) {
            return null;
        }

        File f = new File(FOLDER + "/" + annee.getId() + "/" + name + ".png");
        if(f.exists() && !f.isDirectory()) {
            return IOUtils.toByteArray(new FileInputStream(f));
        }
        return null;
    }

}
