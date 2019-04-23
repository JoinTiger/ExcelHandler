package com.example.demo.controller;


import com.example.demo.bean.DeviceInfo;
import com.example.demo.bean.JsonData;
import com.example.demo.bean.ProduceInfo;
import com.example.demo.bean.User;
import com.example.demo.repository.ProduceInfoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.ExcelUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
public class ExcelController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProduceInfoRepository produceInfoRepository;

    @RequestMapping("/poi")
    public String toIndex1(){
        return "poi";
    }
    @RequestMapping("/export")
    public String toIndex2(){
        return "export";
    }
    @RequestMapping("/")
    public String toIndex(){
        return "parse";
    }

    @PostMapping("/excel/up")
    @ResponseBody
    public String fileUp(@RequestBody String json) throws IOException {
        System.out.println(json);
        ObjectMapper mapper = new ObjectMapper();
        List<JsonData> list = mapper.readValue(json, new TypeReference<List<JsonData>>(){});
        ProduceInfo produceInfo = new ProduceInfo();
        for(JsonData jsonData: list) {

            if(jsonData.getMacSn() != null && produceInfo == null) {
                produceInfo = new ProduceInfo();
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setMotorNum(jsonData.getMotorNum());
                deviceInfo.setSvNum(jsonData.getSvNum());
                produceInfo.addDevice(deviceInfo);
                produceInfo.setBatchId(new SimpleDateFormat("yyyy-MM-dd").format(new Date())+new Random(1000));
                produceInfo.setContractNum(jsonData.getContractNum());
                produceInfo.setIpcNum(jsonData.getIpcNum());
                produceInfo.setMacSn(jsonData.getMacSn());
                produceInfo.setNcNum(jsonData.getNcNum());
            } else {
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setMotorNum(jsonData.getMotorNum());
                deviceInfo.setSvNum(jsonData.getSvNum());
                produceInfo.addDevice(deviceInfo);
            }

            produceInfoRepository.save(produceInfo);
        }
        return "parse";
    }

    @GetMapping(value = "/excel/getCh")
    @ResponseBody
    public List<ProduceInfo> getCh(){
        return produceInfoRepository.findAll();
    }

//
//    @RequestMapping("/excel/upload")
//    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
//        if(!ExcelUtils.validateExcel(file.getOriginalFilename())){
//            System.out.println("文件必须是excel!");
//            return null;
//        }
//        long size=file.getSize();
//        if(file.getOriginalFilename()==null || file.getOriginalFilename().equals("") || size==0){
//            System.out.println("文件不能为空");
//            return null;
//        }
//         List list =  ExcelUtils.readExcel2007(file.getInputStream(), new User());
//        userRepository.saveAll(list);
//        return "poi";
//    }
//
//    //生成user表excel
//    @GetMapping(value = "/excel/getUser")
//    public String getUser(HttpServletResponse response) throws Exception{
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        String sheetname = "统计表";
//        XSSFSheet sheet = workbook.createSheet(sheetname);
//        createTitle(workbook,sheet);
//        String fileName = "导出excel例子.xlsx";
//        //FileOutputStream out = new FileOutputStream(fileName);
//        ExcelUtils.generateSheet(userRepository.findAll(), workbook, sheetname, fileName);
//        //生成excel文件
//        buildExcelFile(fileName, workbook);
//        //浏览器下载excel
//        buildExcelDocument(fileName,workbook,response);
//        return "download excel";
//    }
//
    @GetMapping(value = "/excel/getCheckInfo")
    public String getCheckInfo(HttpServletResponse response) throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetname = "统计表";
        //XSSFSheet sheet = workbook.createSheet(sheetname);
       // createTitle(workbook,sheet);
        String fileName = "导出excel例子.xlsx";


        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ExcelUtils.exportExcel(produceInfoRepository.findAll(), ProduceInfo.class,fileName, workbook, out);

        //生成excel文件
        buildExcelFile(fileName, workbook);
        //浏览器下载excel
        buildExcelDocument(fileName,workbook,response);
        return "download excel";
    }

    @GetMapping(value = "/excel/getExample")
    public String getExample(HttpServletResponse response) throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook();
        String sheetname = "统计表";
        XSSFSheet sheet = workbook.createSheet(sheetname);
        createTitle(workbook,sheet);
        String fileName = "导出excel例子.xlsx";
        buildExcelFile(fileName, workbook);
        //浏览器下载excel
        buildExcelDocument(fileName,workbook,response);
        return "download excel";
    }

    //创建表头
    private void createTitle(XSSFWorkbook workbook,XSSFSheet sheet){
        XSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(1,12*256);
        sheet.setColumnWidth(3,17*256);
        //设置为居中加粗
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);

        style.setFont(font);
        XSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("ID");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("用户名");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("显示名");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);
    }
    //生成excel文件
    protected void buildExcelFile(String filename,XSSFWorkbook workbook) throws Exception{
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }
    //浏览器下载excel
    protected void buildExcelDocument(String filename,XSSFWorkbook workbook,HttpServletResponse response) throws Exception{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}