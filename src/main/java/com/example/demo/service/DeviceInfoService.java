package com.example.demo.service;


import com.example.demo.bean.DeviceInfo;
import com.example.demo.bean.ProductInfo;
import com.example.demo.redis.SequenceUtils;
import com.example.demo.repository.DeviceInfoRepository;
import com.example.demo.repository.ProductInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DeviceInfoService {

    @Autowired
    private DeviceInfoRepository deviceInfoRepository;

    @Transactional
    public void save(DeviceInfo deviceInfo) {
        deviceInfoRepository.save(deviceInfo);
    }

    @Transactional
    public void saveAll(List<DeviceInfo> deviceInfoList) {
        deviceInfoRepository.saveAll(deviceInfoList);
    }




}
