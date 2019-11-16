package com.office.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.office.domain.Luogo;

public interface LuogoDao extends JpaRepository<Luogo, Integer>{
    Luogo getLuogoById(int id);
}