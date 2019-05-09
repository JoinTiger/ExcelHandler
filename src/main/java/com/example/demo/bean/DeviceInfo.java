package com.example.demo.bean;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = 1385237083886390516L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty("驱动编号")
    private String svNum;

    @JsonProperty("电机编号")
    private String motorNum;

    @JsonProperty("系统Sn号")
    private String macSn;

    @JsonProperty("批次号")
    private String batchId;


    public DeviceInfo() {
    }


    public DeviceInfo(String svNum, String motorNum, String macSn, String batchId) {
        this.svNum = svNum;
        this.motorNum = motorNum;
        this.macSn = macSn;
        this.batchId = batchId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSvNum() {
        return svNum;
    }

    public void setSvNum(String svNum) {
        this.svNum = svNum;
    }

    public String getMotorNum() {
        return motorNum;
    }

    public void setMotorNum(String motorNum) {
        this.motorNum = motorNum;
    }

    public String getMacSn() {
        return macSn;
    }

    public void setMacSn(String macSn) {
        this.macSn = macSn;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
}
