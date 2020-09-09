package com.myschool.service;

import com.myschool.domain.*;
import com.myschool.dto.GroupeDto;
import com.myschool.dto.MatiereDto;
import com.myschool.dto.PromoDto;
import com.myschool.dto.StudentNoteDto;
import com.myschool.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.*;


/**
 * Service Implementation for managing Promo.
 */
@Service
@Transactional
public class PromoService {

    private final Logger log = LoggerFactory.getLogger(PromoService.class);

    @Autowired
    private AnneeRepository anneeRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Save a promo.
     *
     * @param promoDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<PromoDto> save(PromoDto promoDto) {
        log.debug("Request to save Promo : {}", promoDto);

        Promo promo = new Promo();

        promo.setClasse(promoDto.getClasse());
        promo.setMontantScolarite(promoDto.getMontantScolarite());
        promo.setCapacite(promoDto.getCapacite());
        promo.setProfPrincipal(promoDto.getProfPrincipal());

        Annee annee = anneeRepository.findOne(promoDto.getAnneeId());
        promo.setAnnee(annee);

        Promo result = promoRepository.save(promo);

        if(result != null){
            ArrayList<Matiere> matieres = new ArrayList<>();
            for(MatiereDto matiereDto : promoDto.getMatieres()){
                Matiere matiere = new Matiere();
                matiere.setCoef(matiereDto.getCoef());
                matiere.setName(matiereDto.getName());
                matiere.setNoteSur(matiereDto.getNoteSur());
                matiere.setPromo(result);
                matieres.add(matiere);
            }
            promo.setMatieres(matieres);

            ArrayList<Groupe> groupes = new ArrayList<>();
            for(GroupeDto groupeDto : promoDto.getGroupes()){
                Groupe groupe = new Groupe();
                groupe.setRank(groupeDto.getRank());
                groupe.setName(groupeDto.getName());
                groupe.setPromo(result);
                groupes.add(groupe);
            }
            promo.setGroupes(groupes);
        }
        return new ResponseEntity<PromoDto>(new PromoDto().createDTO(result), HttpStatus.CREATED);
    }

    /**
     * Update a promo.
     *
     * @param promoDto the entity to update
     * @return the persisted entity
     */
    public ResponseEntity<PromoDto> update(PromoDto promoDto) {
        log.debug("Request to update Promo : {}", promoDto);

        //Promo promo = promoRepository.findByAnneeIdAndClasseId(promoDto.getAnneeId(), promoDto.getClasseId());
        Promo promo = promoRepository.findOne(promoDto.getId());
        promo.setClasse(promoDto.getClasse());
        promo.setMontantScolarite(promoDto.getMontantScolarite());
        promo.setCapacite(promoDto.getCapacite());
        promo.setProfPrincipal(promoDto.getProfPrincipal());

        Promo result = promoRepository.save(promo);
        return new ResponseEntity<PromoDto>(new PromoDto().createDTO(result), HttpStatus.CREATED);
    }

    /**
     *  Get one promo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PromoDto findOne(Long id) {
        log.debug("Request to get Promo : {}", id);
        Promo promo = promoRepository.findOne(id);
        return new PromoDto().createDTO(promo);
    }


    public List<PromoDto> findByAnnee(Long anneeId) {
        List<PromoDto> promoDtos = new ArrayList<>();
        List<Promo> promos = promoRepository
                .findByAnneeId(anneeId);

        for (Promo promo : promos)
            promoDtos.add(new PromoDto().createDTO(promo));

        return promoDtos;
    }

    public void delete(Long id) {
        log.debug("Request to delete Promo : {}", id);
        Promo promo = promoRepository.findOne(id);
        if(Optional.ofNullable(promo).isPresent()){
            promoRepository.delete(id);
        }
    }

    public void importNotes(MultipartFile file, Long promoId, Long sequenceId, Long matiereId) throws Exception {
        if(file != null){
            if(!file.isEmpty()){
                InputStream inputStream =  new BufferedInputStream(file.getInputStream());
                process(inputStream, promoId, sequenceId, matiereId);
            }
        }
    }

    public void process(InputStream is, Long promoId, Long sequenceId, Long matiereId) throws Exception {
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        StringBuilder s = new StringBuilder();

        Iterator<Row> iterator = sheet.iterator();
        List<Student> students;

        int rowIndex = 0;
        while (iterator.hasNext()) {

            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();

            StudentNoteDto studentNote = new StudentNoteDto();
            studentNote.setPromoId(promoId);
            studentNote.setSequenceId(sequenceId);
            studentNote.setMatiereId(matiereId);

            if(rowIndex != 0){
                while (cellIterator.hasNext()) {

                    Cell currentCell = cellIterator.next();

                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        s.append((currentCell.getStringCellValue()).trim() + "; ");
                        students = studentRepository.findByMatriculeOrLastNameOrFirstName(promoId, (currentCell.getStringCellValue()).trim());
                        if(!students.isEmpty()){
                            studentNote.setStudentId(students.get(0).getId());
                        }
                    }
                    else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        studentNote.setNote(currentCell.getNumericCellValue());
                    }

                    if(studentNote.getStudentId() != null)
                    studentService.saveNote(studentNote);
                }
            }
            rowIndex++;
            System.out.println("--------------");
            s.append("\n");
        }
        System.out.println(s);
    }

}