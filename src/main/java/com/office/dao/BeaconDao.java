package com.office.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.office.domain.Beacon;

public interface BeaconDao extends JpaRepository<Beacon, Integer>{
    Beacon getBeaconById(int id);
}