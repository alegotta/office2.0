package com.office.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.office.domain.Movimento;

public interface MovimentoDao extends JpaRepository<Movimento, Integer>{

	List<Movimento> findAllById(int id);
}