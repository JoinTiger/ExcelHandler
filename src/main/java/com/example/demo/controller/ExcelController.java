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
        for(ProduceInfo produceInfo: list) {
            produceInfo.setBatchId(sequenceUtils.getAutoFlowCode());
            for(DeviceInfo deviceInfo: produceInfo.getDeviceInfo()) {
                deviceInfo.setProduceInfo(produceInfo);
            }
        }
        produceInfoRepository.saveAll(list);
        return "parse";
    }

    @PostMapping(value = "/excel/export")
    @ResponseBody
    public String excelExport() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(produceInfoRepository.findAll());
        return json;
    }


}