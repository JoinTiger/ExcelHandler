package com.example.demo.repository;

import com.example.demo.bean.DeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceInfoRepository extends JpaRepository<DeviceInfo,String> {
}
