package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProduceInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty("数控编号")
    private String ncNum;
    @JsonProperty("ipc编号")
    private String ipcNum;
    @JsonProperty("合同编号")
    private String contractNum;

    @JsonProperty("系统Sn号")
    private String macSn;
    @JsonProperty("批次号")
    private String batchId;

    @JsonProperty("子表")
    @Transient
    private List<DeviceInfo> deviceInfos = new ArrayList<>();

    public ProduceInfo() {
    }


    public ProduceInfo(String ncNum, String ipcNum, String contractNum, String macSn, String batchId, List<DeviceInfo> deviceInfos) {
        this.ncNum = ncNum;
        this.ipcNum = ipcNum;
        this.contractNum = contractNum;
        this.macSn = macSn;
        this.batchId = batchId;
        this.deviceInfos = deviceInfos;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNcNum() {
        return ncNum;
    }

    public void setNcNum(String ncNum) {
        this.ncNum = ncNum;
    }

    public String getIpcNum() {
        return ipcNum;
    }

    public void setIpcNum(String ipcNum) {
        this.ipcNum = ipcNum;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
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

    public List<DeviceInfo> getDeviceInfos() {
        return deviceInfos;
    }

    public void setDeviceInfos(List<DeviceInfo> deviceInfos) {
        this.deviceInfos = deviceInfos;
    }
}