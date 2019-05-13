package com.example.demo.repository;

import com.example.demo.bean.ProductInfo;

import com.example.demo.bean.ProductSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {

//    String getMacSn();
//    String getBatchId();


    @Query(value = "select distinct batch_id as batchId, mac_sn as macSn from product_info", nativeQuery = true)
    public List<ProductSummary> getMacSnAndBatchId();

    @Query
    public ProductInfo findFirstByBatchIdAndMacSn(String batchId, String macSn);





    /**
     *
     * @param ncNum
     * @param ipcNum
     * @param contractNum
     * @param macSn
     * @param batchId
     * @param startTime '2017-1-1 00:00:00'
     * @param endTime'2017-1-1 00:00:00'
     * @return
     */

    @Query(value = "select distinct t.batchId as batchId, t.macSn as macSn from"
            + " ( select distinct batch_id as batchId, mac_sn as macSn from product_info" + " where"
            + " if(ISNULL(?1) || length(trim(?1)) < 1, 1 = 1, nc_num = ?1) and"
            + " if(ISNULL(?2) || length(trim(?2)) < 1, 1 = 1, ipc_num = ?2) and"

            + " if(ISNULL(?3) || length(trim(?3)) < 1, 1 = 1, contract_num = ?3) and"
            + " if(ISNULL(?4) || length(trim(?4)) < 1, 1 = 1, mac_sn = ?4) and"
            + " if(ISNULL(?5) || length(trim(?5)) < 1, 1 = 1, batch_id = ?5) and"

            + " if(ISNULL(?6) || length(trim(?6)) < 1, 1 = 1, time >= ?6) and"
            + " if(ISNULL(?7) || length(trim(?7)) < 1, 1 = 1, time <= ?7) ) t"
            + " join"
            
            + " ("
            + " select distinct batch_id as batchId, mac_sn as macSn from device_info" + " where"
            + " if(ISNULL(?8) || length(trim(?8)) < 1, 1 = 1, sv_num = ?8) and"
            + " if(ISNULL(?9) || length(trim(?9)) < 1, 1 = 1, motor_num = ?9)"
            + ") k "
            + " on"
            + "(t.batchId = k.batchId and t.macSn = k.macSn)"
            ,nativeQuery = true)
    public List<ProductSummary> getMacSnAndBatchId(String ncNum, String ipcNum,
                                                   String contractNum, String macSn, String batchId,
                                                   String startTime, String endTime,
                                                   String svNum, String motorNum);


    @Query(value = "select distinct batch_id as batchId, mac_sn as macSn from product_info" + " where"
            + " if(ISNULL(?1) || length(trim(?1)) < 1, 1 = 1, nc_num = ?1) and"
            + " if(ISNULL(?2) || length(trim(?2)) < 1, 1 = 1, ipc_num = ?2) and"

            + " if(ISNULL(?3) || length(trim(?3)) < 1, 1 = 1, contract_num = ?3) and"
            + " if(ISNULL(?4) || length(trim(?4)) < 1, 1 = 1, mac_sn = ?4) and"
            + " if(ISNULL(?5) || length(trim(?5)) < 1, 1 = 1, batch_id = ?5) and"

            + " if(ISNULL(?6) || length(trim(?6)) < 1, 1 = 1, time >= ?6) and"
            + " if(ISNULL(?7) || length(trim(?7)) < 1, 1 = 1, time <= ?7)", nativeQuery = true)
    public List<ProductSummary> getMacSnAndBatchId(String ncNum, String ipcNum,
                                                   String contractNum, String macSn, String batchId,
                                                   String startTime, String endTime);


}
