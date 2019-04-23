package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonData {
    private long id;
    @JsonProperty("批次号")
    private String batchId;
    @JsonProperty("系统Sn号")
    private String macSn;
    @JsonProperty("数控编号")
    private String ncNum;
    @JsonProperty("ipc编号")
    private String ipcNum;
    @JsonProperty("合同编号")
    private String contractNum;

    @JsonProperty("驱动编号")
    private String svNum;
    @JsonProperty("电机编号")
    private String motorNum;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getMacSn() {
        return macSn;
    }

    public void setMacSn(String macSn) {
        this.macSn = macSn;
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
}
