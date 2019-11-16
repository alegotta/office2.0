package com.office.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.office.domain.Movimento;

public interface MovimentoDao extends JpaRepository<Movimento, Integer>{

    List<Movimento> findAllById(int id);
    
    @Query(value = "Select * from movimenti order by timestamp desc limit to 1 ", nativeQuery = true)
    Movimento findLatest();
}