package com.myschool.service;

import com.myschool.domain.*;
import com.myschool.domain.enumerations.Sexe;
import com.myschool.domain.enumerations.Statut;
import com.myschool.domain.enumerations.TypeEtablissement;
import com.myschool.dto.*;
import com.myschool.repository.*;
import com.myschool.utils.MapUtil;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service Implementation for managing Student.
 */
@Service
@Transactional
public class StudentService {

    private final Logger log = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private StudentNoteRepository studentNoteRepository;

    @Autowired
    private StudentNoteService studentNoteService;

    @Autowired
    private SequenceRepository sequenceRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private GroupeRepository groupeRepository;

    @Autowired
    private TrimestreRepository trimestreRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Save a student.
     *
     * @param studentDto the entity to save
     * @return the persisted entity
     */
    public Student save(StudentDto studentDto) {
        log.debug("Request to save Student : {}", studentDto);

        Student student = new Student();
        student.setId(studentDto.getId());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());

        //set created date;
        String pattern = "yyyy-MM-dd";
        LocalDate date = new LocalDate();
        student.setCreatedDate(date.toString(pattern));

        student.setSexe(Sexe.fromValue(studentDto.getSexe()));
        student.setStatut(Statut.fromValue(studentDto.getStatut()));
        student.setDateNaissance(studentDto.getDateNaissance());
        student.setLieuNaissance(studentDto.getLieuNaissance());
        student.setNationalite(studentDto.getNationalite());
        student.setMatricule(studentDto.getMatricule());

        student.setFatherName(studentDto.getFatherName());
        student.setFatherPhone(studentDto.getFatherPhone());
        student.setFatherProfession(studentDto.getFatherProfession());
        student.setMotherName(studentDto.getMotherName());
        student.setMotherPhone(studentDto.getMotherPhone());
        student.setMotherProfession(studentDto.getMotherProfession());
        student.setOtherInfos(studentDto.getOtherInfos());

