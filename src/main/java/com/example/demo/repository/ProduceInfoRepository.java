package com.example.demo.repository;

import com.example.demo.bean.ProduceInfo;

import org.springframework.data.jpa.repository.JpaRepository;



public interface ProduceInfoRepository extends JpaRepository<ProduceInfo, Long> {
}
