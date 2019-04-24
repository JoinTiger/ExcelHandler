package com.example.demo.bean;


import com.example.demo.annotation.ExcelAttribute;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class DeviceInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonProperty("驱动编号")
    @ExcelAttribute(name="驱动编号",column="F")
    private String svNum;
    @JsonProperty("电机编号")
    @ExcelAttribute(name="电机编号",column="G")
    private String motorNum;

    @ManyToOne
    @JoinColumn(name = "batchId", referencedColumnName = "batchId")
    private ProduceInfo produceInfo;
    public DeviceInfo(){}

    public String getMotorNum() {
        return motorNum;
    }

    public void setMotorNum(String motorNum) {
        this.motorNum = motorNum;
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



    public ProduceInfo getProduceInfo() {
        return produceInfo;
    }
    @JsonBackReference
    public void setProduceInfo(ProduceInfo produceInfo) {
        this.produceInfo = produceInfo;
    }
}
