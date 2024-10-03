package com.effisoft.nflpool.services;

import com.effisoft.nflpool.interfaces.FileReader;
import com.effisoft.nflpool.model.Participant;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelReader implements FileReader {

    private FileInputStream fileInputStream;
    private XSSFWorkbook workbook;
    private Sheet sheet;

    @Override
    public List<Participant> readParticipants(int year, int week) {

        List<Participant> participants = new ArrayList<>();
        String filePath = String.format("/home/cygnusremote/Downloads/%s%s.xlsx",year,week);
        int participantId = 1;
        int lastColumnIndex = -1;
        int mondayNightPointsColumn = 0;
        Boolean lastRow = false;

        try {
            fileInputStream = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fileInputStream);
            sheet = workbook.getSheetAt(2);
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
                        if (cell.getStringCellValue().equals("PUNTOS")) {
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
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return participants;
    }
}
