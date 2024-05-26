package com.example.mywarehouse.excel;

// Java Program to Illustrate Writing
// Data to Excel File using Apache POI

// Import statements
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.example.mywarehouse.models.Order;
import com.example.mywarehouse.repositories.productRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Main class
public class Write {

    // Main driver method
    public static void write(List<Order> orders)
    {

        // Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Creating a blank Excel sheet
        XSSFSheet sheet
                = workbook.createSheet("Отчет");

        // Creating an empty TreeMap of string and Object][]
        // type
        Map<String, Object[]> data
                = new TreeMap<String, Object[]>();

        // Writing data to Object[]
        // using put() method
        Integer index=3;
        Float sum = 0f;
        data.put("1",
                new Object[] { "ID"});
        data.put("2", new Object[] {"","Товар","Количество","Продавец", "Покупатель", "Сумма"});
        for (Order order : orders) {
            data.put(index.toString(), new Object[]{order.getOrderId(), order.getProduct().getName(),
                    order.getAmount(), order.getCompanyFrom().getName(), order.getCompanyTo().getName(), order.getSum().toString()});
            index++;
            sum+= order.getSum();
        }
        data.put(index.toString(), new Object[] {"","", "", "", "", "", "Общая сумма: ", sum.toString()});

        // Iterating over data and writing it to sheet
        Set<String> keyset = data.keySet();

        int rownum = 0;

        for (String key : keyset) {

            // Creating a new row in the sheet
            Row row = sheet.createRow(rownum++);

            Object[] objArr = data.get(key);

            int cellnum = 0;

            for (Object obj : objArr) {

                // This line creates a cell in the next
                // column of that row
                Cell cell = row.createCell(cellnum++);

                if (obj instanceof String)
                    cell.setCellValue((String)obj);

                else if (obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
            }
        }

        // Try block to check for exceptions
        try {
            Path path = Path.of("D:/Docs");
            if (!Files.isDirectory(path))
                Files.createDirectories(path);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy__HH_mm_ss");
            // Writing the workbook
            FileOutputStream out = new FileOutputStream(
                    new File(path + "/Отчет__" + LocalDateTime.now().format(dateTimeFormatter) + ".xlsx"));
            workbook.write(out);

            // Closing file output connections
            out.close();

            // Console message for successful execution of
            // program
            System.out.println(
                    "Название файла.xlsx written successfully on disk.");
        }

        // Catch block to handle exceptions
        catch (Exception e) {

            // Display exceptions along with line number
            // using printStackTrace() method
            e.printStackTrace();
        }
    }
}
