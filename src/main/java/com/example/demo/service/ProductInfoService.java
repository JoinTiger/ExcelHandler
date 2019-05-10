package com.example.demo.service;


import com.example.demo.bean.DeviceInfo;
import com.example.demo.bean.ProductInfo;
import com.example.demo.bean.ProductSummary;
import com.example.demo.redis.SequenceUtils;
import com.example.demo.repository.DeviceInfoRepository;
import com.example.demo.repository.ProductInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductInfoService {


    @Autowired
    private SequenceUtils sequenceUtils;

    @Autowired
    private DeviceInfoRepository deviceInfoRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Transactional
    public void save(ProductInfo productInfo) {
        productInfoRepository.save(productInfo);
    }


    @Transactional
    public void saveAll(List<ProductInfo> productInfoList) {
        productInfoRepository.saveAll(productInfoList);
    }


    @Transactional
    public void saveProductAndDevice(List<ProductInfo> list) {
        for(ProductInfo pro : list) {


            String autoFlowCode = sequenceUtils.getAutoFlowCode();
            Date date = new Date();

            pro.setBatchId(autoFlowCode);
            pro.setTime(date);

            productInfoRepository.save(pro);

            for(DeviceInfo deviceInfo1 : pro.getDeviceInfos()) {
                deviceInfo1.setBatchId(autoFlowCode);
                deviceInfoRepository.save(deviceInfo1);
            }
        }

    }


    @Transactional
    public List<ProductInfo> getAllProduct() {
        List<ProductInfo> ret = new ArrayList<>();
        List<ProductSummary> productSummaries = productInfoRepository.getMacSnAndBatchId();

        for(ProductSummary productSummary : productSummaries) {
            String batchId = productSummary.getBatchId();
            String macSn = productSummary.getMacSn();

            ProductInfo productInfo = productInfoRepository.findFirstByBatchIdAndMacSn(batchId, macSn);

            List<DeviceInfo> deviceInfos = deviceInfoRepository.findByBatchIdAndMacSn(batchId, macSn);

            productInfo.getDeviceInfos().addAll(deviceInfos);

            Date date = new Date();
            productInfo.setTime(date);
            ret.add(productInfo);
        }

        return ret;
    }

    @Transactional
    public List<ProductInfo> getSelectProduct(String ncNum, String ipcNum,
                                              String contractNum, String macSn, String batchId,
                                              String startTime, String endTime,
                                              String svNum, String motorNum) {
        List<ProductInfo> ret = new ArrayList<>();

        List<ProductSummary> productSummaries = productInfoRepository.getMacSnAndBatchId(ncNum, ipcNum, contractNum, macSn, batchId, startTime, endTime);
        List<ProductSummary> devSummaries = deviceInfoRepository.getMacSnAndBatchId(svNum, motorNum);


        productSummaries.retainAll(devSummaries);

        for(ProductSummary productSummary : productSummaries) {
            String sBatchId = productSummary.getBatchId();
            String sMacSn = productSummary.getMacSn();

            ProductInfo productInfo = productInfoRepository.findFirstByBatchIdAndMacSn(sBatchId, sMacSn);

            List<DeviceInfo> deviceInfos = deviceInfoRepository.findByBatchIdAndMacSn(sBatchId, sMacSn);

            productInfo.getDeviceInfos().addAll(deviceInfos);

            ret.add(productInfo);
        }

        return ret;
    }



}
