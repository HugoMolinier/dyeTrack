package com.example.dyeTrack.core.service;

import com.example.dyeTrack.core.entity.*;
import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.entity.setOfPlannedExercise.SetOfPlannedExercise;
import com.example.dyeTrack.core.port.in.ExportDayDataUseCase;
import com.example.dyeTrack.core.port.out.ExportRequestPort;
import com.example.dyeTrack.core.port.out.MailPort;
import com.example.dyeTrack.core.port.out.UserPort;
import com.example.dyeTrack.core.util.EntityUtils;
import com.example.dyeTrack.core.util.HashUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.function.BiFunction;

@Service
public class ExportDayDataService implements ExportDayDataUseCase {

    private final ExportRequestPort exportRequestPort;
    private final UserPort userPort;
    private final MailPort mailPort;
    private final String emailSecretKey;

    public ExportDayDataService(ExportRequestPort exportRequestPort, UserPort userPort, MailPort mailPort, @Value("${email.secret.key}") String emailSecretKey) {
        this.exportRequestPort = exportRequestPort;
        this.userPort = userPort;
        this.mailPort = mailPort;
        this.emailSecretKey = emailSecretKey;
    }

    @Override
    public ExportRequest createExportRequest(Long idUser, LocalDate startDate, LocalDate endDate) {
        User user = EntityUtils.getUserOrThrow(idUser, userPort);
        ExportRequest request = new ExportRequest();
        request.setUser(user);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        return exportRequestPort.save(request);
    }

    @Override
    public void runExport(Long exportRequestId) {
        ExportRequest request = exportRequestPort.getById(exportRequestId);

        try {
            request.markRunning();
            exportRequestPort.save(request);

            List<DayDataOfUser> data =
                    exportRequestPort.getDayDataBetween(
                            request.getUser().getId(),
                            request.getStartDate(),
                            request.getEndDate()
                    );

            byte[] file = this.generateExcelFile(data, request);


            mailPort.sendExportMail(  HashUtil.decryptEmail(request.getUser().getEmail(),this.emailSecretKey), file, "export_seances.xlsx");

            request.markDone();
            exportRequestPort.save(request);

        } catch (Exception e) {
            request.markError(e.getMessage());
            exportRequestPort.save(request);
        }
    }

