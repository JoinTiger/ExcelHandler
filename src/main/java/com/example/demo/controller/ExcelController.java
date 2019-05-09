package com.example.demo.controller;

import com.example.demo.bean.DeviceInfo;
import com.example.demo.bean.ProductInfo;
import com.example.demo.redis.SequenceUtils;
import com.example.demo.repository.ProductInfoRepository;
import com.example.demo.service.DeviceInfoService;
import com.example.demo.service.ProductInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExcelController {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private DeviceInfoService deviceInfoService;



    @RequestMapping("/export")
    public String exportPage(){
        return "export";
    }
    @RequestMapping("/")
    public String importPage(){
        return "import";
    }


    @PostMapping("/excel/import")
    @ResponseBody
    public String excelImport(@RequestBody String json) throws IOException {
        //System.out.println(json);
        ObjectMapper mapper = new ObjectMapper();
        List<ProductInfo> productInfos = mapper.readValue(json, new TypeReference<List<ProductInfo>>(){});

        productInfoService.saveProductAndDevice(productInfos);

        return "success";
    }


    @GetMapping(value = "/excel/export")
    @ResponseBody
    public String excelExport() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<ProductInfo> productInfos = productInfoService.getAllProduct();

        String json = mapper.writeValueAsString(productInfos);

        return json;
    }







    @GetMapping(value = "/getJson")
    @ResponseBody
    public String getProductInfo() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<ProductInfo> pros = new ArrayList<>();

        ProductInfo productInfo1 = new ProductInfo();
        ProductInfo productInfo2 = new ProductInfo();

        DeviceInfo deviceInfo1 = new DeviceInfo();
        DeviceInfo deviceInfo2 = new DeviceInfo();
        DeviceInfo deviceInfo3 = new DeviceInfo();

        DeviceInfo deviceInfo4 = new DeviceInfo();
        DeviceInfo deviceInfo5 = new DeviceInfo();


        deviceInfo1.setBatchId("aa11111");
        deviceInfo1.setMacSn("aa11111");
        deviceInfo1.setMotorNum("a32");
        deviceInfo1.setSvNum("adafa");

        deviceInfo2.setBatchId("aa11111");
        deviceInfo2.setMacSn("aa11111");
        deviceInfo2.setMotorNum("b32");
        deviceInfo2.setSvNum("bdafa");


        deviceInfo3.setBatchId("aa11111");
        deviceInfo3.setMacSn("aa11111");
        deviceInfo3.setMotorNum("c32");
        deviceInfo3.setSvNum("cdafa");


        deviceInfo4.setBatchId("bb11111");
        deviceInfo4.setMacSn("bb11111");
        deviceInfo4.setMotorNum("d23");
        deviceInfo4.setSvNum("d3434");


        deviceInfo5.setBatchId("bb11111");
        deviceInfo5.setMacSn("bb11111");
        deviceInfo5.setMotorNum("e23");
        deviceInfo5.setSvNum("e3434");




        productInfo1.setBatchId("aa11111");
        productInfo1.setMacSn("aa11111");
        productInfo1.getDeviceInfos().add(deviceInfo1);
        productInfo1.getDeviceInfos().add(deviceInfo2);
        productInfo1.getDeviceInfos().add(deviceInfo3);


        productInfo2.setBatchId("bb11111");
        productInfo2.setMacSn("bb11111");
        productInfo2.getDeviceInfos().add(deviceInfo4);
        productInfo2.getDeviceInfos().add(deviceInfo5);

        pros.add(productInfo1);
        pros.add(productInfo2);

        String json = mapper.writeValueAsString(pros);
        return json;
    }







}