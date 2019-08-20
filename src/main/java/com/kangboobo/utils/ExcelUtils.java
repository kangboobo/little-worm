/*
 *
 *                  Copyright 2017 Crab2Died
 *                     All rights reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Browse for more information ：
 * 1) https://gitee.com/Crab2Died/Excel4J
 * 2) https://github.com/Crab2died/Excel4J
 *
 */

package com.kangboobo.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Excel4J的主要操作工具类
 * <p>
 * 主要包含6大操作类型,并且每个类型都配有一个私有handler：<br>
 * <p>
 * 另外列举了部分常用的参数格式的方法(不同参数的排列组合实在是太多,没必要完全列出)
 * 如遇没有自己需要的参数类型的方法,可通过最全的方法来自行变换<br>
 * <p>
 * 详细用法请关注: https://gitee.com/Crab2Died/Excel4J
 *
 * Created by kangboobo on 2019/08/15.
 */
public final class ExcelUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 单例模式
     * 通过{@link ExcelUtils#getInstance()}获取对象实例
     */
    private static volatile ExcelUtils excelUtils;

    private ExcelUtils() {
    }

    /**
     * 双检锁保证单例
     */
    public static ExcelUtils getInstance() {
        if (null == excelUtils) {
            synchronized (ExcelUtils.class) {
                if (null == excelUtils) {
                    excelUtils = new ExcelUtils();
                }
            }
        }
        return excelUtils;
    }

    /**
     * 导出动态列excel文件
     *
     * @param titles
     * @param values
     * @param fileName
     * @param sheetName
     * @return
     * @throws IOException
     */
    public static File exportDyExcelFile(List<String> titles, List<List<String>> values, String fileName, String sheetName, String filePath) throws IOException {
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String realTitle = replaceStr(fileName);
        realTitle = realTitle + ".xlsx";
        File file = new File(filePath, realTitle);
        if (!file.exists()) {
            file.createNewFile();
        }
        String newSheetName = sheetName;
        if (StringUtils.isEmpty(sheetName)) {
            newSheetName = "sheet1";
        } else if (sheetName.contains("/")) {
            newSheetName = sheetName.replaceAll("/", "_");
        }
        try {
            OutputStream os = new FileOutputStream(file);
            SXSSFWorkbook wb = new SXSSFWorkbook(1000);
            SXSSFSheet sheet = wb.createSheet(newSheetName);// 创建一个sheet
            sheet.setDefaultColumnWidth(20);//默认列宽20
            //首行样式
            CellStyle styleHeader = wb.createCellStyle();// 设置这些样式
            styleHeader.setAlignment(HorizontalAlignment.CENTER);//水平居中
            styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            styleHeader.setFillForegroundColor(IndexedColors.VIOLET.getIndex());//颜色
            styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);//必填
            Font font = wb.createFont();
            font.setColor(IndexedColors.WHITE.index);
            font.setBold(true);
            styleHeader.setFont(font);//字体

            CellStyle lightOrangeStyle = wb.createCellStyle();// 设置这些样式
            lightOrangeStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
            lightOrangeStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            Font lightOrangeFront = wb.createFont();
            lightOrangeFront.setColor(IndexedColors.LIGHT_ORANGE.index);
            lightOrangeStyle.setFont(lightOrangeFront);//字体

            CellStyle greenStyle = wb.createCellStyle();// 设置这些样式
            greenStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
            greenStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            Font greenFont = wb.createFont();
            greenFont.setColor(IndexedColors.GREEN.index);
            greenStyle.setFont(greenFont);//字体

            CellStyle blackStyle = wb.createCellStyle();// 设置这些样式
            blackStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
            blackStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            Font blackFont = wb.createFont();
            blackFont.setColor(IndexedColors.BLACK.index);
            blackStyle.setFont(blackFont);//字体

            // 创建第一行表头
            Row titleRow = sheet.createRow(0);
            for (int i = 0; i < titles.size(); i++) {
                Cell titleCell = titleRow.createCell(i);
                titleCell.setCellStyle(styleHeader);
                titleCell.setCellValue(titles.get(i));
            }
            int rownum = 0;
            for (int i = 0; i < values.size(); i++) {
                List<String> valuesList = values.get(i);
                Row valuesRow = sheet.createRow(i + 1);
                for (int j = 0; j < valuesList.size(); j++) {
                    Cell valueCell = valuesRow.createCell((short) j);
                    if (valuesList.get(j).equals("不合格")) {
                        valueCell.setCellStyle(lightOrangeStyle);
                    } else if (valuesList.get(j).equals("合格")) {
                        valueCell.setCellStyle(greenStyle);
                    } else {
                        valueCell.setCellStyle(blackStyle);
                    }
                    valueCell.setCellValue(new XSSFRichTextString(valuesList.get(j)));
                }
                if (rownum % 100 == 0) {
                    // retain 100 last rows and flush all others
                    ((SXSSFSheet) sheet).flushRows(100);
                }
                rownum++;
            }
            wb.write(os);// 写文件
            os.close();// 关闭输出流
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return file;
    }


    /**
     * 替换特殊字符
     *
     * @param fileName
     * @return
     */
    public static String replaceStr(String fileName) {
        // 替换所有名称的特殊字符
        String regEx = "[\\\\\\/:*?\"\\<\\>\\|]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(fileName);
        return m.replaceAll(" ");
    }

    /**
     * 导出动态列excel文件
     *
     * @param titles
     * @param values
     * @param fileName
     * @param sheetName
     * @return
     * @throws IOException
     */
    public static File exportDynamicExcelFile(List<String> titles, List<List<String>> values, String fileName, String sheetName, String filePath) throws IOException {
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String realTitle = replaceStr(fileName);
        realTitle = realTitle + ".xlsx";
        File file = new File(filePath, realTitle);
        if (!file.exists()) {
            file.createNewFile();
        }
        String newSheetName = sheetName;
        if (StringUtils.isEmpty(sheetName)) {
            newSheetName = "sheet1";
        } else if (sheetName.contains("/")) {
            newSheetName = sheetName.replaceAll("/", "_");
        }
        try {
            OutputStream os = new FileOutputStream(file);
            SXSSFWorkbook wb = new SXSSFWorkbook(1000);
            SXSSFSheet sheet = wb.createSheet(newSheetName);// 创建一个sheet
            sheet.setDefaultColumnWidth(20);//默认列宽20
            //首行样式
            CellStyle styleHeader = wb.createCellStyle();// 设置这些样式
            styleHeader.setAlignment(HorizontalAlignment.CENTER);//水平居中
            styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            styleHeader.setFillForegroundColor(IndexedColors.VIOLET.getIndex());//颜色
            styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);//必填
            Font font = wb.createFont();
            font.setColor(IndexedColors.WHITE.index);
            font.setBold(true);
            styleHeader.setFont(font);//字体

            CellStyle styleLine = wb.createCellStyle();// 设置这些样式
            styleLine.setAlignment(HorizontalAlignment.CENTER);//水平居中
            styleLine.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            // 创建第一行表头
            Row titleRow = sheet.createRow(0);
            for (int i = 0; i < titles.size(); i++) {
                Cell titleCell = titleRow.createCell(i);
                titleCell.setCellStyle(styleHeader);
                titleCell.setCellValue(titles.get(i));
            }
            int rownum = 0;
            for (int i = 0; i < values.size(); i++) {
                List<String> valuesList = values.get(i);
                Row valuesRow = sheet.createRow(i + 1);
                for (int j = 0; j < valuesList.size(); j++) {
                    Cell valueCell = valuesRow.createCell(j);
                    valueCell.setCellStyle(styleLine);
                    valueCell.setCellValue(new XSSFRichTextString(valuesList.get(j)));
                }
                if (rownum % 100 == 0) {
                    // retain 100 last rows and flush all others
                    ((SXSSFSheet) sheet).flushRows(100);
                }
                rownum++;
            }
            wb.write(os);// 写文件
            os.close();// 关闭输出流
        } catch (Exception e) {
            logger.error("exportDynamicExcelFile error, fileName={}, error={}", fileName, e.getMessage());
        }
        return file;
    }

}