    //Visuel & Data
    private byte[] generateExcelFile(List<DayDataOfUser> allData, ExportRequest request) {


        Workbook workbook = new XSSFWorkbook();

        Map<String, CellStyle> allStyle = getAllCellStyle(workbook);
        Sheet s_visuel = workbook.createSheet("Session Visual");
        Sheet s_data = workbook.createSheet("Session Data");
        Sheet other_data = workbook.createSheet("Other Data");
        int rowNumDataS = 0;
        int rowNumDataOther = 0;
        int rowNumVisuel = 0;

        List<String> headerVisuel = List.of( "start date", "end date", "total sessions", "average calories", "average steps", "total sets", "weighted sum(kg)", "", "username", "request date" );
        Row titleRow = s_visuel.createRow(rowNumVisuel++);
        Cell titleCell = titleRow.createCell(3);
        titleCell.setCellValue("Information");

        mergeAndStyle(
                s_visuel,
                titleRow,
                3,
                12,
                allStyle.get("TITLEINFORMATION")
        );

        Row headerRowVisuel = s_visuel.createRow(rowNumVisuel++);//#F1A983
        Row contentInformationRowVisuel = s_visuel.createRow(rowNumVisuel++);
        for (int i = 0; i < headerVisuel.size(); i++) {
            Cell cell = headerRowVisuel.createCell(i + 3);
            cell.setCellValue(headerVisuel.get(i));
            cell.setCellStyle(allStyle.get("HEADERINFORMATION"));

            Cell cellContent = contentInformationRowVisuel.createCell(i + 3);
            cellContent.setCellStyle(allStyle.get("CONTENTINFORMATION"));
        }
        s_visuel.createRow(rowNumVisuel++);

        Row RowFullHeaderRow = s_visuel.createRow(rowNumVisuel++);
        RowFullHeaderRow.createCell(0).setCellValue("Info");
        mergeAndStyle(
                s_visuel,
                RowFullHeaderRow,
                0,
                1,
                allStyle.get("TITLEINFO")
        );

        RowFullHeaderRow.createCell(3).setCellValue("Séance");

        mergeAndStyle(
                s_visuel,
                RowFullHeaderRow,
                3,
                12,
                allStyle.get("TITLESeance")
        );

        RowFullHeaderRow.createCell(14).setCellValue("Nutrition");
        mergeAndStyle(
                s_visuel,
                RowFullHeaderRow,
                14,
                20,
                allStyle.get("TITLENUTRI")
        );
        List<String> headerList = List.of( "session date", "preset", "", "order", "exercise", "set count", "rir", "performance", "weighted sum", "type", "laterality", "primary muscle", "muscle group", "", "calories", "proteins", "carbohydrates", "fats", "fiber", "step_count", "sleep" );

        // Header data
        List<String> headersDataNutriPhysio = List.of( "session_date", "calories", "proteins", "carbohydrates", "fats", "fiber", "step_count", "sleep" );
        Row headerRowAutre = other_data.createRow(rowNumDataOther++);
        for (int i = 0; i < headersDataNutriPhysio.size(); i++) {
            headerRowAutre.createCell(i).setCellValue(headersDataNutriPhysio.get(i));
        }

        List<String> headersDataSeance = List.of( "session_date", "preset", "exercise_order", "exercise_name", "set_order", "rep", "left_reps", "right_reps", "weight_kg", "rir", "type", "laterality", "primary_muscle", "muscle_group" );
        Row headerRow = s_data.createRow(rowNumDataS++);
        for (int i = 0; i < headersDataSeance.size(); i++) {
            headerRow.createCell(i).setCellValue(headersDataSeance.get(i));
        }


        Map<Long, Muscle> mainMuscleCache = new HashMap<>();
        //1 Ligne par série
        String firstDate = allData.get(0).getDay().toString();
        String lastDate = allData.get(allData.size() - 1).getDay().toString();
        int nombreTotalSeance=0;
        double Moyennecalorie=0;
        double MoyenneStep=0;
        int totalPhysioData=0;
        int totalNutriData=0;
        int series_totales=0;
        double somme_ponderee_kg=0;
        for (DayDataOfUser d : allData) {


            SeanceTrack seanceTrack = d.getSeanceTrack();
            PhysioTrack physioTrack = d.getPhysioTrack();
            NutritionTrack nutritionTrack = d.getNutritionTrack();
            if(seanceTrack!=null ||( physioTrack != null && !physioTrack.isEmpty())
                    ||( nutritionTrack != null && !nutritionTrack.isEmpty())) {
                nombreTotalSeance+=1;
                Row ligne6 = s_visuel.createRow(rowNumVisuel++);
                for (int i = 0; i < headerList.size(); i++) {
                    String style = (i <= 1) ? "HEADERINFO"
                            : (i >= 14) ? "HEADERNutri"
                            : (i >= 3 && i<13) ? "HEADERSeance"
                            : "";
                    Cell cell = ligne6.createCell(i );
                    cell.setCellValue(headerList.get(i));
                    cell.setCellStyle(allStyle.get(style));
                }


                Row ligneSousHeader = s_visuel.createRow(rowNumVisuel++);
                LocalDate date_seance = d.getDay();
                Cell cell = ligneSousHeader.createCell(0);
                cell.setCellValue(date_seance.toString());
                cell.setCellStyle(allStyle.get("CONTENTINFO"));

                if ((physioTrack != null && !physioTrack.isEmpty())
                        || (nutritionTrack != null && !nutritionTrack.isEmpty())) {



                    List<Integer> values = new ArrayList<>();

                    if (nutritionTrack != null && !nutritionTrack.isEmpty()) {
                        totalNutriData+=1;
                        Moyennecalorie+=nutritionTrack.getCalories();
                        values.add(nutritionTrack.getCalories());
                        values.add(nutritionTrack.getProteins());
                        values.add(nutritionTrack.getCarbohydrates());
                        values.add(nutritionTrack.getLipids());
                        values.add(nutritionTrack.getFiber());
                    }
                    if (physioTrack != null && !physioTrack.isEmpty()) {
                        totalPhysioData+=1;
                        MoyenneStep+=physioTrack.getStep();
                    }

                    int step = (physioTrack != null && !physioTrack.isEmpty()) ?physioTrack.getStep() : 0;
                    float sleep = (physioTrack != null && !physioTrack.isEmpty()) ? physioTrack.getHourOfSleep() : 0;


                    // ===== Insertion dans la feuille "Autre Data" =====
                    int startCol = 14;
                    Row rowOther = other_data.createRow(rowNumDataOther++);
                    rowOther.createCell(0).setCellValue(date_seance.toString());
                    for (int i = 0; i < values.size(); i++) {
                        rowOther.createCell(i + 1).setCellValue(values.get(i));
                        Cell cellnutri = ligneSousHeader.createCell(startCol + i);
                        cellnutri.setCellValue( values.get(i));
                        cellnutri.setCellStyle(allStyle.get("CONTENTNUTRI"));
                    }
                    rowOther.createCell(6).setCellValue(step);
                    // sleep
                    Cell cellStep = ligneSousHeader.createCell(19);
                    cellStep.setCellValue(step);
                    cellStep.setCellStyle(allStyle.get("CONTENTNUTRI"));

                    rowOther.createCell(7).setCellValue(sleep);
                    // sleep
                    Cell cellSleep = ligneSousHeader.createCell(20);
                    cellSleep.setCellValue(sleep);
                    cellSleep.setCellStyle(allStyle.get("CONTENTNUTRI"));


                }

                if (seanceTrack != null) {
                    String presetName = seanceTrack.getPresetSeance() != null
                            ? seanceTrack.getPresetSeance().getName()
                            : "N/A";


                    Cell cellPreset = ligneSousHeader.createCell(1);
                    cellPreset.setCellValue(presetName);
                    cellPreset.setCellStyle(allStyle.get("CONTENTINFO"));
                    boolean first =true;
                    //partis Exercise
                    int totalNumSerie=0;
                    int lastOrder=0;
                    Set<String> allmainMuscleName = new HashSet<>();
                    Set<String> allmainGroupName = new HashSet<>();

                    double totalSumPond=0;
                    for (PlannedExercise exercisePlanned : seanceTrack.getPlannedExercises().stream()
                            .sorted(Comparator.comparingInt(PlannedExercise::getExerciseOrder))
                            .toList()) {
                        //
                        double sommePondExercis=0;
                        int ordreExercise = exercisePlanned.getExerciseOrder() + 1;
                        lastOrder = ordreExercise;
                        Long exerciseId = exercisePlanned.getExercise().getIdExercise();
                        Muscle mainMuscle = mainMuscleCache.computeIfAbsent(
                                exerciseId,
                                id -> exercisePlanned.getExercise()
                                        .getRelExerciseMuscles()
                                        .stream()
                                        .filter(RelExerciseMuscle::isPrincipal)
                                        .map(RelExerciseMuscle::getMuscle)
                                        .findFirst()
                                        .orElse(null)
                        );
                        if (mainMuscle == null) {
                            continue; // ou gestion d’erreur
                        }
                        String mainMuscleName = mainMuscle.getNameEN();
                        String mainGroupName = mainMuscle.getMuscleGroup().getNameEN();
                        allmainMuscleName.add(mainMuscleName);
                        allmainGroupName.add(mainGroupName);
                        String nom_exercise = exercisePlanned.getExercise().getNameEN();
                        String lateralite = exercisePlanned.getLateralite().getNameEN();
                        String equipment = exercisePlanned.getEquipment().getNameEN();

                        String setDisplayFormat = "";
                        List<String> allRir =new ArrayList<>();
                        int numSerie=0;
                        Row ligneExercise;
                        if(first){
                            ligneExercise = ligneSousHeader;
                        }else {
                            ligneExercise = s_visuel.createRow(rowNumVisuel++);
                        }
                         first =false;

                        Double previousCharge= null;
                        double leftRep=0;
                        for (SetOfPlannedExercise set : exercisePlanned.getSetsOfPlannedExercise().stream()
                                .sorted(Comparator.comparingInt(SetOfPlannedExercise::getSetOrder))
                                .toList()) {
                            int ordreSerie = set.getSetOrder() + 1;

                            if (set.getSide() == SetOfPlannedExercise.Side.RIGHT) {
                                int lastRow = s_data.getLastRowNum();

                                s_data.getRow(lastRow).createCell(7).setCellValue(set.getRepsNumber());
                                setDisplayFormat+=" / " +set.getRepsNumber();
                                setDisplayFormat+= " - ";
                                sommePondExercis+=((set.getRepsNumber()+ leftRep)*set.getCharge()/2);
                            } else {
                                numSerie++;
                                int reps = set.getRepsNumber();
                                double charge_kg = set.getCharge();
                                int rir = set.getRir();
                                allRir.add(String.valueOf(rir));
                                if (previousCharge != null && charge_kg != previousCharge) {
                                    setDisplayFormat+=previousCharge+"kg |  ";
                                }
                                previousCharge = charge_kg;
                                setDisplayFormat+=reps;

                                if (set.getSide() == SetOfPlannedExercise.Side.BOTH) {
                                    setDisplayFormat += " - ";
                                    sommePondExercis+=(reps*charge_kg);
                                }else{
                                    leftRep=reps;
                                }
                                String repBoth = set.getSide() == SetOfPlannedExercise.Side.BOTH ? String.valueOf(reps) : "";
                                String repLeft = set.getSide() == SetOfPlannedExercise.Side.LEFT ? String.valueOf(reps) : "";

                                Row row = s_data.createRow(rowNumDataS++);
                                row.createCell(0).setCellValue(date_seance.toString());
                                row.createCell(1).setCellValue(presetName);
                                row.createCell(2).setCellValue(ordreExercise);
                                row.createCell(3).setCellValue(nom_exercise);
                                row.createCell(4).setCellValue(ordreSerie);
                                row.createCell(5).setCellValue(repBoth);
                                row.createCell(6).setCellValue(repLeft);
                                row.createCell(8).setCellValue(charge_kg);
                                row.createCell(9).setCellValue(rir);
                                row.createCell(10).setCellValue(equipment);
                                row.createCell(11).setCellValue(lateralite);
                                row.createCell(12).setCellValue(mainMuscleName);
                                row.createCell(13).setCellValue(mainGroupName);
                            }
                        }
                        totalNumSerie+=numSerie;
                        setDisplayFormat+=previousCharge+"kg";
                        totalSumPond+=sommePondExercis;

                        List<Object> addToExerciceRow = List.of(ordreExercise,nom_exercise,numSerie,String.join(" - ", allRir),setDisplayFormat,sommePondExercis,equipment,lateralite,mainMuscleName,mainGroupName );
                        String r = (rowNumVisuel%2 ==1) ?"CONTENTSeance1":"CONTENTSeance2";
                        fillRowWithValues(ligneExercise, 3, addToExerciceRow, allStyle.get(r));


                    }
                    series_totales+=totalNumSerie;
                    somme_ponderee_kg+=totalSumPond;
                    Row total = (lastOrder == 0)
                            ? ligneSousHeader
                            : s_visuel.createRow(rowNumVisuel++);

                    String r = (rowNumVisuel % 2 == 1) ? "CONTENTSeance1" : "CONTENTSeance2";
                    CellStyle baseStyle = allStyle.get(r);
                    CellStyle finalStyle = (lastOrder != 0)
                            ? withTopBorder(workbook, baseStyle)
                            : baseStyle;

                    List<Object> addToExerciceRow = List.of(
                            "Total", lastOrder, totalNumSerie, "", "",
                            totalSumPond, "", "",
                            String.join(", ", allmainMuscleName),
                            String.join(", ", allmainGroupName)
                    );

                    fillRowWithValues(total, 3, addToExerciceRow, finalStyle);                }

                s_visuel.createRow(rowNumVisuel++);
                s_visuel.createRow(rowNumVisuel++);
            }


        }

        //Data
        Row dataInfoRow = s_visuel.getRow(2);

        dataInfoRow.getCell(3).setCellValue(firstDate);
        dataInfoRow.getCell(4).setCellValue(lastDate);
        dataInfoRow.getCell(5).setCellValue(nombreTotalSeance);
        dataInfoRow.getCell(6).setCellValue(Moyennecalorie/totalNutriData);
        dataInfoRow.getCell(7).setCellValue(MoyenneStep/totalPhysioData);
        dataInfoRow.getCell(8).setCellValue(series_totales);
        dataInfoRow.getCell(9).setCellValue(somme_ponderee_kg);
        dataInfoRow.getCell(11).setCellValue(request.getUser().getPseudo());
        dataInfoRow.getCell(12).setCellValue(request.getCreatedAt());
        for (int i = 0; i <= 20; i++) {
            s_visuel.autoSizeColumn(i);
        }
        applyColumnStyle(s_visuel, 2, allStyle.get("Border"));   // C
        applyColumnStyle(s_visuel, 13, allStyle.get("Border"));
        s_visuel.setColumnWidth(2, 10 * 37);  // 10px pour la colonne C (index 2)
        s_visuel.setColumnWidth(13, 10 * 37);
        System.out.println(workbook);


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Erreur génération export Excel", e);
        } finally {
            try {
                workbook.close();
            } catch (IOException ignored) {
            }
        }
        return outputStream.toByteArray();
    }


    private Map<String, CellStyle> getAllCellStyle(Workbook workbook) {
        Map<String, CellStyle> styles = new HashMap<>();

        // Helper pour créer un style
        BiFunction<Color[], Boolean, CellStyle> createStyle = (colors, bold) -> {
            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
            // Fond
            if (colors[0] != null) style.setFillForegroundColor(new XSSFColor(colors[0], null));
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Alignement centré par défaut
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            // Police
            XSSFFont font = (XSSFFont) workbook.createFont();
            if (colors.length > 1 && colors[1] != null) font.setColor(new XSSFColor(colors[1], null));
            font.setBold(bold);
            style.setFont(font);
            return style;
        };

        // ============================
        // Styles Orange
        styles.put("TITLEINFORMATION", createStyle.apply(new java.awt.Color[]{new java.awt.Color(0xBE, 0x50, 0x14), new java.awt.Color(0xFB, 0xE2, 0xD5)}, true));
        styles.put("HEADERINFORMATION", createStyle.apply(new java.awt.Color[]{new java.awt.Color(0xF1, 0xA9, 0x83), null}, true));
        styles.put("CONTENTINFORMATION", createStyle.apply(new java.awt.Color[]{new java.awt.Color(0xFB, 0xE2, 0xD5), null}, false));

        // ============================
        // Styles Violet
        styles.put("TITLEINFO", createStyle.apply(new java.awt.Color[]{new java.awt.Color(0x51, 0x15, 0x4A), new java.awt.Color(0xF2, 0xCE, 0xEF)}, true));
        styles.put("HEADERINFO", createStyle.apply(new java.awt.Color[]{new java.awt.Color(0xD8, 0x6D, 0xCD), null}, true));
        styles.put("CONTENTINFO", createStyle.apply(new java.awt.Color[]{new java.awt.Color(0xF2, 0xCE, 0xEF), null}, false));

        // ============================
        // Styles Bleu Seance
        styles.put("TITLESeance", createStyle.apply(new java.awt.Color[]{new java.awt.Color(21, 96, 130), new java.awt.Color(218, 233, 248)}, true));
        styles.put("HEADERSeance", createStyle.apply(new java.awt.Color[]{new java.awt.Color(77, 147, 217), null}, true));
        styles.put("CONTENTSeance1", createStyle.apply(new java.awt.Color[]{new java.awt.Color(166, 201, 236), null}, false));
        styles.put("CONTENTSeance2", createStyle.apply(new java.awt.Color[]{new java.awt.Color(218, 233, 248), null}, false));

        // ============================
        // Styles Vert Nutrition
        styles.put("TITLENUTRI", createStyle.apply(new java.awt.Color[]{new java.awt.Color(25, 107, 36), new java.awt.Color(218, 242, 208)}, true));
        styles.put("HEADERNutri", createStyle.apply(new java.awt.Color[]{new java.awt.Color(78, 167, 46), null}, true));
        styles.put("CONTENTNUTRI", createStyle.apply(new java.awt.Color[]{new java.awt.Color(193, 240, 200), null}, false));


        styles.put("Border", createStyle.apply(new java.awt.Color[]{new java.awt.Color(64, 64, 64), null}, false));

        return styles;
    }

    private void mergeAndStyle(
            Sheet sheet,
            Row row,
            int firstCol,
            int lastCol,
            CellStyle style
    ) {
        int rowNum = row.getRowNum();

        // Fusion
        sheet.addMergedRegion(new CellRangeAddress(
                rowNum,
                rowNum,
                firstCol,
                lastCol
        ));

        // Style sur toute la zone
        for (int col = firstCol; col <= lastCol; col++) {
            Cell cell = row.getCell(col);
            if (cell == null) {
                cell = row.createCell(col);
            }
            cell.setCellStyle(style);
        }
    }

    private void fillRowWithValues(Row row, int startCol, List<Object> values, CellStyle style) {
        for (int i = 0; i < values.size(); i++) {
            Cell cell = row.createCell(startCol + i);
            Object val = values.get(i);

            // Valeur par défaut si null
            if (val == null) {
                cell.setCellValue("");
            } else if (val instanceof Number) {
                cell.setCellValue(((Number) val).doubleValue());
            } else {
                cell.setCellValue(val.toString());
            }

            // Appliquer le style
            cell.setCellStyle(style);
        }
    }

    void applyColumnStyle(Sheet sheet, int colIndex, CellStyle style) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Cell cell = row.getCell(colIndex);
            if (cell == null) cell = row.createCell(colIndex);
            cell.setCellStyle(style);
        }

    }
    private CellStyle withTopBorder(Workbook workbook, CellStyle baseStyle) {
        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(baseStyle);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }

}