package com.example.demo.repository;

import com.example.demo.bean.DeviceInfo;
import com.example.demo.bean.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceInfoRepository extends JpaRepository<DeviceInfo,String> {


    @Query
    public List<DeviceInfo> findByBatchIdAndMacSn(String batchId, String macSn);


}
