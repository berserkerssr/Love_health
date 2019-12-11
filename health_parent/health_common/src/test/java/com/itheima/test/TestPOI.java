package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ClassName TestPOI
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/12/2 8:48
 * @Version V1.0
 */
public class TestPOI {
    /**
     * 工作簿：XSSFWorkBook
     * 工作表：XSSFSheet
     * 行对象：XSSFRow
     * 单元格对象：XSSFCell
     */

    // （1）从Excel文件中读取数据
    @Test
    public void readExcel() throws IOException {
        // 1：创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\hello.xlsx");
        // 2：获取工作表对象
        XSSFSheet sheet = workbook.getSheetAt(0); // 0表示第1个
        // 3：遍历工作表对象，获得行对象
        for (Row row : sheet) {
            // 4：遍历行对象，获得单元格对象
            for (Cell cell : row) {
                // 5：获取数据
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }
        // 6：关闭
        workbook.close();
    }

    // 还有一种方式就是获取工作表最后一个行号，从而根据行号获得行对象，通过行获取最后一个单元格索引，从而根据单元格索引获取每行的一个单元格对象，代码如下：
    // 问题：读取一些空白行（读取excel数据的时候，虽然表格中没有数据，不能添加样式，读取excel的时候，会读到空白行）
    @Test
    public void readExcel_2() throws IOException {
        // 1：创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\hello.xlsx");
        // 2：获取工作表对象
        XSSFSheet sheet = workbook.getSheetAt(0); // 0表示第1个
        // 3：读取最后一行的行号，通过行号获取每个行对象
        int rows = sheet.getLastRowNum();
        System.out.println(rows);
        for(int i=0;i<=rows;i++){
            XSSFRow row = sheet.getRow(i);
            // 4：读取当前行最后一个单元格列号，通过列号获取每个单元格对象
            short cellNum = row.getLastCellNum();
            //System.out.println(cellNum);
            for(short j=0;j<cellNum;j++){
                XSSFCell cell = row.getCell(j);
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }
        // 6：关闭
        workbook.close();
    }

    // （2）向Excel文件中写数据
    // 使用POI可以在内存中创建一个Excel文件并将数据写入到这个文件，最后通过输出流将内存中的Excel文件下载到磁盘
    @Test
    public void writeExcel() throws IOException {
        // 1：创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 2：创建工作表对象
        XSSFSheet sheet = workbook.createSheet("用户统计报表");
        // 3：创建行对象
        XSSFRow row0 = sheet.createRow(0);
        // 4：创建列对象，设置内容
        row0.createCell(0).setCellValue("序号");
        row0.createCell(1).setCellValue("姓名");
        row0.createCell(2).setCellValue("年龄");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("张三");
        row1.createCell(2).setCellValue("22");

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("李四");
        row2.createCell(2).setCellValue("20");

        // 5：通过输出流将workbook对象下载到磁盘（D盘itcast80.xlsx）
        FileOutputStream out = new FileOutputStream(new File("D:\\itcast80.xlsx"));
        workbook.write(out);
        out.flush(); // 刷出缓冲区
        out.close();
        workbook.close();
    }
}
