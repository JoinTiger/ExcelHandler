package com.example.demo.bean;

import com.example.demo.annotation.ExcelAttribute;
import com.example.demo.annotation.ExcelElement;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProduceInfo implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    @JsonProperty("批次号")
    @ExcelAttribute(name="批次号",column="A")
    private String batchId;
    @JsonProperty("系统Sn号")
    @ExcelAttribute(name="系统Sn号",column="B")
    private String macSn;
    @JsonProperty("数控编号")
    @ExcelAttribute(name="数控编号",column="C")
    private String ncNum;
    @JsonProperty("ipc编号")
    @ExcelAttribute(name="ipc编号",column="D")
    private String ipcNum;
    @JsonProperty("合同编号")
    @ExcelAttribute(name="合同编号",column="E")
    private String contractNum;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="produceInfo" )
    @ExcelElement
    private List<DeviceInfo> deviceInfos = new ArrayList<>();

    public void addDevice(DeviceInfo deviceInfo) {
        deviceInfos.add(deviceInfo);
    }


    public ProduceInfo() {
    }

    public ProduceInfo(long id, String batchId, String macSn, String ncNum, String ipcNum, String contractNum) {
        this.id = id;
        this.batchId = batchId;
        this.macSn = macSn;
        this.ncNum = ncNum;
        this.ipcNum = ipcNum;
        this.contractNum = contractNum;
    }

    @Override
    public String toString() {
        return "ProduceInfo{" +
                "id=" + id +
                ", batchId='" + batchId + '\'' +
                ", macSn='" + macSn + '\'' +
                ", ncNum='" + ncNum + '\'' +
                ", ipcNum='" + ipcNum + '\'' +
                ", contractNum='" + contractNum + '\'' +
                ", deviceInfo=" + deviceInfos +
                '}';
    }

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

    public List<DeviceInfo> getDeviceInfo() {
        return deviceInfos;
    }

    public void setDeviceInfo(List<DeviceInfo> deviceInfo) {
        this.deviceInfos = deviceInfo;
    }
}