        return studentRepository.save(student);
    }

    public StudentDto update(StudentDto studentDto) {
        log.debug("Request to save Student : {}", studentDto);

        Student student = studentRepository.findOne(studentDto.getId());

        student.setId(studentDto.getId());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());

        student.setSexe(Sexe.fromValue(studentDto.getSexe()));
        student.setStatut(Statut.fromValue(studentDto.getStatut()));
        student.setDateNaissance(studentDto.getDateNaissance());
        student.setLieuNaissance(studentDto.getLieuNaissance());
        student.setNationalite(studentDto.getNationalite());
        student.setMatricule(studentDto.getMatricule());

        student.setFatherName(studentDto.getFatherName());
        student.setFatherPhone(studentDto.getFatherPhone());
        student.setFatherProfession(studentDto.getFatherProfession());
        student.setMotherName(studentDto.getMotherName());
        student.setMotherPhone(studentDto.getMotherPhone());
        student.setMotherProfession(studentDto.getMotherProfession());
        student.setOtherInfos(studentDto.getOtherInfos());

        Student result = studentRepository.save(student);
        return new StudentDto().createDTO(result);
    }

    public StudentNoteDto saveNote(StudentNoteDto studentNoteDto) {
        StudentNote studentNote = studentNoteRepository
                .findByInscriptionStudentIdAndInscriptionPromoIdAndSequenceIdAndMatiereId(
                        studentNoteDto.getStudentId(),
                        studentNoteDto.getPromoId(),
                        studentNoteDto.getSequenceId(),
                        studentNoteDto.getMatiereId());

        if(studentNote == null) studentNote = new StudentNote();

        studentNote.setNote(studentNoteDto.getNote());
        if(studentNoteDto.getPromoId() != null){
            Promo promo = promoRepository.findOne(studentNoteDto.getPromoId());
            if(promo != null){
                if(studentNoteDto.getStudentId() != null){
                    Inscription inscription = inscriptionRepository
                            .findByStudentIdAndAnneeId(studentNoteDto.getStudentId(), promo.getAnnee().getId());
                    studentNote.setInscription(inscription);
                }
            }
        }
        if(studentNoteDto.getSequenceId() != null){
            Sequence sequence = sequenceRepository.findOne(studentNoteDto.getSequenceId());
            studentNote.setSequence(sequence);
        }
        if(studentNoteDto.getMatiereId() != null){
            Matiere matiere = matiereRepository.findOne(studentNoteDto.getMatiereId());
            studentNote.setMatiere(matiere);
        }
        return new StudentNoteDto().createDTO(studentNoteRepository.save(studentNote));
    }

    public void deleteNote(StudentNoteDto studentNoteDto) {
        StudentNote studentNote = studentNoteRepository
                .findByInscriptionStudentIdAndInscriptionPromoIdAndSequenceIdAndMatiereId(
                        studentNoteDto.getStudentId(),
                        studentNoteDto.getPromoId(),
                        studentNoteDto.getSequenceId(),
                        studentNoteDto.getMatiereId());
        if(studentNote != null)
            studentNoteRepository.delete(studentNote.getId());
    }


    /**
     *  Get all the students.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<StudentDto> findAll(){
        log.debug("Request to get all Students");
        List<Student> students = studentRepository.findAll();
        List<StudentDto> studentDtos = new ArrayList<>();

        for (Student student : students)
            studentDtos.add(new StudentDto().createDTO(student));

        //return new ResponseEntity<List<StudentDto>>(studentDtos, HttpStatus.OK);
        return studentDtos;
    }

    public Page<StudentDto> findByAnnee(Integer page, Integer size, String sortBy, String direction, Long anneeId, String studentName, Long promoId) {
        log.debug("Request to get all Students");

        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Student> students;
        if(promoId == 0)
            students = studentRepository.findByAnnee(anneeId,"%"+studentName+"%", pageable);
        else
            students = studentRepository.findByAnneeAndPromoId(anneeId, promoId, "%"+studentName+"%", pageable);

        Page<StudentDto> studentDtos = students.map(student -> {
            StudentDto studentDto = new StudentDto().createDTO(student);

            Inscription inscription = inscriptionRepository.findByStudentIdAndAnneeId(student.getId(), anneeId);
            if(inscription != null){
                if(inscription.getPromo() != null){
                    if(inscription.getPromo().getClasse() != null){
                        studentDto.setClasse(inscription.getPromo().getClasse());
                    }
                }
                if (inscription.getReglements() != null){
                    double mtPaye = 0;
                    for(Reglement reglement: inscription.getReglements()){
                        mtPaye += reglement.getAmount();
                    }
                    studentDto.setMontantPaye(mtPaye);
                    studentDto.setSolde(mtPaye - inscription.getPromo().getMontantScolarite());
                }
            }
            return studentDto;
        });
        return studentDtos;
    }

    public List<StudentDto> findByPromo( Long promoId, String studentName) {
        log.debug("Request to get all Students");

        List<Student> students = studentRepository.findByPromoId(promoId, "%"+studentName+"%");

        List<StudentDto> studentDtos = new ArrayList<>();
        for(Student student: students){
            studentDtos.add(new StudentDto().createDTO(student));
        }
        return studentDtos;
    }

    public List<StudentWithNotesDto> findByPromoWithNotes(String studentName, Long promoId, Long noteId) {
        List<Student> students = studentRepository.findByPromoId(promoId, "%"+studentName+"%");
        Promo promo = promoRepository.getOne(promoId);
        List<StudentWithNotesDto> studentWithNoteDtos = new ArrayList<>();
        for( Student student : students) {
            StudentWithNotesDto studentWithNotesDto = new StudentWithNotesDto().createDTO(student);
            Inscription inscription = inscriptionRepository.findByInscriptionPK(new InscriptionPK(student.getId(), promoId));
            if(inscription != null){
                if (inscription.getStudentNotes() != null){
                    Map<Long, List<StudentNoteDto>> groupedByTrimestre = groupByTrimestre(inscription.getStudentNotes(), noteId);
                    studentWithNotesDto.setNotes(computeMoyennes(groupedByTrimestre, promo.getAnnee().getTypeEtablissement()));
                    studentWithNotesDto.setFinalNote(computeFinalNote(studentWithNotesDto));
                }
            }
            studentWithNoteDtos.add(studentWithNotesDto);
        }
        return studentWithNoteDtos;
    }

    private Double computeFinalNote(StudentWithNotesDto studentWithNotesDto) {
        double sum = 0;
        for (Double note : studentWithNotesDto.getNotes().values()) {
            sum += note;
        }
        return Math.round((sum / studentWithNotesDto.getNotes().values().size()) * 100) / 100.0;
    }

    public Map<Long, Double> computeMoyennes(Map<Long, List<StudentNoteDto>> myMap, TypeEtablissement typeEtablissement) {
        Map<Long, Double> mapResult = new TreeMap<>();
        for (Map.Entry<Long, List<StudentNoteDto>> entry : myMap.entrySet()) {
            mapResult.put(entry.getKey(), computeMoyenne(entry.getValue(), typeEtablissement));
        }
        return mapResult;
    }

    public Map<Long, List<StudentNoteDto>> groupByTrimestre(List<StudentNote> list) {
        Map<Long, List<StudentNoteDto>> map = new TreeMap<Long, List<StudentNoteDto>>();
        for (StudentNote studentNote : list) {
            Long trimestreId = studentNote.getSequence().getTrimestre().getId();
            List<StudentNoteDto> group = map.get(trimestreId);
            if (group == null) {
                group = new ArrayList();
                map.put(trimestreId, group);
            }
            group.add(new StudentNoteDto().createDTO(studentNote));
        }
        return map;
    }

    public Map<Long, List<StudentNoteDto>> groupByTrimestre(List<StudentNote> list, Long trimestreId) {
        Map<Long, List<StudentNoteDto>> map = new TreeMap<Long, List<StudentNoteDto>>();
        for (StudentNote studentNote : list) {
            Long id = studentNote.getSequence().getTrimestre().getId();
            if(studentNote.getSequence().getTrimestre().getId() == trimestreId || trimestreId == 0){
                List<StudentNoteDto> group = map.get(id);
                if (group == null) {
                    group = new ArrayList();
                    map.put(id, group);
                }
                group.add(new StudentNoteDto().createDTO(studentNote));
            }
        }
        return map;
    }

    public List<LigneGroupeDto> groupByGroupe(List<StudentNoteDto> notes, Inscription inscription, Trimestre trimestre) {

        Map<Long, List<StudentNoteDto>> map = new TreeMap<Long, List<StudentNoteDto>>();

        for (StudentNoteDto studentNoteDto : notes) {
            Long groupeId = studentNoteDto.getGroupeId();
            if(groupeId != null){
                List<StudentNoteDto> group = map.get(groupeId);
                if (group == null) {
                    group = new ArrayList();
                    map.put(groupeId, group);
                }
                group.add(studentNoteDto);
            }
        }

        List<LigneGroupeDto> ligneGroupeDtos = new ArrayList<>();
        if(map.entrySet().isEmpty()){
            LigneGroupeDto ligneGroupeDto = new LigneGroupeDto();
            ligneGroupeDto.setGroupe("Groupe 1");
            ligneGroupeDto.setMoyenne(computeMoyenne(notes, trimestre.getAnnee().getTypeEtablissement()));
            ligneGroupeDto.setMatieres(groupByMatiere(inscription, trimestre, notes));
            ligneGroupeDtos.add(ligneGroupeDto);
        }
        else{
            for (Map.Entry<Long, List<StudentNoteDto>> entry : map.entrySet()) {
                LigneGroupeDto ligneGroupeDto = new LigneGroupeDto();

                Groupe groupe = groupeRepository.findOne(entry.getKey());
                if(groupe != null){
                    ligneGroupeDto.setGroupe(groupe.getName());
                    ligneGroupeDto.setRank(groupe.getRank());
                    ligneGroupeDto.setMoyenne(computeMoyenne(entry.getValue(), trimestre.getAnnee().getTypeEtablissement()));
                    ligneGroupeDto.setMoyenneGenerale(getMoyenneGeneraleByTrimestreAndGroupe(inscription, trimestre, groupe));
                    ligneGroupeDto.setRang(getRangByInscriptionAndTrimestreAndGroupe(inscription, trimestre, groupe));
                    ligneGroupeDto.setTotalCoef(computeTotalCoef(entry.getValue()) / trimestre.getSequences().size());
                    ligneGroupeDto.setTotalNotes(computeTotalNotes(entry.getValue(), trimestre.getAnnee().getTypeEtablissement()) / trimestre.getSequences().size());
                    ligneGroupeDto.setMatieres(groupByMatiere(inscription, trimestre, entry.getValue()));
                    ligneGroupeDto.setSequences(groupBySequence(inscription, entry.getValue()));
                    ligneGroupeDtos.add(ligneGroupeDto);
                }
            }
        }
        ligneGroupeDtos.sort(new GroupComparator());
        return ligneGroupeDtos;
    }

    public class GroupComparator implements Comparator<LigneGroupeDto> {
        @Override
        public int compare(LigneGroupeDto o1, LigneGroupeDto o2) {
            return o1.getRank().compareTo(o2.getRank());
        }
    }

    private List<LigneMatiereDto> groupByMatiere(Inscription inscription, Trimestre trimestre, List<StudentNoteDto> notes) {
        Map<Long, List<StudentNoteDto>> map = new TreeMap<Long, List<StudentNoteDto>>();
        for (StudentNoteDto studentNoteDto : notes) {
            Long matiereId = studentNoteDto.getMatiereId();
            List<StudentNoteDto> group = map.get(matiereId);
            if (group == null) {
                group = new ArrayList();
                map.put(matiereId, group);
            }
            group.add(studentNoteDto);
        }

        List<LigneMatiereDto> ligneMatiereDtos = new ArrayList<>();
        for (Map.Entry<Long, List<StudentNoteDto>> entry : map.entrySet()) {
            LigneMatiereDto ligneMatiereDto = new LigneMatiereDto();

            Matiere matiere = matiereRepository.findOne(entry.getKey());
            if(matiere != null){
                ligneMatiereDto.setMatiere(matiere.getName());
                ligneMatiereDto.setTeacher(matiere.getTeacher());
                ligneMatiereDto.setCoef(matiere.getCoef());
                ligneMatiereDto.setNoteSur(matiere.getNoteSur());
                ligneMatiereDto.setMoyenne(computeMoyenneSimple(entry.getValue()));
                ligneMatiereDto.setRang(getRangByInscriptionAndTrimestreAndMatiere(inscription, trimestre, matiere));
                ligneMatiereDto.setNotes(entry.getValue());

                ligneMatiereDto.setMention(StudentNoteDto.findMention(ligneMatiereDto.getMoyenne()));
                ligneMatiereDtos.add(ligneMatiereDto);
            }
        }
        return ligneMatiereDtos;
    }

    private List<SequenceDto> groupBySequence(Inscription inscription, List<StudentNoteDto> notes) {
        Map<Long, List<StudentNoteDto>> map = new TreeMap<Long, List<StudentNoteDto>>();
        for (StudentNoteDto studentNoteDto : notes) {
            Long sequenceId = studentNoteDto.getSequenceId();
            List<StudentNoteDto> group = map.get(sequenceId);
            if (group == null) {
                group = new ArrayList();
                map.put(sequenceId, group);
            }
            group.add(studentNoteDto);
        }

        List<SequenceDto> sequenceDtos = new ArrayList<>();
        for (Map.Entry<Long, List<StudentNoteDto>> entry : map.entrySet()) {
            SequenceDto sequenceDto = new SequenceDto();
            //System.out.println(entry.getKey() + ", " + entry.getValue());

            Sequence sequence = sequenceRepository.findOne(entry.getKey());
            if(sequence != null){
                sequenceDto.setId(sequence.getId());
                sequenceDto.setName(sequence.getName());
                sequenceDto.setWeight(sequence.getWeight());
                sequenceDto.setTrimestreId(sequence.getTrimestre().getId());
                sequenceDto.setTrimestre(sequence.getTrimestre().getName());
                sequenceDto.setTotalNotes(computeTotalNotes(entry.getValue(), sequence.getTrimestre().getAnnee().getTypeEtablissement()));
                sequenceDto.setMoyenne(computeMoyenne(entry.getValue(), sequence.getTrimestre().getAnnee().getTypeEtablissement()));
                sequenceDto.setRang(getRangByInscriptionAndSequence(inscription, sequence));
                sequenceDto.setMoyenneGenerale(getMoyenneGeneraleBySequence(inscription, sequence));
                sequenceDto.setEffectif(inscription.getPromo().getInscriptions().size());
                sequenceDtos.add(sequenceDto);
            }
        }
        return sequenceDtos;
    }

    /*
    private Double computeMoyenneOk(List<StudentNoteDto> notes) {
        double sum = 0;
        double totalCoef = 0;
        for(StudentNoteDto studentNoteDto : notes){
            sum += ( (studentNoteDto.getNote() * 20 / studentNoteDto.getNoteSur()) * ( studentNoteDto.getCoef() * 20 / studentNoteDto.getNoteSur()) );
            totalCoef += studentNoteDto.getCoef() * 20 / studentNoteDto.getNoteSur();
        }
        return Math.round((sum/totalCoef) * 100) / 100.0;
    }
    */

    private Double computeMoyenne(List<StudentNoteDto> notes, TypeEtablissement typeEtablissement) {
        double sum = 0;
        double totalCoef = 0;
        if(typeEtablissement.equals(TypeEtablissement.Primaire)){
                /*for(StudentNoteDto studentNoteDto : notes){
                    sum += ( (studentNoteDto.getNote() * 20 / studentNoteDto.getNoteSur()) * ( studentNoteDto.getCoef() * 20 / studentNoteDto.getNoteSur()) );
                    totalCoef += studentNoteDto.getCoef() * 20 / studentNoteDto.getNoteSur();
                }
                return Math.round((sum/totalCoef) * 100) / 100.0;*/
            for(StudentNoteDto studentNoteDto : notes){
                sum += ( studentNoteDto.getNote() );
                totalCoef += studentNoteDto.getNoteSur()/20.0;
            }
            return Math.round((sum/totalCoef) * 100) / 100.0;
        }
        else{
            for(StudentNoteDto studentNoteDto : notes){
                sum += ( studentNoteDto.getNote() * (studentNoteDto.getWeight() / 100.0) * studentNoteDto.getCoef());
                totalCoef += studentNoteDto.getCoef() * (studentNoteDto.getWeight() / 100.0);
            }
            return Math.round((sum/totalCoef) * 100) / 100.0;
        }
    }

    /*
    //previous compute moyenne simple without weights taken into account
    private Double computeMoyenneSimple(List<StudentNoteDto> notes) {
        double sum = 0;
        for(StudentNoteDto studentNoteDto : notes){
            sum += ( studentNoteDto.getNote());
        }
        return Math.round((sum/notes.size()) * 100) / 100.0;
    }
    */

    //new function with weigths
    private Double computeMoyenneSimple(List<StudentNoteDto> notes) {
        double moy = 0;
        
        if(notes.size() == 1)
            return notes.get(0).getNote();

        for(StudentNoteDto studentNoteDto : notes){
            moy += ( studentNoteDto.getNote() * studentNoteDto.getWeight() / 100);
        }
        return Math.round(moy * 100) / 100.0;
    }

    private Integer computeTotalNoteSur(List<StudentNoteDto> notes) {
        int sum = 0;
        for(StudentNoteDto studentNoteDto : notes)
            sum += studentNoteDto.getNoteSur();
        return sum;
    }

    private Integer computeTotalCoef(List<StudentNoteDto> notes) {
        int sum = 0;
        for(StudentNoteDto studentNoteDto : notes)
            sum += studentNoteDto.getCoef();
        return sum;
    }

    private Double computeTotalNotes(List<StudentNoteDto> notes, TypeEtablissement typeEtablissement) {
        double sum = 0;
        if(typeEtablissement.equals(TypeEtablissement.Primaire)){
            for(StudentNoteDto studentNoteDto : notes)
                sum += studentNoteDto.getNote();
            return sum;
        }
        else{
            for(StudentNoteDto studentNoteDto : notes)
                sum += (studentNoteDto.getNote() * studentNoteDto.getCoef());
            return sum;
        }
    }

    public Page<StudentWithNotesForMatiereDto> findByPromoWithNotesForMatiereAndNoteId(Integer page, Integer size, String sortBy, String direction, String studentName, Long promoId, Long matiereId, Long trimestreId) {
        log.debug("Request to get all Students By Promo with Notes for Matiere");

        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Student> students = studentRepository.findByPromoId(promoId, "%"+studentName+"%", pageable);

        Page<StudentWithNotesForMatiereDto> studentWithNotesForMatiere = students.map(student -> {
            StudentWithNotesForMatiereDto studentWithNotesForMatiereDto = new StudentWithNotesForMatiereDto().createDTO(student);

            if(matiereId != 0){
                Map<Long, Double> notes = new HashMap<>();

                List<StudentNote> studentNotes = studentNoteRepository
                        .findBySequenceTrimestreIdAndMatiereIdAndInscriptionStudentId(trimestreId, matiereId, student.getId());

                for(StudentNote studentNote: studentNotes){
                    notes.put(studentNote.getSequence().getId(), studentNote.getNote());
                }
                studentWithNotesForMatiereDto.setNotes(notes);
            }
            return studentWithNotesForMatiereDto;
        });
        return studentWithNotesForMatiere;
    }

    public List<StudentDto> findByMc(String mc) {
        List<Student> students = studentRepository.findByMc("%"+mc+"%");
        List<StudentDto> studentDtos = new ArrayList<>();
        for (Student student : students)
            studentDtos.add(new StudentDto().createDTO(student));
        return studentDtos;
    }

    /**
     *  Get one student by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ResponseEntity<StudentDto> findOne(Long id) {
        log.debug("Request to get Student : {}", id);
        Student student = studentRepository.findOne(id);

        StudentDto studentDto = new StudentDto().createDTO(student);
        return Optional.ofNullable(studentDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //same like previous but return StudentDto entity
    public StudentDto findOne(Long id, Long anneeId) {
        log.debug("Request to get Student : {}", id);
        Student student = studentRepository.findOne(id);

        if(student != null){
            StudentDto studentDto = new StudentDto().createDTO(student);
            Inscription inscription = inscriptionRepository.findByStudentIdAndAnneeId(student.getId(), anneeId);
            if(inscription != null){
                if(inscription.getPromo() != null){
                    if(inscription.getPromo().getClasse() != null){
                        studentDto.setClasse(inscription.getPromo().getClasse());
                    }
                }
                if (inscription.getReglements() != null){
                    double mtPaye = 0;
                    for(Reglement reglement: inscription.getReglements()){
                        mtPaye += reglement.getAmount();
                    }
                    studentDto.setMontantPaye(mtPaye);
                    studentDto.setSolde(mtPaye - inscription.getPromo().getMontantScolarite());
                }
                if (inscription.getStudentNotes() != null){
                    Map<Long, List<StudentNoteDto>> groupedByTrimestre = groupByTrimestre(inscription.getStudentNotes());

                    List<LigneTrimestreDto> ligneTrimestreDtos = new ArrayList<>();
                    for (Map.Entry<Long, List<StudentNoteDto>> entry : groupedByTrimestre.entrySet()) {
                        LigneTrimestreDto ligneTrimestreDto = new LigneTrimestreDto();

                        Trimestre trimestre = trimestreRepository.findOne(entry.getKey());
                        if(trimestre != null){
                            ligneTrimestreDto.setTrimestreId(trimestre.getId());
                            ligneTrimestreDto.setTrimestre(trimestre.getName());
                            ligneTrimestreDto.setClasse(inscription.getPromo().getClasse());
                            ligneTrimestreDto.setMoyenne(computeMoyenne(entry.getValue(), trimestre.getAnnee().getTypeEtablissement()));
                            ligneTrimestreDto.setTotalNoteSur( computeTotalNoteSur(entry.getValue()) / trimestre.getSequences().size());
                            ligneTrimestreDto.setTotalCoef( computeTotalCoef(entry.getValue()) / trimestre.getSequences().size());
                            ligneTrimestreDto.setTotalNotes( computeTotalNotes(entry.getValue(), trimestre.getAnnee().getTypeEtablissement()) / trimestre.getSequences().size());
                            ligneTrimestreDto.setGroupes(groupByGroupe(entry.getValue(), inscription, trimestre));
                            //ligneTrimestreDto.setMatieres(groupByMatiere(entry.getValue()));
                            ligneTrimestreDto.setSequences(groupBySequence(inscription, entry.getValue()));
                            ligneTrimestreDto.setRang(getRangByInscriptionAndTrimestre(inscription, trimestre));
                            ligneTrimestreDto.setMoyenneGenerale(getMoyenneGeneraleByTrimestre(inscription, trimestre));
                            ligneTrimestreDto.setHighestMark(getHighestMarkByTrimestre(inscription, trimestre));
                            ligneTrimestreDto.setLowestMark(getLowestMarkByTrimestre(inscription, trimestre));
                            ligneTrimestreDto.setEffectif(inscription.getPromo().getInscriptions().size());
                            ligneTrimestreDtos.add(ligneTrimestreDto);
                        }
                    }
                    studentDto.setNotes(ligneTrimestreDtos);
                }
            }
            //Add inscriptionDtos and reglementDto in studentDto
            ArrayList<MouvementDto> mouvementDtos = new ArrayList<>();
            List<Inscription> inscriptions = inscriptionRepository.findByStudentId(studentDto.getId());
            for(Inscription i : inscriptions){
                mouvementDtos.add(new MouvementDto().createDTO(i));
                for(Reglement reglement: i.getReglements()){
                    mouvementDtos.add(new MouvementDto().createDTO(reglement));
                }
            }
            studentDto.setMouvements(mouvementDtos);
            return studentDto;
        }
        return null;
    }

    private Integer getRangByInscriptionAndTrimestre(Inscription inscription, Trimestre trimestre) {
        Map<Long, Double> moyennes = getMoyennesByPromoAndTrimestre(inscription.getPromo(), trimestre);
        int rang = 0;
        for (Map.Entry<Long, Double> entry : moyennes.entrySet()) {
            rang++;
            if(entry.getKey() == inscription.getStudent().getId()) return rang;
        }
        return null;
    }
    private Integer getRangByInscriptionAndTrimestreAndGroupe(Inscription inscription, Trimestre trimestre, Groupe groupe) {
        Map<Long, Double> moyennes = getMoyennesByPromoAndTrimestreAndGroupe(inscription.getPromo(), trimestre, groupe);
        int rang = 0;
        for (Map.Entry<Long, Double> entry : moyennes.entrySet()) {
            rang++;
            if(entry.getKey() == inscription.getStudent().getId()) return rang;
        }
        return null;
    }


    private Integer getRangByInscriptionAndTrimestreAndMatiere(Inscription inscription, Trimestre trimestre, Matiere matiere) {
        Map<Long, Double> moyennes = getMoyennesByPromoAndTrimestreAndMatiere(inscription.getPromo(), trimestre, matiere);
        int rang = 0;
        for (Map.Entry<Long, Double> entry : moyennes.entrySet()) {
            rang++;
            if(entry.getKey() == inscription.getStudent().getId()) return rang;
        }
        return null;
    }

    private Double getMoyenneGeneraleByTrimestre(Inscription inscription, Trimestre trimestre) {
        double sum = 0;
        for(Inscription i: inscription.getPromo().getInscriptions()){
            sum += computeMoyenne(studentNoteService.findBySequenceTrimestreIdAndInscriptionStudentId(trimestre.getId(), i.getStudent().getId()), trimestre.getAnnee().getTypeEtablissement());
        }
        return Math.round((sum / inscription.getPromo().getInscriptions().size()) * 100) / 100.0;
    }

    private Double getMoyenneGeneraleByTrimestreAndGroupe(Inscription inscription, Trimestre trimestre, Groupe groupe) {
        double sum = 0;
        for(Inscription i: inscription.getPromo().getInscriptions()){
            sum += computeMoyenne(studentNoteService.findBySequenceTrimestreIdAndMatiereGroupeIdAndInscriptionStudentId(trimestre.getId(), groupe.getId(), i.getStudent().getId()), trimestre.getAnnee().getTypeEtablissement());
        }
        return Math.round((sum / inscription.getPromo().getInscriptions().size()) * 100) / 100.0;
    }

    private Double getHighestMarkByTrimestre(Inscription inscription, Trimestre trimestre) {
        double max = 0;
        for(Inscription i: inscription.getPromo().getInscriptions()){
            double moy = computeMoyenne(studentNoteService.findBySequenceTrimestreIdAndInscriptionStudentId(trimestre.getId(), i.getStudent().getId()), trimestre.getAnnee().getTypeEtablissement());
            if(moy > max)
                max = moy;
        }
        return Math.round(max * 100) / 100.0;
    }

    private Double getLowestMarkByTrimestre(Inscription inscription, Trimestre trimestre) {
        double min = 20;
        for(Inscription i: inscription.getPromo().getInscriptions()){
            double moy = computeMoyenne(studentNoteService.findBySequenceTrimestreIdAndInscriptionStudentId(trimestre.getId(), i.getStudent().getId()), trimestre.getAnnee().getTypeEtablissement());
            if(moy < min && moy != 0)
                min = moy;
        }
        return Math.round(min * 100) / 100.0;
    }

    private Double getMoyenneGeneraleBySequence(Inscription inscription, Sequence sequence) {
        double sum = 0;
        for(Inscription i: inscription.getPromo().getInscriptions()){
            sum += computeMoyenne(studentNoteService.findBySequenceIdAndInscriptionStudentId(sequence.getId(), i.getStudent().getId()), sequence.getTrimestre().getAnnee().getTypeEtablissement());
        }
        return Math.round((sum / inscription.getPromo().getInscriptions().size()) * 100) / 100.0;
    }

    private Map<Long, Double> getMoyennesByPromoAndTrimestre(Promo promo, Trimestre trimestre) {
        Map<Long, Double> moyennes = new HashMap<>();
        for(Inscription inscription: promo.getInscriptions()){
            moyennes.put(inscription.getStudent().getId(),
                    computeMoyenne(studentNoteService.findBySequenceTrimestreIdAndInscriptionStudentId(trimestre.getId(), inscription.getStudent().getId()), trimestre.getAnnee().getTypeEtablissement()));
        }
        return MapUtil.sortByValue(moyennes);
    }

    private Map<Long,Double> getMoyennesByPromoAndTrimestreAndGroupe(Promo promo, Trimestre trimestre, Groupe groupe) {
        Map<Long, Double> moyennes = new HashMap<>();
        for(Inscription inscription: promo.getInscriptions()){
            moyennes.put(inscription.getStudent().getId(),
                    computeMoyenne(studentNoteService.findBySequenceTrimestreIdAndMatiereGroupeIdAndInscriptionStudentId(trimestre.getId(), groupe.getId(), inscription.getStudent().getId()), trimestre.getAnnee().getTypeEtablissement()));
        }
        return MapUtil.sortByValue(moyennes);
    }

    private Map<Long,Double> getMoyennesByPromoAndTrimestreAndMatiere(Promo promo, Trimestre trimestre, Matiere matiere) {
        Map<Long, Double> moyennes = new HashMap<>();
        for(Inscription inscription: promo.getInscriptions()){
            moyennes.put(inscription.getStudent().getId(),
                    computeMoyenne(studentNoteService.findBySequenceTrimestreIdAndMatiereIdAndInscriptionStudentId(trimestre.getId(), matiere.getId(), inscription.getStudent().getId()), trimestre.getAnnee().getTypeEtablissement()));
        }
        return MapUtil.sortByValue(moyennes);
    }


    private Integer getRangByInscriptionAndSequence(Inscription inscription, Sequence sequence) {
        Map<Long, Double> moyennes = getMoyennesByPromoAndSequence(inscription.getPromo(), sequence);
        int rang = 0;
        for (Map.Entry<Long, Double> entry : moyennes.entrySet()) {
            rang++;
            if(entry.getKey() == inscription.getStudent().getId()) return rang;
        }
        return null;
    }

    private Map<Long,Double> getMoyennesByPromoAndSequence(Promo promo, Sequence sequence) {
        Map<Long, Double> moyennes = new HashMap<>();
        for(Inscription inscription: promo.getInscriptions()){
            moyennes.put(inscription.getStudent().getId(),
                    computeMoyenne(studentNoteService.findBySequenceIdAndInscriptionStudentId(sequence.getId(), inscription.getStudent().getId()), sequence.getTrimestre().getAnnee().getTypeEtablissement()));
        }
        return MapUtil.sortByValue(moyennes);
    }


    /**
     *  Delete the  student by id.
     *
     *  @param id the id of the entity
     */
    @Secured(value = {"ROLE_ADMIN"})
    public void delete(Long id) {
        log.debug("Request to delete Student : {}", id);
        Student student = studentRepository.findOne(id);
        if(student != null){
            studentRepository.delete(id);
        }
    }

    public List<StudentDto> findAll(Long anneeId, Long promoId) {
        List<StudentDto> studentDtos = new ArrayList<>();
        List<Student> students = studentRepository.findByPromoId(promoId);
        for(Student student: students){
            studentDtos.add(findOne(student.getId(), anneeId));
        }
        return studentDtos;
    }
}


