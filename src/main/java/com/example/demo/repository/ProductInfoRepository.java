package com.example.demo.repository;

import com.example.demo.bean.ProductInfo;

import com.example.demo.bean.ProductSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {

//    String getMacSn();
//    String getBatchId();


    @Query(value="select distinct batch_id as batchId, mac_sn as macSn from product_info", nativeQuery = true)
    public List<ProductSummary> getMacSnAndBatchId();

    @Query
    public ProductInfo findFirstByBatchIdAndMacSn(String batchId, String macSn);



}
