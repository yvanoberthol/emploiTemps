package com.myschool.service;


import com.myschool.dto.*;
import com.myschool.repository.*;
import com.myschool.security.SecurityUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service Implementation for managing Student.
 */
@Service
@Transactional
public class DownloadService {

    private final Logger log = LoggerFactory.getLogger(DownloadService.class);

    @Value("${dir.myschool}")
    private String MYSCHOOL_FOLDER;

    @Autowired
    private StudentService studentService;

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

    public String createBulletin(Long studentId, Long anneId, Long trimestreId) throws IOException {
        StudentDto student = studentService.findOne(studentId, anneId);
        LigneTrimestreDto ligneTrimestreDto = student.getNotes().stream()
                .filter(ligneTrimestre -> ligneTrimestre.getTrimestreId() == trimestreId)
                .findAny()
                .orElse(null);

        //System.out.println(ligneTrimestreDto);
        String TEX_FOLDER = MYSCHOOL_FOLDER + "data/" + SecurityUtils.getCurrentUserLogin() + "/tex/";
        String makefile = createMakeFile();
        String texTemplate = createTex(ligneTrimestreDto);

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
        for(StudentDto studentDto: students){
            LigneTrimestreDto ligneTrimestreDto = studentDto.getNotes().stream()
                    .filter(ligneTrimestre -> ligneTrimestre.getTrimestreId() == trimestreId)
                    .findAny()
                    .orElse(null);
            ligneTrimestreDtos.add(ligneTrimestreDto);
        }

        String TEX_FOLDER = MYSCHOOL_FOLDER + "data/" + SecurityUtils.getCurrentUserLogin() + "/tex/";
        String makefile = createMakeFile();
        String texTemplate = createTex(ligneTrimestreDtos);

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

    private String createMakeFile() {
        StringBuilder s = new StringBuilder();
        s.append("all :\n")
                //.append("\trm -rf *.log *.aux *.pdf\n")
                //to time for watermark to render correctly
                .append("\tpdflatex *.tex\n" )
                .append("\tpdflatex *.tex\n" );
                ///.append("\trm -rf *.log *.aux *.tex *.epl");
        return s.toString();
    }

    private String createTex(LigneTrimestreDto ligneTrimestreDto) throws IOException {
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
                "            {\\transparent{0.05}\\includegraphics[width=0.8\\textwidth]{olive.jpeg}}%\n" +
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
                    "\\includegraphics[width=\\textwidth]{olive.jpeg}\n" +
                    "\\end{minipage}\n" +
                    "\\hfill\n" +
                    "\\begin{minipage}[c]{0.75\\textwidth}\n");

            s.append("\\underline{\\textbf{ \\large{FOSSO MARC ANDREW}}} \\\\[0.2cm]\n")
            .append("Matricule/Regist number: \\textbf{xxxxxxxxxx}\t\t\t\t\t\t\\hfill")
                    .append("\tAnnée scolaire: \\textbf{2019 - 2020} \\\\\n")
            .append("\tDate de naissance/Date of birth: \\textbf{01/11/2003}\t\t\\hfill")
                    .append("\tClasse: \\textbf{").append(ligneTrimestreDto.getClasse()).append("}, Effectif: \\textbf{").append(ligneTrimestreDto.getEffectif()).append("} \\\\\n")
            .append("Sexe: \\textbf{Masculin} \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\\hfill")
                    .append("\tProf titulaire: \\textbf{Ndongo}\\\\\n")
            .append("\\end{minipage}\n" + "\\vspace{0.3cm}");

            s.append(
            "\\begin{center}\n" +
            "\t%\\begin{tabular}{|L{2cm}|C{4cm}|c|c|c|c|d{1}|}\n" +
            "\t\\textbf{BULLETIN DU 2EME TRIMESTRE 2017/2018}\\\\\n" +
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
                .append(ligneGroupeDto.getRang()).append("/4} } \\\\ \t\t\\cline{1-5}\n")
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

    private String createTex(List<LigneTrimestreDto> ligneTrimestreDtos) throws IOException {
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
                "            {\\transparent{0.05}\\includegraphics[width=0.8\\textwidth]{olive.jpeg}}%\n" +
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

        for(LigneTrimestreDto ligneTrimestreDto: ligneTrimestreDtos){
            s.append("\\newpage\n");
            s.append("\\begin{minipage}[c]{0.20\\textwidth}\n" +
                    "\\includegraphics[width=\\textwidth]{olive.jpeg}\n" +
                    "\\end{minipage}\n" +
                    "\\hfill\n" +
                    "\\begin{minipage}[c]{0.75\\textwidth}\n");

            s.append("\\underline{\\textbf{ \\large{FOSSO MARC ANDREW}}} \\\\[0.2cm]\n")
                    .append("Matricule/Regist number: \\textbf{xxxxxxxxxx}\t\t\t\t\t\t\\hfill")
                    .append("\tAnnée scolaire: \\textbf{2019 - 2020} \\\\\n")
                    .append("\tDate de naissance/Date of birth: \\textbf{01/11/2003}\t\t\\hfill")
                    .append("\tClasse: \\textbf{").append(ligneTrimestreDto.getClasse()).append("}, Effectif: \\textbf{").append(ligneTrimestreDto.getEffectif()).append("} \\\\\n")
                    .append("Sexe: \\textbf{Masculin} \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\\hfill")
                    .append("\tProf titulaire: \\textbf{Ndongo}\\\\\n")
                    .append("\\end{minipage}\n" + "\\vspace{0.3cm}");

            s.append(
                    "\\begin{center}\n" +
                            "\t%\\begin{tabular}{|L{2cm}|C{4cm}|c|c|c|c|d{1}|}\n" +
                            "\t\\textbf{BULLETIN DU 2EME TRIMESTRE 2017/2018}\\\\\n" +
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
                        .append(ligneGroupeDto.getRang()).append("/4} } \\\\ \t\t\\cline{1-5}\n")
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
        s.append("\n\n" + "\\end{document}");

        return s.toString();
    }

    private Double getNote(Long sequenceId, LigneMatiereDto ligneMatiereDto) {
        StudentNoteDto studentNote  = ligneMatiereDto.getNotes().stream()
                .filter(note -> note.getSequenceId() == sequenceId)
                .findAny()
                .orElse(null);

        return studentNote.getNote();
    }
}

