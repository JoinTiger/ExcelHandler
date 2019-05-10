package com.example.demo.repository;

import com.example.demo.bean.DeviceInfo;
import com.example.demo.bean.ProductSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceInfoRepository extends JpaRepository<DeviceInfo,String> {


    @Query
    public List<DeviceInfo> findByBatchIdAndMacSn(String batchId, String macSn);

    //    @JsonProperty("驱动编号")
//    private String svNum;
//
//    @JsonProperty("电机编号")
//    private String motorNum;

    @Query(value = "select distinct batch_id as batchId, mac_sn as macSn from device_info" + " where"
            + " if(ISNULL(?1) || length(trim(?1)) < 1, 1 = 1, sv_num = ?1) and"
            + " if(ISNULL(?2) || length(trim(?2)) < 1, 1 = 1, motor_num = ?2)"
            ,nativeQuery = true)
    public List<ProductSummary> getMacSnAndBatchId(String svNum, String motorNum);

}
