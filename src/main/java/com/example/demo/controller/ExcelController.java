package com.example.demo.controller;

import com.example.demo.bean.DeviceInfo;
import com.example.demo.bean.ProduceInfo;
import com.example.demo.redis.SequenceUtils;
import com.example.demo.repository.ProduceInfoRepository;
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
    private ProduceInfoRepository produceInfoRepository;

    @Autowired
    private SequenceUtils sequenceUtils;

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
    public String excelExport(@RequestBody String json) throws IOException {
        System.out.println(json);
        ObjectMapper mapper = new ObjectMapper();
        List<ProduceInfo> list = mapper.readValue(json, new TypeReference<List<ProduceInfo>>(){});

        for(ProduceInfo pro : list) {
            System.out.print("product-macSn:" + pro.getMacSn() + "          ");
            System.out.print("product-batchId:" + pro.getBatchId());
            System.out.println();

            for(DeviceInfo deviceInfo1 : pro.getDeviceInfos()) {
                System.out.print("         sub-macSn:" + deviceInfo1.getMacSn());
                System.out.print("         sub-batchId:" + deviceInfo1.getBatchId());
                System.out.print("         sub-motorNUm:" + deviceInfo1.getMotorNum());
            }

            System.out.println();

        }

        return "parse";
    }

    @GetMapping(value = "/excel/export")
    @ResponseBody
    public String getProductInfo() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<ProduceInfo> pros = new ArrayList<>();

        ProduceInfo produceInfo1 = new ProduceInfo();
        ProduceInfo produceInfo2 = new ProduceInfo();

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




        produceInfo1.setBatchId("aa11111");
        produceInfo1.setMacSn("aa11111");
        produceInfo1.getDeviceInfos().add(deviceInfo1);
        produceInfo1.getDeviceInfos().add(deviceInfo2);
        produceInfo1.getDeviceInfos().add(deviceInfo3);


        produceInfo2.setBatchId("bb11111");
        produceInfo2.setMacSn("bb11111");
        produceInfo2.getDeviceInfos().add(deviceInfo4);
        produceInfo2.getDeviceInfos().add(deviceInfo5);

        pros.add(produceInfo1);
        pros.add(produceInfo2);

        String json = mapper.writeValueAsString(pros);
        return json;
    }







}