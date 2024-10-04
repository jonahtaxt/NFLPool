package com.effisoft.nflpool.services;

import com.effisoft.nflpool.interfaces.FileReader;
import com.effisoft.nflpool.model.Participant;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelReader implements FileReader {

    public List<Participant> readParticipants(InputStream fileStream) {

        List<Participant> participants = new ArrayList<>();
        int participantId = 1;
        int lastColumnIndex = -1;
        int mondayNightPointsColumn = 0;
        boolean lastRow = false;

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(fileStream);
            Sheet sheet = workbook.getSheetAt(2);
            Iterator<Row> rowIterator = sheet.iterator();

            while(rowIterator.hasNext() && !lastRow) {
                Row row = rowIterator.next();
                String participantName = null;
                int participantMondayNightPoints = 0;
                List<String> bets = new ArrayList<>();

                for (Cell cell : row) {
                    if((cell.getColumnIndex() == lastColumnIndex)) {
                        break;
                    } else if(row.getRowNum() == 0) {
                        if (CellType.STRING.equals(cell.getCellType()) &&
                                !CellType.BLANK.equals(cell.getCellType()) &&
                                cell.getStringCellValue().equals("PUNTOS")) {
                            lastColumnIndex = cell.getColumnIndex() + 1;
                            mondayNightPointsColumn = cell.getColumnIndex();
                        }
                    } else {
                        if(cell.getColumnIndex() == 0 && cell.getStringCellValue().equals("LastRow")) {
                            lastRow = true;
                            break;
                        }
                        if(cell.getColumnIndex() == 0 && !cell.getStringCellValue().equals("LastRow")) {
                            participantName = cell.getStringCellValue();
                        } else {
                            if(!(cell.getCellType() == CellType.BLANK) && (cell.getColumnIndex() <= mondayNightPointsColumn)) {
                                if(cell.getColumnIndex() == mondayNightPointsColumn) {
                                    participantMondayNightPoints = (int) cell.getNumericCellValue();
                                    participants.add(new Participant(participantId, participantName, participantMondayNightPoints, bets));
                                    participantId++;
                                } else {
                                    bets.add(sheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue());
                                }
                            }
                        }
                    }
                }
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return participants;
    }
}
