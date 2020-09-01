package com.myschool.service;


import com.myschool.domain.Inscription;
import com.myschool.domain.Promo;
import com.myschool.domain.Student;
import com.myschool.domain.enumerations.TypeCarte;
import com.myschool.domain.enumerations.TypeEtablissement;
import com.myschool.dto.*;
import com.myschool.repository.*;
import com.myschool.security.SecurityUtils;
import com.myschool.utils.Partition;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * Service Implementation for managing Student.
 */
@Service
@Transactional
public class DownloadService {

    private final Logger log = LoggerFactory.getLogger(DownloadService.class);

    @Value("${dir.myschool}")
    private String FOLDER;

    @Autowired
    private StudentService studentService;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private PromoRepository promoRepository;

    public String createNotesImportSample(Long promoId, Long sequenceId) {
        Promo promo = promoRepository.getOne(promoId);
        String TEX_FOLDER = FOLDER + promo.getAnnee().getId() + "/";
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(StringUtils.stripAccents(promo.getClasse().toLowerCase()));

        //sheet.setColumnWidth(2, 30*256);
        //sheet.setColumnWidth(3, 50*256);

        List<Inscription> inscriptions = promo.getInscriptions();
        Collections.sort(inscriptions);

        this.fillSheetInformations(sheet, inscriptions);

        try {
            FileOutputStream outputStream = new FileOutputStream(TEX_FOLDER + StringUtils.stripAccents(promo.getClasse().toLowerCase()) + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
            return TEX_FOLDER + StringUtils.stripAccents(promo.getClasse().toLowerCase()) + ".xlsx";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void fillSheetInformations(XSSFSheet sheet, List<Inscription> inscriptions) {
        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        int colNum = 0;
        row.createCell(colNum++).setCellValue("Matricule");
        row.createCell(colNum++).setCellValue("Noms");
        row.createCell(colNum++).setCellValue("Prénoms");
        row.createCell(colNum++).setCellValue("Notes");

        for (Inscription inscription : inscriptions){
            row = sheet.createRow(rowNum++);
            colNum = 0;

            if(inscription.getStudent().getMatricule() != null){
                row.createCell(colNum++).setCellValue(inscription.getStudent().getMatricule());
            }
            else{
                row.createCell(colNum++).setCellValue("");
            }

            if(inscription.getStudent().getLastName() != null){
                row.createCell(colNum++).setCellValue(inscription.getStudent().getLastName());
            }
            else{
                row.createCell(colNum++).setCellValue("");
            }

            if(inscription.getStudent().getFirstName() != null){
                row.createCell(colNum++).setCellValue(inscription.getStudent().getFirstName());
            }
            else{
                row.createCell(colNum++).setCellValue("");
            }

            row.createCell(colNum++).setCellValue("");
        }
    }



    class AfficheurFlux implements Runnable {

        private final InputStream inputStream;
        private String output;

        AfficheurFlux(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        private BufferedReader getBufferedReader(InputStream is) {
            return new BufferedReader(new InputStreamReader(is));
        }

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }

        @Override
        public void run() {
            BufferedReader br = getBufferedReader(inputStream);
            String ligne = "";
            this.output = "";
            try {
                while ((ligne = br.readLine()) != null) {
                    //System.out.println(ligne);
                    this.output += ligne;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String createBulletin(Long studentId, Long anneeId, Long trimestreId) throws IOException {
        StudentDto student = studentService.findOne(studentId, anneeId);
        Inscription inscription = inscriptionRepository.findByStudentIdAndAnneeId(studentId, anneeId);

        LigneTrimestreDto ligneTrimestreDto = student.getNotes().stream()
                .filter(ligneTrimestre -> ligneTrimestre.getTrimestreId() == trimestreId)
                .findAny()
                .orElse(null);

        //System.out.println(ligneTrimestreDto);
        String TEX_FOLDER = FOLDER + anneeId + "/";
        String makefile = createMakeFile();
        String texTemplate = createBulletinTex(ligneTrimestreDto, inscription);

        try {
            FileUtils.writeStringToFile(new File(TEX_FOLDER + "Makefile"), makefile);
            FileUtils.writeStringToFile(new File(TEX_FOLDER + StringUtils.stripAccents(student.getLastName().toLowerCase()) + ".tex"), texTemplate);

            String[] paramsArr = {"make"};
            List<String> paramsList = Arrays.asList(paramsArr);

            ProcessBuilder pb = new ProcessBuilder(paramsList);
            pb.directory(new File(TEX_FOLDER));

            try {
                Process p = pb.start();
                AfficheurFlux fluxSortie = new AfficheurFlux(p.getInputStream());
                AfficheurFlux fluxErreur = new AfficheurFlux(p.getErrorStream());
                new Thread(fluxSortie).start();
                new Thread(fluxErreur).start();
                p.waitFor();

                return TEX_FOLDER + StringUtils.stripAccents(student.getLastName().toLowerCase()) + ".pdf";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createBulletins(Long promoId, Long anneeId, Long trimestreId) throws IOException {
        List<StudentDto> students = studentService.findAll(anneeId, promoId);
        List<LigneTrimestreDto> ligneTrimestreDtos = new ArrayList<>();
        Map<Inscription, LigneTrimestreDto> map = new HashMap();

        for(StudentDto studentDto: students){
            LigneTrimestreDto ligneTrimestreDto = studentDto.getNotes().stream()
                    .filter(ligneTrimestre -> ligneTrimestre.getTrimestreId() == trimestreId)
                    .findAny()
                    .orElse(null);
            Inscription inscription = inscriptionRepository.findByStudentIdAndAnneeId(studentDto.getId(), anneeId);
            map.put(inscription, ligneTrimestreDto);
            //ligneTrimestreDtos.add(ligneTrimestreDto);
        }

        String TEX_FOLDER = FOLDER + anneeId + "/";
        String makefile = createMakeFile();
        String texTemplate = createBulletinsTex(map);

        try {
            FileUtils.writeStringToFile(new File(TEX_FOLDER + "Makefile"), makefile);
            FileUtils.writeStringToFile(new File(TEX_FOLDER +  "all.tex"), texTemplate);

            String[] paramsArr = {"make"};
            List<String> paramsList = Arrays.asList(paramsArr);

            ProcessBuilder pb = new ProcessBuilder(paramsList);
            pb.directory(new File(TEX_FOLDER));

            try {
                Process p = pb.start();
                AfficheurFlux fluxSortie = new AfficheurFlux(p.getInputStream());
                AfficheurFlux fluxErreur = new AfficheurFlux(p.getErrorStream());
                new Thread(fluxSortie).start();
                new Thread(fluxErreur).start();
                p.waitFor();

                return TEX_FOLDER + "all.pdf";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createStudentCards(Long promoId) throws IOException {
        Promo promo = promoRepository.getOne(promoId);

        String TEX_FOLDER = FOLDER + promo.getAnnee().getId() + "/";
        String makefile = createMakeFile();
        String texTemplate = createStudentCardsTex(promo);

        try {
            String filename = StringUtils.stripAccents(promo.getClasse().toLowerCase()).replace(" ", "_");
            FileUtils.writeStringToFile(new File(TEX_FOLDER + "Makefile"), makefile);
            FileUtils.writeStringToFile(new File(TEX_FOLDER +  filename + ".tex"), texTemplate);

            String[] paramsArr = {"make"};
            List<String> paramsList = Arrays.asList(paramsArr);

            ProcessBuilder pb = new ProcessBuilder(paramsList);
            pb.directory(new File(TEX_FOLDER));

            try {
                Process p = pb.start();
                AfficheurFlux fluxSortie = new AfficheurFlux(p.getInputStream());
                AfficheurFlux fluxErreur = new AfficheurFlux(p.getErrorStream());
                new Thread(fluxSortie).start();
                new Thread(fluxErreur).start();
                p.waitFor();

                return TEX_FOLDER +  filename +".pdf";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createListeDefinitive(Long promoId) throws IOException {
        Promo promo = promoRepository.getOne(promoId);

        String TEX_FOLDER = FOLDER + promo.getAnnee().getId() + "/";
        String makefile = createMakeFile();
        String texTemplate = createListeDefinitiveTex(promo);

        try {
            String filename = StringUtils.stripAccents(promo.getClasse().toLowerCase()).replace(" ", "_");
            FileUtils.writeStringToFile(new File(TEX_FOLDER + "Makefile"), makefile);
            FileUtils.writeStringToFile(new File(TEX_FOLDER +  filename + ".tex"), texTemplate);

            String[] paramsArr = {"make"};
            List<String> paramsList = Arrays.asList(paramsArr);

            ProcessBuilder pb = new ProcessBuilder(paramsList);
            pb.directory(new File(TEX_FOLDER));

            try {
                Process p = pb.start();
                AfficheurFlux fluxSortie = new AfficheurFlux(p.getInputStream());
                AfficheurFlux fluxErreur = new AfficheurFlux(p.getErrorStream());
                new Thread(fluxSortie).start();
                new Thread(fluxErreur).start();
                p.waitFor();

                return TEX_FOLDER +  filename +".pdf";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createMakeFile() {
        StringBuilder s = new StringBuilder();
        s.append("all :\n")
                .append("\trm -rf *.log *.aux *.pdf\n")
                //to time for watermark to render correctly
                .append("\tpdflatex *.tex\n" )
                .append("\tpdflatex *.tex\n" )
                .append("\trm -rf *.log *.aux *.tex *.epl");
        return s.toString();
    }

    private String createStudentCardsTex(Promo promo) throws IOException {
        if(promo.getAnnee().getCarteScolaire() != null){
            TypeCarte type = promo.getAnnee().getCarteScolaire().getTypeCarte();
            if(type != null){
                if(type.equals(TypeCarte.A6)){
                    return createStudentCardsTexA6(promo);
                }
                else{
                    return createStudentCardsTexBC(promo);
                }
            }
        }
        return createStudentCardsTexA6(promo);
    }

    private String createStudentCardsTexA6(Promo promo) {
        StringBuilder s = new StringBuilder();
        s.append("\\documentclass[8pt]{extarticle}\n" +
                "\\usepackage[dvips]{graphicx}\n" +
                "\\usepackage[francais]{babel}" +
                "\\usepackage[utf8]{inputenc}\n" +
                "\\usepackage[T1]{fontenc}\n" +
                "\\usepackage{lmodern}" +
                "\\usepackage{xcolor}\n" +
                "\\usepackage{tikz}\n" +
                "\\usepackage[a4paper, landscape, margin=0mm]{geometry}" +
                "\\pagestyle{empty}\n" +
                "\n" +
                "\\renewcommand\\familydefault{\\sfdefault}" +
                "\\newlength{\\cardw}\n" +
                "\\newlength{\\cardh}" +
                "\\setlength{\\cardw}{148mm}\n" +
                "\\setlength{\\cardh}{103mm}" +
                "\\setlength{\\parindent}{0pt}" +
                "\n" +
                "\\begin{document}");

        File recto = new File(FOLDER + promo.getAnnee().getId() + "/" + "recto.png");
        File verso = new File(FOLDER + promo.getAnnee().getId() + "/" + "verso.png");

        List<Inscription> inscriptions = promo.getInscriptions();
        Collections.sort(inscriptions);
        Partition<Inscription> partition = Partition.ofSize(inscriptions, 4);

        for(int i=0; i<partition.size(); i++){

            List<Inscription> group = partition.get(i);

            s.append("\\begin{tikzpicture}");
            //draw grid
            //s.append("\\foreach \\i in {0,1,2} \\draw[very thin, gray,dashed] (0,\\i*\\cardh) -- (2*\\cardw,\\i*\\cardh);");
            //  s.append("\\foreach \\j in {0,1,2} \\draw[very thin, gray,dashed] (\\j*\\cardw,0) -- (\\j*\\cardw, 2*\\cardh);");

            //grid content
            int cpt = 0;
            for(int x=1; x>=0; x--){
                for(int y=0; y<2; y++){
                    if(cpt < group.size()){

                        if(recto.exists() && !recto.isDirectory()) {
                            s.append("\\node[opacity=1] at (")
                                    .append(y).append("*\\cardw+.5\\cardw, ")
                                    .append(x)
                                    .append("*\\cardh+0.5\\cardh) {\\includegraphics[width=\\cardw ,height=1\\cardh]{")
                                    .append("recto.png").append("}};\n");
                        }



                        File logo  = new File(FOLDER + group.get(cpt).getPromo().getAnnee().getId() + "/" + "logo.png");
                        if(logo.exists() && !logo.isDirectory()) {
                            s.append("\\node at (").append(y).append("*\\cardw+0.25\\cardw, ")
                                    .append(x).append("*\\cardh+0.5\\cardh) {\\includegraphics[width=0.3\\cardw]{logo}};");
                        }

                        s.append(" \\node[black!25!gray , right] at (")
                                .append(y)
                                .append("*\\cardw+0.5\\cardw, ").append(x).append("*\\cardh+0.97\\cardh)  {\\tiny \\textbf{\\MakeUppercase{République du Cameroun}}};");

                        s.append(" \\node[black!25!gray, right] at (")
                                .append(y)
                                .append("*\\cardw+0.5\\cardw, ").append(x).append("*\\cardh+0.94\\cardh)  {\\tiny{Paix - Travail - Patrie}};");

                        s.append("\\node[black!25!gray, left] at (")
                                .append(y)
                                .append("*\\cardw+1\\cardw, ").append(x).append("*\\cardh+0.97\\cardh)  {\\tiny \\textbf{\\MakeUppercase{Republic of Cameroon}}};\n" +
                                "\\node[black!25!gray, left] at (")
                                .append(y)
                                .append("*\\cardw+1\\cardw, ").append(x).append("*\\cardh+0.94\\cardh)  {\\tiny{Peace - Work - Fatherland}};");

                        if(group.get(cpt).getPromo().getAnnee().getTypeEtablissement().equals(TypeEtablissement.Secondaire))
                            s.append("\\node[black!25!gray] at (")
                                    .append(y)
                                    .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.87\\cardh)  {\\small \\textbf{\\MakeUppercase{Ministère des enseignements secondaires}}};\n" +
                                    "   \\node[black!25!gray] at (")
                                    .append(y)
                                    .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.84\\cardh)  {\\small \\MakeUppercase{Ministry of secondary education}};");
                        else
                            s.append("\\node[black!25!gray] at (")
                                    .append(y)
                                    .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.87\\cardh)  {\\small \\textbf{\\MakeUppercase{Ministère de l'Education de Base}}};\n" +
                                    "   \\node[black!25!gray] at (")
                                    .append(y)
                                    .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.84\\cardh)  {\\small \\MakeUppercase{Ministry of Basic Education}};");

                        s.append("\\draw[line width=0.2mm] (")
                                .append(y)
                                .append("*\\cardw+0.66\\cardw, ").append(x).append("*\\cardh+0.81\\cardh) -- (")
                                .append(y)
                                .append("*\\cardw+0.83\\cardw, ").append(x).append("*\\cardh+0.81\\cardh);");
                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.78\\cardh) {\\small \\textbf{\\MakeUppercase{Délégation Régionale du ").append(group.get(cpt).getPromo().getAnnee().getRegion() != null ? group.get(cpt).getPromo().getAnnee().getRegion() : "").append("}}};\n" +
                                "   \\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.75\\cardh)  {\\small \\MakeUppercase{").append(group.get(cpt).getPromo().getAnnee().getRegion() != null ? group.get(cpt).getPromo().getAnnee().getRegion() : "").append(" Regional Delegation}};");
                        s.append("\\draw[line width=0.2mm] (")
                                .append(y)
                                .append("*\\cardw+0.66\\cardw, ").append(x).append("*\\cardh+0.72\\cardh) -- (")
                                .append(y)
                                .append("*\\cardw+0.83\\cardw, ").append(x).append("*\\cardh+0.72\\cardh);");
                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.69\\cardh)  {\\small \\textbf{\\MakeUppercase{Délégation Départementale du ").append(group.get(cpt).getPromo().getAnnee().getDepartement() != null ? group.get(cpt).getPromo().getAnnee().getDepartement() : "").append("}}};\n" +
                                "   \\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.66\\cardh)  {\\small \\MakeUppercase{Divisional Delegation of ").append(group.get(cpt).getPromo().getAnnee().getDepartement() != null ? group.get(cpt).getPromo().getAnnee().getDepartement() : "").append("}};");

                        logo  = new File(FOLDER + group.get(cpt).getPromo().getAnnee().getId() + "/" + "logo.png");
                        if(logo.exists() && !logo.isDirectory()) {
                            s.append("\\node at (")
                                    .append(y)
                                    .append("*\\cardw+.75\\cardw, ").append(x).append("*\\cardh+0.53\\cardh) {\\includegraphics[width=0.10\\cardw]{logo}};");
                        }

                        //info etablissement
                        s.append(" \\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.40\\cardh)  {\\small \\textbf{\\MakeUppercase{").append(group.get(cpt).getPromo().getAnnee().getNomEtablissement() != null ? group.get(cpt).getPromo().getAnnee().getNomEtablissement().toUpperCase() : "").append("}}};");

                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.37\\cardh) {\\small {\\MakeUppercase{").append(group.get(cpt).getPromo().getAnnee().getNameEtablissement() != null ? group.get(cpt).getPromo().getAnnee().getNameEtablissement().toUpperCase() : "").append("}}};");
                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.34\\cardh)  {\\small \\textbf{\\MakeUppercase{").append(group.get(cpt).getPromo().getAnnee().getAdresse()!= null? group.get(cpt).getPromo().getAnnee().getAdresse(): "").append(" - Tel : ")
                                .append(group.get(cpt).getPromo().getAnnee().getPhone() != null ? group.get(cpt).getPromo().getAnnee().getPhone() : "").append("}}};");
                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.26\\cardh)  { \\large \\textbf{\\MakeUppercase{Carte d'Identité Scolaire}}};");
                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.22\\cardh)  { \\large {\\MakeUppercase{Student Identity Card}}};");
                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.145\\cardh) {\\small \\textbf{\\MakeUppercase{N\\textsuperscript{o}}........}};");
                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.07\\cardh)  {\\small \\textbf{\\MakeUppercase{Année Scolaire : ").append(group.get(cpt).getPromo().getAnnee().toString()).append("}}};");
                        s.append("\\node[black!25!gray , left] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.045\\cardh)  {\\small {\\MakeUppercase{School Year}}};");
                    }
                    cpt++;

                }
            }

            s.append("\\end{tikzpicture}\n");

            // verso
            s.append("\\newpage");

            s.append("\\begin{tikzpicture}");
            //draw grid
            //s.append("\\foreach \\i in {0,1,2} \\draw[very thin, gray,dashed] (0,\\i*\\cardh) -- (2*\\cardw,\\i*\\cardh);");
            //s.append("\\foreach \\j in {0,1,2} \\draw[very thin, gray,dashed] (\\j*\\cardw,0) -- (\\j*\\cardw, 2*\\cardh);");

            //grid content
            cpt = 0;
            for(int x=1; x>=0; x--){
                for(int y=0; y<2; y++){
                    if(cpt < group.size()){

                        File logo  = new File(FOLDER + group.get(cpt).getPromo().getAnnee().getId() + "/" + "logo.png");
                        if(logo.exists() && !logo.isDirectory()) {
                            s.append("\\node[opacity=0.1] at (")
                                    .append(y)
                                    .append("*\\cardw+.5\\cardw, ").append(x).append("*\\cardh+0.5\\cardh) {\\includegraphics[width=0.5\\cardw]{logo}};\n");
                        }


                        String lastName = group.get(cpt).getStudent().getLastName().substring(0, Math.min(group.get(cpt).getStudent().getLastName().length(), 22));
                        String firstName = group.get(cpt).getStudent().getFirstName() != null ? StringUtils.capitalize(group.get(cpt).getStudent().getFirstName().substring(0, Math.min(group.get(cpt).getStudent().getFirstName().length(), 22))) : "";
                        String fatherName = group.get(cpt).getStudent().getFatherName() != null ? StringUtils.capitalize(group.get(cpt).getStudent().getFatherName().substring(0, Math.min(group.get(cpt).getStudent().getFatherName().length(), 22))) : "";
                        String motherName = group.get(cpt).getStudent().getMotherName() != null ? StringUtils.capitalize(group.get(cpt).getStudent().getMotherName().substring(0, Math.min(group.get(cpt).getStudent().getMotherName().length(), 22))) : "";


                        s.append("\\node[right] at (")
                                .append(y)
                                .append("*\\cardw, ").append(x).append("*\\cardh+0.5\\cardh) {\n" +
                                "   {\n" +
                                "        \t\\begin{tabular}{@{}r@{\\hspace{4mm}}l}\n" +
                                "            \t\n" +
                                "            \t\\rule{0pt}{5ex}\n" +
                                "            \t{\\color{gray} \\small \\textbf{Noms}} &  \\textbf{").append(lastName.toUpperCase()).append("}\\\\\n" +
                                "            \t{\\color{gray} \\tiny \\textbf{Name}} &  \\\\\n" +
                                "            \t\n" +
                                "            \t\\rule{0pt}{3ex}\n" +
                                "            \t{\\color{gray} \\small \\textbf{Prénoms}} & ")
                                .append(firstName).append("\\\\\n" +
                                "            \t{\\color{gray} \\tiny \\textbf{First Name}} &  \\\\\n" +
                                "            \t\n" +
                                "            \t\\rule{0pt}{3ex}\n" +
                                "            \t{\\color{gray} \\small \\textbf{Né(e) le}} &  ").append(group.get(cpt).getStudent().getDateNaissanceFormatted() != null ? group.get(cpt).getStudent().getDateNaissanceFormatted() : "").append("\\\\\n" +
                                "            \t{\\color{gray} \\tiny \\textbf{Born on}} &  \\\\\n" +
                                "            \t\n" +
                                "            \t\\rule{0pt}{3ex}\n" +
                                "            \t{\\color{gray} \\small \\textbf{\\MakeUppercase{à}}} & ")
                                .append(group.get(cpt).getStudent().getLieuNaissance() != null ? group.get(cpt).getStudent().getLieuNaissance() : "").append("\\\\\n" +
                                "            \t{\\color{gray} \\tiny \\textbf{At}} &  \\\\\n" +
                                "            \t\n" +
                                "            \t\\rule{0pt}{3ex}\n" +
                                "            \t{\\color{gray} \\small \\textbf{Fils ou Fille de }} & ").append(fatherName).append("\\\\\n" +
                                "            \t{\\color{gray} \\tiny \\textbf{Son or Daughter of}} &  \\\\\n" +
                                "            \t\n" +
                                "            \t\\rule{0pt}{3ex}\n" +
                                "            \t{\\color{gray} \\small \\textbf{Et de}} &  ").append(motherName).append("\\\\\n" +
                                "            \t{\\color{gray} \\tiny \\textbf{And}} &  \\\\\n" +
                                "            \t\n" +
                                "            \t%\\rule{0pt}{3ex}\n" +
                                "            \t%{\\color{gray} \\small \\textbf{Domicile des Parents}} & ").append(group.get(cpt).getStudent().getDomicile() != null ? group.get(cpt).getStudent().getDomicile() : "").append("\\\\\n" +
                                "            \t%{\\color{gray} \\tiny \\textbf{Parent's Residence}} &  \\\\\n" +
                                "            \t\n" +
                                "            \t\\rule{0pt}{3ex}\n" +
                                "            \t{\\color{gray} \\small \\textbf{Contact}} & ").append(group.get(cpt).getStudent().getFatherPhone() != null ? group.get(cpt).getStudent().getFatherPhone() : group.get(cpt).getStudent().getMotherPhone() != null ? group.get(cpt).getStudent().getMotherPhone() : "").append("\\\\\n" +
                                "            \t{\\color{gray} \\tiny \\textbf{Contact}} &  \\\\\n" +
                                "            \t\n" +
                                "            \t\\rule{0pt}{3ex}\n" +
                                "            \t{\\color{gray} \\small \\textbf{Classe}} &  ").append(group.get(cpt).getPromo().getClasse()).append("\\\\\n" +
                                "            \t{\\color{gray} \\tiny \\textbf{Form}} &  \\\\\n" +
                                "            \t\n" +
                                "            \t\\rule{0pt}{3ex}\n" +
                                "            \t{\\color{gray} \\small \\textbf{Année Scolaire }} & ").append(group.get(cpt).getPromo().getAnnee().toString()).append("\\\\\n" +
                                "            \t{\\color{gray} \\tiny \\textbf{School Year }} &  \\\\\n" +
                                "            \t\n" +
                                "            \t\\rule{0pt}{3ex}\n" +
                                "            \t{\\color{gray} \\small \\textbf{Matricule}} &  ").append(group.get(cpt).getStudent().getMatricule() != null ? group.get(cpt).getStudent().getMatricule() : "").append("\\\\\n" +
                                "            \t{\\color{gray} \\tiny \\textbf{Registration N\\textsuperscript{o} }} &  \\\\\n" +
                                "            \t\n" +
                                "            \t\\rule{0pt}{5ex}\n" +
                                "            \t{\\color{gray} \\small \\textbf{Signature de l'élève}} &  \\\\\n" +
                                "            \t{\\color{gray} \\tiny \\textbf{Student's Signature}} &  \\\\\n" +
                                "\n" +
                                "            \t\n" +
                                "            \\end{tabular}\n" +
                                "        }\n" +
                                "    };");

                        File f = new File(FOLDER + group.get(cpt).getPromo().getAnnee().getId() + "/" + group.get(cpt).getPromo().getId() + "_" + group.get(cpt).getStudent().getId() + ".png");
                        if(f.exists() && !f.isDirectory()) {
                            s.append("\\node at (")
                                    .append(y)
                                    .append("*\\cardw+0.8\\cardw, ").append(x)
                                    .append("*\\cardh + 0.75\\cardh) {\\includegraphics[width=0.25\\cardw]{")
                                    .append(group.get(cpt).getPromo().getId() + "_" + group.get(cpt).getStudent().getId() + ".png").append("}};");
                        }

                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.45\\cardh)  {\\small \\textbf{").append(group.get(cpt).getPromo().getAnnee().getVille()).append(", le \\today}};");
                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.30\\cardh) {\\small \\textbf {\\MakeUppercase{Le Proviseur}}};");
                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.75\\cardw, ").append(x).append("*\\cardh+0.27\\cardh) {\\small {\\MakeUppercase{The Principal}}};");
                    }
                    cpt++;
                }
            }
            s.append("\\end{tikzpicture}");

            //if ! last page
            if(i < partition.size()-1)
                s.append("\\newpage");
        }
        s.append("\n\n" + "\\end{document}");
        return s.toString();
    }

    private String createStudentCardsTexBC(Promo promo) {
        StringBuilder s = new StringBuilder();
        File f;
        s.append("\\documentclass[10pt]{article}\n" +
                "\\usepackage[dvips]{graphicx}\n" +
                "\\usepackage[utf8]{inputenc}\n" +
                "\\usepackage[T1]{fontenc}\n" +
                "\\usepackage{xcolor}\n" +
                "\\usepackage{tikz}\n" +
                "\\usepackage{geometry}\n" +
                "\\geometry{total={210mm,297mm},hmargin=10mm,vmargin=0mm}\n" +
                "\\pagestyle{empty}\n" +
                "\n" +
                "\\renewcommand\\familydefault{\\sfdefault}" +
                "\\newlength{\\cardw}\n" +
                "\\newlength{\\cardh}" +
                "\\setlength{\\cardw}{85mm}\n" +
                "\\setlength{\\cardh}{55mm}" +
                "\n" +
                "\\begin{document}");

        List<Inscription> inscriptions = promo.getInscriptions();
        Collections.sort(inscriptions);
        Partition<Inscription> partition = Partition.ofSize(inscriptions, 10);

        File recto = new File(FOLDER  + "/" + promo.getAnnee().getId()  + "/" + "recto.png");
        File verso = new File(FOLDER  + "/" + promo.getAnnee().getId()  + "/" + "verso.png");

        for(int i=0; i<partition.size(); i++){
            List<Inscription> group = partition.get(i);
            s.append("\\begin{tikzpicture}");

            //draw grid
            s.append("\\foreach \\i in {0,...,5} \\draw[very thin, gray,dashed] (0,\\i*\\cardh) -- (2*\\cardw,\\i*\\cardh);");
            s.append("\\foreach \\j in {0,1,2} \\draw[very thin, gray,dashed] (\\j*\\cardw,0) -- (\\j*\\cardw,5*\\cardh);");

            //grid content
            int cpt = 0;
            for(int x=4; x>=0; x--){
                for(int y=0; y<2; y++){
                    if(cpt < group.size()){


                        if(recto.exists() && !recto.isDirectory()) {
                            s.append("\\node[opacity=0.6] at (")
                                    .append(y).append("*\\cardw+.5\\cardw, ")
                                    .append(x)
                                    .append("*\\cardh+0.5\\cardh) {\\includegraphics[width=\\cardw ,height=1\\cardh]{")
                                    .append("recto.png").append("}};\n");
                        }

                        s.append("   \\node[black!25!gray , right] at (")
                                .append(y)
                                .append("*\\cardw, ")
                                .append(x).append("*\\cardh+0.97\\cardh)  {\\tiny \\textbf{\\MakeUppercase{République du Cameroun}}};");

                        s.append(
                                "\n" +
                                        "  \\node[black!25!gray, right] at (")
                                .append(y).append("*\\cardw, ")
                                .append(x).append("*\\cardh+0.92\\cardh)  {\\tiny{Paix - Travail - Patrie}};");



                        File logo  = new File(FOLDER + group.get(cpt).getPromo().getAnnee().getId() + "/" + "logo.png");
                        if(logo.exists() && !logo.isDirectory()) {
                            s.append(
                                    "\n" +
                                            "  \\node[black!25!gray] at (")
                                    .append(y).append("*\\cardw+0.5\\cardw, ")
                                    .append(x).append("*\\cardh+0.85\\cardh)  {\\includegraphics[width=0.18\\cardw]{logo}};");
                        }


                        s.append("   \\node[black!25!gray , left] at (")
                                .append(y)
                                .append("*\\cardw+1\\cardw, ")
                                .append(x).append("*\\cardh+0.97\\cardh)  {\\tiny \\textbf{\\MakeUppercase{Republic of Cameroon}}};");

                        s.append(
                                "\n" +
                                        "  \\node[black!25!gray, left] at (")
                                .append(y).append("*\\cardw+1\\cardw, ")
                                .append(x).append("*\\cardh+0.92\\cardh)  {\\tiny{Peace - Work - Fatherland}};");

                        s.append(
                                "\n" +
                                        "  \\node[black!25!gray] at (")
                                .append(y).append("*\\cardw+0.5\\cardw, ")
                                .append(x).append("*\\cardh+0.60\\cardh) { \\tiny \\textbf{\\MakeUppercase{Carte d'Identité Scolaire}}};");

                        s.append(
                                "\n" +
                                        "  \\node[black!25!gray] at (")
                                .append(y).append("*\\cardw+0.5\\cardw, ")
                                .append(x).append("*\\cardh+0.55\\cardh) { \\tiny {\\MakeUppercase{Student Identity Card}}};");

                        s.append(
                                "\n" +
                                        "  \\node[black!25!gray] at (")
                                .append(y).append("*\\cardw+0.5\\cardw, ")
                                .append(x).append("*\\cardh+0.40\\cardh)  {\\Large \\textbf{\\MakeUppercase{").append(group.get(cpt).getPromo().getAnnee().getNomEtablissement()).append("}}};");

                        s.append(
                                "\n" +
                                        "  \\node[black!25!gray] at (")
                                .append(y).append("*\\cardw+0.5\\cardw, ")
                                .append(x).append("*\\cardh+0.30\\cardh)  {\\small {\\MakeUppercase{").append(group.get(cpt).getPromo().getAnnee().getNameEtablissement()).append("}}};");

                        s.append(
                                "\n" +
                                        "  \\node[black!25!gray] at (")
                                .append(y).append("*\\cardw+0.5\\cardw, ")
                                .append(x).append("*\\cardh+0.23\\cardh)  {\\tiny \\textbf{\\MakeUppercase{").append(group.get(cpt).getPromo().getAnnee().getSlogan()).append("}}};");

                        s.append("   \\node[black!25!gray , right] at (")
                                .append(y)
                                .append("*\\cardw+0.62\\cardw, ")
                                .append(x).append("*\\cardh+0.07\\cardh)  {\\tiny \\textbf{\\MakeUppercase{Année Scolaire : ").append(group.get(cpt).getPromo().getAnnee().toString()).append("}}};");

                        s.append("   \\node[black!25!gray , right] at (")
                                .append(y)
                                .append("*\\cardw+0.62\\cardw, ")
                                .append(x).append("*\\cardh+0.04\\cardh)  {\\tiny {\\MakeUppercase{School Year}}};");

                    }
                    cpt++;

                }
            }

            s.append("\\end{tikzpicture}");

            // verso
            s.append("\\newpage");

            s.append("\\begin{tikzpicture}");

            //draw grid
            s.append("\\foreach \\i in {0,...,5} \\draw[very thin, gray,dashed] (0,\\i*\\cardh) -- (2*\\cardw,\\i*\\cardh);");
            s.append("\\foreach \\j in {0,1,2} \\draw[very thin, gray,dashed] (\\j*\\cardw,0) -- (\\j*\\cardw,5*\\cardh);");

            //grid content
            cpt = 0;
            for(int x=4; x>=0; x--){
                for(int y=0; y<2; y++){
                    if(cpt < group.size()){

                        if(verso.exists() && !verso.isDirectory()) {
                            s.append("\\node[opacity=0.6] at (")
                                    .append(y).append("*\\cardw+.5\\cardw, ")
                                    .append(x)
                                    .append("*\\cardh+0.5\\cardh) {\\includegraphics[width=\\cardw ,height=1\\cardh]{")
                                    .append("verso.png").append("}};\n");
                        }

                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.5\\cardw, ")
                                .append(x).append("*\\cardh+0.9\\cardh) {\\small \\textbf{").append(group.get(cpt).getPromo().getAnnee().getNomEtablissement() != null ? group.get(cpt).getPromo().getAnnee().getNomEtablissement().toUpperCase() : "").append("}};");

                        s.append("\\node[black!25!gray] at (")
                                .append(y)
                                .append("*\\cardw+0.5\\cardw, ").append(x).append("*\\cardh+0.82\\cardh) {\\small  ").append("CARTE D'IDENTITE SCOLAIRE : ").append(group.get(cpt).getPromo().getAnnee().toString()).append("};");


                        f = new File(FOLDER + group.get(cpt).getPromo().getAnnee().getId() + "/" + group.get(cpt).getPromo().getId() + "_" + group.get(cpt).getStudent().getId() + ".png");
                        if(f.exists() && !f.isDirectory()) {
                            s.append("\\node at (").append(y).append("*\\cardw+0.2\\cardw, ").append(x)
                                    .append("*\\cardh+0.4\\cardh) {\\includegraphics[width=0.3\\cardw]{")
                                    .append(group.get(cpt).getPromo().getId() + "_" + group.get(cpt).getStudent().getId() + ".png")
                                    .append("}};");
                        }

                        //s.append(" \\node[black!25!gray, right] at (").append(y).append("*\\cardw+0.4\\cardw, ").append(x).append("*\\cardh+0.9\\cardh)  {\\small \\textbf{CARTE D'IDENTITE SCOLAIRE}};");

                        String lastName = group.get(cpt).getStudent().getLastName().substring(0, Math.min(group.get(cpt).getStudent().getLastName().length(), 17));
                        String firstName = group.get(cpt).getStudent().getFirstName() != null ? StringUtils.capitalize(group.get(cpt).getStudent().getFirstName().substring(0, Math.min(group.get(cpt).getStudent().getFirstName().length(), 17))) : "";
                        String fatherName = group.get(cpt).getStudent().getFatherName() != null ? StringUtils.capitalize(group.get(cpt).getStudent().getFatherName().substring(0, Math.min(group.get(cpt).getStudent().getFatherName().length(), 17))) : "";
                        String motherName = group.get(cpt).getStudent().getMotherName() != null ? StringUtils.capitalize(group.get(cpt).getStudent().getMotherName().substring(0, Math.min(group.get(cpt).getStudent().getMotherName().length(), 17))) : "";


                        s.append("\\node[right] at (").append(y).append("*\\cardw+0.35\\cardw, ").append(x).append("*\\cardh+0.4\\cardh) {\n" +
                                "   {\n" +
                                "        \t\\begin{tabular}{@{}r@{\\hspace{2mm}}l}\n" +
                                "            \t{\\color{gray} \\tiny Matricule} & \\small ").append(group.get(cpt).getStudent().getMatricule() != null ? group.get(cpt).getStudent().getMatricule() : "").append("\\\\\n" +
                                "            \t%{\\color{gray} \\tiny Student ID}\\\\\n" +
                                "            \t{\\color{gray} \\tiny Noms} & \\small \\textbf{").append(lastName.toUpperCase()).append("}\\\\\n" +
                                "            \t{\\color{gray} \\tiny Prénoms} & \\small ").append(firstName).append("\\\\\n" +
                                "            \t{\\color{gray} \\tiny Né(e) le} & \\small ").append(group.get(cpt).getStudent().getDateNaissanceFormatted() != null ? group.get(cpt).getStudent().getDateNaissanceFormatted() : "").append("\\\\\n" +
                                "            \t{\\color{gray} \\tiny Classe} & \\small ").append(group.get(cpt).getPromo().getClasse()).append("\\\\\n" +
                                "            \t{\\color{gray} \\tiny Fils/Fille de} & \\small ").append(fatherName).append(" \\\\\n" +
                                "            \t{\\color{gray} \\tiny Et de} & \\small ").append(motherName).append("\\\\\n" +
                                "            \t{\\color{gray}\\tiny \\textbf{Contact} } & \\small ").append(group.get(cpt).getStudent().getFatherPhone() != null ? group.get(cpt).getStudent().getFatherPhone() : group.get(cpt).getStudent().getMotherPhone() != null ? group.get(cpt).getStudent().getMotherPhone() : "").append("\\\\\n" +
                                "            \\end{tabular}\n" +
                                "        }\n" +
                                "    };");

                        File cachet  = new File(FOLDER + group.get(cpt).getPromo().getAnnee().getId() + "/" + "cachet.png");
                        if(cachet.exists() && !cachet.isDirectory()) {
                            s.append("\\node[opacity=0.5] at (").append(y).append("*\\cardw+.3\\cardw, ").append(x).append("*\\cardh+0.18\\cardh) {\\includegraphics[width=0.28\\cardw]{cachet}};");
                        }
                    }
                    cpt++;

                }
            }
            s.append("\\end{tikzpicture}");

            if(i < partition.size()-1)
                s.append("\\newpage");
        }
        s.append("\n\n" + "\\end{document}");
        return s.toString();
    }

    private String createListeDefinitiveTex(Promo promo) throws IOException {
        StringBuilder s = new StringBuilder();
        int cpt = 0;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("ddMMyyyy");
        s.append("\\documentclass[french,11pt]{article}\n" +
                "\\usepackage{babel}\n" +
                "\\usepackage[utf8]{inputenc}\n" +
                "\\usepackage[T1]{fontenc}\n" +
                "\\usepackage[a4paper]{geometry}\n" +
                "\\usepackage{longtable}\n" +
                "\n" +
                "\\geometry{verbose,tmargin=4em,bmargin=4em,lmargin=1.5em,rmargin=1.5em}\n" +
                "\\begin{document}\n");

        s.append("\\begin{center}\n" +

                "\\MakeUppercase{\\textbf{").append(promo.getAnnee().getNomEtablissement() != null? promo.getAnnee().getNomEtablissement() : "").append("}}\\\\\n" +
                "\\vspace{0.2cm}\n" +
                "\\MakeUppercase{\\textbf{Liste définitive Classe : ").append(promo.getClasse()).append(" - Année scolaire : ").append(promo.getAnnee().toString()).append("}}\\\\\n" +
                "\n" +
                "\\vspace{0.5cm}"+

                "\\begin{longtable}{|c|l|l|l|l|l|l|l|}");

        s.append("\\hline \n" +
                "\\textbf{N\\textsuperscript{o}} & \\textbf{Matricule} & \\textbf{Noms} & \\textbf{Prénoms} & \\textbf{Date Naiss} & \\textbf{Lieu}& \\textbf{Sexe} & \\textbf{Statut} \\\\ \n" +
                "\\hline \t");

        List<Inscription> inscriptions = promo.getInscriptions();
        Collections.sort(inscriptions);
        for(Inscription inscription: inscriptions){
            s.append(++cpt).append("\t &");
            s.append(inscription.getStudent().getMatricule() != null ? inscription.getStudent().getMatricule() : "").append("\t &");
            s.append(inscription.getStudent().getLastName() != null ? StringUtils.capitalize(inscription.getStudent().getLastName()) : "").append("\t &");
            s.append(inscription.getStudent().getFirstName() != null ? StringUtils.capitalize(inscription.getStudent().getFirstName()) : "").append("\t &");
            s.append(inscription.getStudent().getDateNaissance() != null ? inscription.getStudent().getDateNaissanceFormatted() : "").append("\t &");
            s.append(inscription.getStudent().getLieuNaissance() != null ? StringUtils.capitalize(inscription.getStudent().getLieuNaissance()) : "").append("\t &");
            s.append(inscription.getStudent().getSexe() != null ? inscription.getStudent().getSexe() : "").append("\t &");
            s.append(inscription.getStudent().getStatut() != null ? inscription.getStudent().getStatut() : "");
            s.append("\\\\ \\hline \t\n");
        }

        s.append("\\end{longtable}\n" +
                "\\end{center}\n" +
                "\\end{document}");
        return s.toString();
    }

    private String createBulletinTex(LigneTrimestreDto ligneTrimestreDto, Inscription inscription) throws IOException {
        StringBuilder s = new StringBuilder();
        s.append("\\documentclass[french,11pt]{article}\n" +
                "\\usepackage{babel}\n" +
                "\\usepackage[utf8]{inputenc}\n" +
                "\\usepackage[T1]{fontenc}\n" +
                "\\usepackage[a4paper]{geometry}\n" +
                "\\usepackage{moreverb}\n" +
                "\\usepackage{graphicx}\n" +
                "\\usepackage{wrapfig}\n" +
                "\\usepackage{fancyhdr}\n" +
                "\\usepackage{lipsum} \n" +
                "\\usepackage[svgnames]{xcolor}\n" +
                "\\usepackage{multirow}\n" +
                "\\usepackage{dcolumn}\n" +
                "\\usepackage{tabularx}\n" +
                "\\usepackage{graphicx}\n" +
                "\\usepackage{transparent}\n" +
                "\\usepackage{enumitem}\n" +
                "\\usepackage{amssymb}\n" +
                "\n" +

                //watermark
                "\\usepackage{eso-pic}\n" +
                "\\AddToShipoutPicture*{\n" +
                "    \\put(0,0){\n" +
                "        \\parbox[b][\\paperheight]{\\paperwidth}{%\n" +
                "            \\vfill\n" +
                "            \\centering\n" +
                "            {\\transparent{0.05}\\includegraphics[width=0.8\\textwidth]{logo}}%\n" +
                "            \\vfill\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +


                "\n" +
                "\n" +
                "%\\geometry{verbose,tmargin=2em,bmargin=2em,lmargin=3em,rmargin=3em}\n" +
                "\\geometry{verbose,tmargin=2em,bmargin=4em,lmargin=2em,rmargin=2em}\n" +
                "\\setlength{\\parindent}{0pt}\n" +
                "\\setlength{\\parskip}{1ex plus 0.5ex minus 0.2ex}\n" +
                "\n" +
                "\\pagestyle{fancy}\n" +
                "\\fancyhead{} % clear all header fields\n" +
                "\\fancyfoot{} % clear all footer fields\n" +
                "\\cfoot{\\hfill \\tiny{gesnotes.com}}\n" +
                "\n" +
                "\\begin{document}\n" +
                "\n" +
                "\\pagenumbering{gobble}\n" +
                "\\renewcommand{\\arraystretch}{1.2}\n");


        s.append("\\begin{minipage}[c]{0.20\\textwidth}\n" +
                "\\includegraphics[width=\\textwidth]{logo.png}\n" +
                "\\end{minipage}\n" +
                "\\hfill\n" +
                "\\begin{minipage}[c]{0.75\\textwidth}\n");

        s.append("\\underline{\\textbf{\\large{").append(inscription.getStudent().getName().toUpperCase()).append("}}} \\\\[0.2cm]\n")
                .append("Matricule/Regist number: \\textbf{").append(inscription.getStudent().getMatricule()).append("}\t\t\t\t\t\t\\hfill")
                .append("\tAnnée scolaire: \\textbf{").append(inscription.getPromo().getAnnee().toString()).append("} \\\\\n")
                .append("\tDate de naissance/Date of birth: \\textbf{").append(inscription.getStudent().getDateNaissanceFormatted()).append("}\t\t\\hfill")
                .append("\tClasse: \\textbf{").append(ligneTrimestreDto.getClasse()).append("}, Effectif: \\textbf{").append(ligneTrimestreDto.getEffectif()).append("} \\\\\n")
                .append("Sexe: \\textbf{").append(inscription.getStudent().getSexe()).append("} \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\\hfill")
                .append("\tProf titulaire: \\textbf{").append(inscription.getPromo().getProfPrincipal()).append("}\\\\\n")
                .append("\\end{minipage}\n" + "\\vspace{0.3cm}");

        s.append(
                "\\begin{center}\n" +
                        "\t%\\begin{tabular}{|L{2cm}|C{4cm}|c|c|c|c|d{1}|}\n" +
                        "\t\\textbf{BULLETIN ").append(ligneTrimestreDto.getTrimestre().toUpperCase()).append("}\\\\\n" +
                        "\t\\vspace{0.2cm}" +
                        "\t\\begin{tabularx}{\\textwidth}{|>{\\centering\\arraybackslash}p{0.5cm}|p{4.5cm}|>{\\centering\\arraybackslash}X|" +
                        ">{\\centering\\arraybackslash}X|>{\\centering\\arraybackslash}X|p{0.8cm}|p{6cm}|}\n" +
                        "\t\t\\hline\t\n");

        s.append("\\textbf{Cf} & \\textbf{Matière/Subject} &");

        for(SequenceDto sequence: ligneTrimestreDto.getSequences()){
            s.append("\\textbf{").append(sequence.getName()).append("}\t&");
        }

        s.append("\\textbf{My/Av} & \\textbf{R/P} & \\textbf{Appréciations/Observations } \\\\\\hline\t");

        for(LigneGroupeDto ligneGroupeDto: ligneTrimestreDto.getGroupes()){
            //System.out.println(ligneGroupeDto);

            for(LigneMatiereDto ligneMatiereDto : ligneGroupeDto.getMatieres()){
                s.append(Math.round(ligneMatiereDto.getCoef())).append("\t&")
                        .append(ligneMatiereDto.getMatiere()).append("\t&");

                for(SequenceDto sequence: ligneTrimestreDto.getSequences()){
                    s.append(getNote(sequence.getId(), ligneMatiereDto)).append("\t&");
                }

                s.append(ligneMatiereDto.getMoyenne()).append("\t&")
                        .append(ligneMatiereDto.getRang()).append("\t&")
                        .append(ligneMatiereDto.getTeacher() + "\\hfill").append("\\scriptsize{").append(ligneMatiereDto.getMention()).append("}\\\\\t\\hline");
            }

            s.append("\t\t\\multicolumn{5}{|l|}{\\textbf{Moy du groupe/Group average:} \\hfill ")
                    .append("\\textbf{").append(ligneGroupeDto.getMoyenne())
                    .append("/20}} & \\multicolumn{2}{c|}{\\multirow{2}{*}{\\textbf{Rang/position :} ")
                    .append(ligneGroupeDto.getRang()).append("/").append(ligneTrimestreDto.getEffectif()).append("} } \\\\ \t\t\\cline{1-5}\n")
                    .append("\t\t\\multicolumn{5}{|l|}{\\textbf{Moy de la classe/Class average:} \\hfill ")
                    .append("\\textbf{").append(ligneGroupeDto.getMoyenneGenerale()).append("/20}} & \\multicolumn{2}{l|}{}\\\\\\hline\t\t\n");
        }

        s.append("\t\t\\multicolumn{4}{|l|}{\\textbf{Moyenne Trimestrielle/Term average :} \\hfill ")
                .append("\\textbf{").append(ligneTrimestreDto.getMoyenne()).append("/20}} & \\multicolumn{3}{|c|}{Plus forte moyenne/Highest mark : \\hfill ")
                .append("\\textbf{").append(ligneTrimestreDto.getHighestMark()).append("/20 }} \\\\ \\hline\n")
                .append("\t\t\\multicolumn{4}{|l|}{\\textbf{Rang Trimestriel :} \\hfill ")
                .append("\\textbf{").append(ligneTrimestreDto.getRang()).append("/").append(ligneTrimestreDto.getEffectif()).append("}")
                .append("} & \\multicolumn{3}{|c|}{Moyenne Générale : \\hfill ")
                .append("\\textbf{").append(ligneTrimestreDto.getMoyenneGenerale()).append("/20}} \\\\ \\hline\n")
                .append("\t\t\\multicolumn{4}{|l|}{} & \\multicolumn{3}{|c|}{Plus faible moyenne/Lowest mark : \\hfill ")
                .append("\\textbf{").append(ligneTrimestreDto.getLowestMark()).append("/20}} \\\\ \n")
                .append("\n\t\t\\hline\t\n")
                .append("\\end{tabularx}\n").append("\\bigskip\n").append("\n\\end{center}\n");


        s.append("\\begin{scriptsize}\n" +
                "\\begin{minipage}[c]{0.60\\textwidth}\n" +
                "\t\\underline{\\textbf{Appréciations académiques/Academic remarks}}\\\\[0.2cm]\n" +
                "\t\\begin{minipage}[t]{0.37\\textwidth}\n" +
                "\t\t\t\\begin{itemize}[label=$\\square$, leftmargin=1cm ,parsep=0cm,itemsep=0.2cm,topsep=0cm, font=\\LARGE]\n" +
                "\t\t\t\t\\item T.Bien/V.Good\n" +
                "\t\t\t\t\\item A peine suffisant\n" +
                "\t\t\t\t\\item Félicitations\n" +
                "\t\t\t\t\\item En baisse\n" +
                "\t\t\t\t\\item Risque d'exclusion\n" +
                "\t\t\t\\end{itemize}\n" +
                "\t\t\\end{minipage}\n" +
                "\t\t\\begin{minipage}[t]{0.35\\textwidth}\n" +
                "\t\t\\begin{itemize}[label=$\\square$, leftmargin=* ,parsep=0cm,itemsep=0.2cm,topsep=0cm, font=\\LARGE]\n" +
                "\t\t\t\\item Assez-bien\n" +
                "\t\t\t\t\\item insuffisant\n" +
                "\t\t\t\t\\item En progrès\n" +
                "\t\t\t\t\\item Risque redoublement\n" +
                "\t\t\t\t\\item Engagement non tenu\n" +
                "\t\t\\end{itemize}\n" +
                "\t\\end{minipage}\n" +
                "\\begin{minipage}[t]{0.25\\textwidth}\n" +
                "\t\t\\begin{itemize}[label=$\\square$, leftmargin=* ,parsep=0cm,itemsep=0.2cm,topsep=0cm, font=\\LARGE]\n" +
                "\t\t\t\\item Passable\n" +
                "\t\t\t\t\\item Faible\n" +
                "\t\t\\end{itemize}\n" +
                "\t\\end{minipage}\n" +
                "\\end{minipage}\n" +
                "\\hfill\n" +
                "\\begin{minipage}[c]{0.40\\textwidth}\n" +
                "\\underline{\\textbf{Appréciations disciplinaires/Discipline appreciations}}\\\\[0.2cm]\n" +
                "\t\\begin{minipage}[c]{0.45\\textwidth}\n" +
                "\t\t\t\\begin{itemize}[label=$\\square$, leftmargin=* ,parsep=0cm,itemsep=0.2cm,topsep=0cm, font=\\LARGE]\n" +
                "\t\t\t\t\\item Attention conduite\n" +
                "\t\t\t\t\\item Tab d'hon de cond\n" +
                "\t\t\t\t\\item Engagement\n" +
                "\t\t\t\t\\item Exclusion\n" +
                "\t\t\t\t\\item Retards\n" +
                "\t\t\t\\end{itemize}\n" +
                "\t\t\\end{minipage}\n" +
                "\t\t\\begin{minipage}[c]{0.45\\textwidth}\n" +
                "\t\t\\begin{itemize}[label=$\\square$, leftmargin=* ,parsep=0cm,itemsep=0.2cm,topsep=0cm, font=\\LARGE]\n" +
                "\t\t\t\\item Avertis conduite\n" +
                "\t\t\t\t\\item Colle\n" +
                "\t\t\t\t\\item Engagement non tenu\n" +
                "\t\t\t\t\\item Engagement cond tenu\n" +
                "\t\t\t\t\\item Absences\n" +
                "\t\t\\end{itemize}\n" +
                "\t\\end{minipage}\n" +
                "\\end{minipage}\n" +
                "\n" +
                "\\end{scriptsize}\n");

        s.append("\\vspace{0.5cm}" +
                "\n" +
                "\\begin{tabularx}{\\textwidth}{|X|X|X|X|}\n" +
                "\t\t\\hline\t\n" +
                "\t\t %&  &  &  \\\\\n" +
                "  \t%\\hline \n" +
                "\t\t\\multicolumn{2}{|>{\\centering\\arraybackslash}X|}{\\textbf{Observations}} & \\multicolumn{2}{|>{\\centering\\arraybackslash}X|}{\\textbf{Visa du chef d'établissement} } \\\\\\hline\n" +
                "\t\t\\multicolumn{2}{|X|}{} & \\multicolumn{2}{|X|}{ } \\\\\n" +
                "  \t\\multicolumn{2}{|X|}{} & \\multicolumn{2}{|X|}{ } \\\\\n" +
                "\t\t\\multicolumn{2}{|X|}{} & \\multicolumn{2}{|X|}{ } \\\\\n" +
                "\t\t\\multicolumn{2}{|X|}{} & \\multicolumn{2}{|X|}{ } \\\\\\hline\n" +
                "\\end{tabularx}\n" +
                "\n" +
                "\n" +
                "\n" +
                "\\end{document}");

        return s.toString();
    }

    private String createBulletinsTex(Map<Inscription, LigneTrimestreDto> map) throws IOException {
        StringBuilder s = new StringBuilder();
        s.append("\\documentclass[french,11pt]{article}\n" +
                "\\usepackage{babel}\n" +
                "\\usepackage[utf8]{inputenc}\n" +
                "\\usepackage[T1]{fontenc}\n" +
                "\\usepackage[a4paper]{geometry}\n" +
                "\\usepackage{moreverb}\n" +
                "\\usepackage{graphicx}\n" +
                "\\usepackage{wrapfig}\n" +
                "\\usepackage{fancyhdr}\n" +
                "\\usepackage{lipsum} \n" +
                "\\usepackage[svgnames]{xcolor}\n" +
                "\\usepackage{multirow}\n" +
                "\\usepackage{dcolumn}\n" +
                "\\usepackage{tabularx}\n" +
                "\\usepackage{graphicx}\n" +
                "\\usepackage{transparent}\n" +
                "\\usepackage{enumitem}\n" +
                "\\usepackage{amssymb}\n" +
                "\n" +
                "\\usepackage{eso-pic}\n" +
                "\\AddToShipoutPicture*{\n" +
                "    \\put(0,0){\n" +
                "        \\parbox[b][\\paperheight]{\\paperwidth}{%\n" +
                "            \\vfill\n" +
                "            \\centering\n" +
                "            {\\transparent{0.05}\\includegraphics[width=0.8\\textwidth]{logo.png}}%\n" +
                "            \\vfill\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "\n" +
                "%\\geometry{verbose,tmargin=2em,bmargin=2em,lmargin=3em,rmargin=3em}\n" +
                "\\geometry{verbose,tmargin=2em,bmargin=4em,lmargin=2em,rmargin=2em}\n" +
                "\\setlength{\\parindent}{0pt}\n" +
                "\\setlength{\\parskip}{1ex plus 0.5ex minus 0.2ex}\n" +
                "\n" +
                "\\pagestyle{fancy}\n" +
                "\\fancyhead{} % clear all header fields\n" +
                "\\fancyfoot{} % clear all footer fields\n" +
                "\\cfoot{\\hfill \\tiny{gesnotes.com}}\n" +
                "\n" +
                "\\begin{document}\n" +
                "\n" +
                "\\pagenumbering{gobble}\n" +
                "\\renewcommand{\\arraystretch}{1.2}\n");

        for (Map.Entry<Inscription, LigneTrimestreDto> entry : map.entrySet()) {
            Inscription inscription = entry.getKey();
            LigneTrimestreDto ligneTrimestreDto = entry.getValue();

            if (ligneTrimestreDto != null) {
                s.append("\\newpage\n");
                s.append("\\begin{minipage}[c]{0.20\\textwidth}\n" +
                        "\\includegraphics[width=\\textwidth]{logo.png}\n" +
                        "\\end{minipage}\n" +
                        "\\hfill\n" +
                        "\\begin{minipage}[c]{0.75\\textwidth}\n");

                s.append("\\underline{\\textbf{\\large{").append(inscription.getStudent().getName().toUpperCase()).append("}}} \\\\[0.2cm]\n")
                        .append("Matricule/Regist number: \\textbf{").append(inscription.getStudent().getMatricule()).append("}\t\t\t\t\t\t\\hfill")
                        .append("\tAnnée scolaire: \\textbf{").append(inscription.getPromo().getAnnee().toString()).append("} \\\\\n")
                        .append("\tDate de naissance/Date of birth: \\textbf{").append(inscription.getStudent().getDateNaissanceFormatted()).append("}\t\t\\hfill")
                        .append("\tClasse: \\textbf{").append(ligneTrimestreDto.getClasse()).append("}, Effectif: \\textbf{").append(ligneTrimestreDto.getEffectif()).append("} \\\\\n")
                        .append("Sexe: \\textbf{").append(inscription.getStudent().getSexe()).append("} \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\\hfill")
                        .append("\tProf titulaire: \\textbf{").append(inscription.getPromo().getProfPrincipal()).append("}\\\\\n")
                        .append("\\end{minipage}\n" + "\\vspace{0.3cm}");

                s.append(
                        "\\begin{center}\n" +
                                "\t%\\begin{tabular}{|L{2cm}|C{4cm}|c|c|c|c|d{1}|}\n" +
                                "\t\\textbf{BULLETIN ").append(ligneTrimestreDto.getTrimestre().toUpperCase()).append("}\\\\\n" +
                                "\t\\vspace{0.2cm}" +
                                "\t\\begin{tabularx}{\\textwidth}{|>{\\centering\\arraybackslash}p{0.5cm}|p{4.5cm}|>{\\centering\\arraybackslash}X|" +
                                ">{\\centering\\arraybackslash}X|>{\\centering\\arraybackslash}X|p{0.8cm}|p{6cm}|}\n" +
                                "\t\t\\hline\t\n");

                s.append("\\textbf{Cf} & \\textbf{Matière/Subject} &");

                for (SequenceDto sequence : ligneTrimestreDto.getSequences()) {
                    s.append("\\textbf{").append(sequence.getName()).append("}\t&");
                }

                s.append("\\textbf{My/Av} & \\textbf{R/P} & \\textbf{Appréciations/Observations } \\\\\\hline\t");


                for (LigneGroupeDto ligneGroupeDto : ligneTrimestreDto.getGroupes()) {

                    for (LigneMatiereDto ligneMatiereDto : ligneGroupeDto.getMatieres()) {
                        s.append(Math.round(ligneMatiereDto.getCoef())).append("\t&")
                                .append(ligneMatiereDto.getMatiere()).append("\t&");

                        for (SequenceDto sequence : ligneTrimestreDto.getSequences()) {
                            s.append(getNote(sequence.getId(), ligneMatiereDto)).append("\t&");
                        }

                        s.append(ligneMatiereDto.getMoyenne()).append("\t&")
                                .append(ligneMatiereDto.getRang()).append("\t&")
                                .append(ligneMatiereDto.getTeacher() + "\\hfill").append("\\scriptsize{").append(ligneMatiereDto.getMention()).append("}\\\\\t\\hline");
                    }

                    s.append("\t\t\\multicolumn{5}{|l|}{\\textbf{Moy du groupe/Group average:} \\hfill ")
                            .append("\\textbf{").append(ligneGroupeDto.getMoyenne())
                            .append("/20}} & \\multicolumn{2}{c|}{\\multirow{2}{*}{\\textbf{Rang/position :} ")
                            .append(ligneGroupeDto.getRang()).append("/").append(ligneTrimestreDto.getEffectif()).append("} } \\\\ \t\t\\cline{1-5}\n")
                            .append("\t\t\\multicolumn{5}{|l|}{\\textbf{Moy de la classe/Class average:} \\hfill ")
                            .append("\\textbf{").append(ligneGroupeDto.getMoyenneGenerale()).append("/20}} & \\multicolumn{2}{l|}{}\\\\\\hline\t\t\n");
                }

                s.append("\t\t\\multicolumn{4}{|l|}{\\textbf{Moyenne Trimestrielle/Term average :} \\hfill ")
                        .append("\\textbf{").append(ligneTrimestreDto.getMoyenne()).append("/20}} & \\multicolumn{3}{|c|}{Plus forte moyenne/Highest mark : \\hfill ")
                        .append("\\textbf{").append(ligneTrimestreDto.getHighestMark()).append("/20 }} \\\\ \\hline\n")
                        .append("\t\t\\multicolumn{4}{|l|}{\\textbf{Rang Trimestriel :} \\hfill ")
                        .append("\\textbf{").append(ligneTrimestreDto.getRang()).append("/").append(ligneTrimestreDto.getEffectif()).append("}")
                        .append("} & \\multicolumn{3}{|c|}{Moyenne Générale : \\hfill ")
                        .append("\\textbf{").append(ligneTrimestreDto.getMoyenneGenerale()).append("/20}} \\\\ \\hline\n")
                        .append("\t\t\\multicolumn{4}{|l|}{} & \\multicolumn{3}{|c|}{Plus faible moyenne/Lowest mark : \\hfill ")
                        .append("\\textbf{").append(ligneTrimestreDto.getLowestMark()).append("/20}} \\\\ \n")
                        .append("\n\t\t\\hline\t\n")
                        .append("\\end{tabularx}\n").append("\\bigskip\n").append("\n\\end{center}\n");


                s.append("\\begin{scriptsize}\n" +
                        "\\begin{minipage}[c]{0.60\\textwidth}\n" +
                        "\t\\underline{\\textbf{Appréciations académiques/Academic remarks}}\\\\[0.2cm]\n" +
                        "\t\\begin{minipage}[t]{0.37\\textwidth}\n" +
                        "\t\t\t\\begin{itemize}[label=$\\square$, leftmargin=1cm ,parsep=0cm,itemsep=0.2cm,topsep=0cm, font=\\LARGE]\n" +
                        "\t\t\t\t\\item T.Bien/V.Good\n" +
                        "\t\t\t\t\\item A peine suffisant\n" +
                        "\t\t\t\t\\item Félicitations\n" +
                        "\t\t\t\t\\item En baisse\n" +
                        "\t\t\t\t\\item Risque d'exclusion\n" +
                        "\t\t\t\\end{itemize}\n" +
                        "\t\t\\end{minipage}\n" +
                        "\t\t\\begin{minipage}[t]{0.35\\textwidth}\n" +
                        "\t\t\\begin{itemize}[label=$\\square$, leftmargin=* ,parsep=0cm,itemsep=0.2cm,topsep=0cm, font=\\LARGE]\n" +
                        "\t\t\t\\item Assez-bien\n" +
                        "\t\t\t\t\\item insuffisant\n" +
                        "\t\t\t\t\\item En progrès\n" +
                        "\t\t\t\t\\item Risque redoublement\n" +
                        "\t\t\t\t\\item Engagement non tenu\n" +
                        "\t\t\\end{itemize}\n" +
                        "\t\\end{minipage}\n" +
                        "\\begin{minipage}[t]{0.25\\textwidth}\n" +
                        "\t\t\\begin{itemize}[label=$\\square$, leftmargin=* ,parsep=0cm,itemsep=0.2cm,topsep=0cm, font=\\LARGE]\n" +
                        "\t\t\t\\item Passable\n" +
                        "\t\t\t\t\\item Faible\n" +
                        "\t\t\\end{itemize}\n" +
                        "\t\\end{minipage}\n" +
                        "\\end{minipage}\n" +
                        "\\hfill\n" +
                        "\\begin{minipage}[c]{0.40\\textwidth}\n" +
                        "\\underline{\\textbf{Appréciations disciplinaires/Discipline appreciations}}\\\\[0.2cm]\n" +
                        "\t\\begin{minipage}[c]{0.45\\textwidth}\n" +
                        "\t\t\t\\begin{itemize}[label=$\\square$, leftmargin=* ,parsep=0cm,itemsep=0.2cm,topsep=0cm, font=\\LARGE]\n" +
                        "\t\t\t\t\\item Attention conduite\n" +
                        "\t\t\t\t\\item Tab d'hon de cond\n" +
                        "\t\t\t\t\\item Engagement\n" +
                        "\t\t\t\t\\item Exclusion\n" +
                        "\t\t\t\t\\item Retards\n" +
                        "\t\t\t\\end{itemize}\n" +
                        "\t\t\\end{minipage}\n" +
                        "\t\t\\begin{minipage}[c]{0.45\\textwidth}\n" +
                        "\t\t\\begin{itemize}[label=$\\square$, leftmargin=* ,parsep=0cm,itemsep=0.2cm,topsep=0cm, font=\\LARGE]\n" +
                        "\t\t\t\\item Avertis conduite\n" +
                        "\t\t\t\t\\item Colle\n" +
                        "\t\t\t\t\\item Engagement non tenu\n" +
                        "\t\t\t\t\\item Engagement cond tenu\n" +
                        "\t\t\t\t\\item Absences\n" +
                        "\t\t\\end{itemize}\n" +
                        "\t\\end{minipage}\n" +
                        "\\end{minipage}\n" +
                        "\n" +
                        "\\end{scriptsize}\n");

                s.append("\\vspace{0.5cm}" +
                        "\n" +
                        "\\begin{tabularx}{\\textwidth}{|X|X|X|X|}\n" +
                        "\t\t\\hline\t\n" +
                        "\t\t %&  &  &  \\\\\n" +
                        "  \t%\\hline \n" +
                        "\t\t\\multicolumn{2}{|>{\\centering\\arraybackslash}X|}{\\textbf{Observations}} & \\multicolumn{2}{|>{\\centering\\arraybackslash}X|}{\\textbf{Visa du chef d'établissement} } \\\\\\hline\n" +
                        "\t\t\\multicolumn{2}{|X|}{} & \\multicolumn{2}{|X|}{ } \\\\\n" +
                        "  \t\\multicolumn{2}{|X|}{} & \\multicolumn{2}{|X|}{ } \\\\\n" +
                        "\t\t\\multicolumn{2}{|X|}{} & \\multicolumn{2}{|X|}{ } \\\\\n" +
                        "\t\t\\multicolumn{2}{|X|}{} & \\multicolumn{2}{|X|}{ } \\\\\\hline\n" +
                        "\\end{tabularx}\n");
            }
        }
        s.append("\n\n" + "\\end{document}");

        return s.toString();
    }

    private Double getNote(Long sequenceId, LigneMatiereDto ligneMatiereDto) {
        StudentNoteDto studentNote  = ligneMatiereDto.getNotes().stream()
                .filter(note -> note.getSequenceId() == sequenceId)
                .findAny()
                .orElse(null);

        if(studentNote != null)
        return studentNote.getNote();

        return Double.valueOf(0);
    }
}

