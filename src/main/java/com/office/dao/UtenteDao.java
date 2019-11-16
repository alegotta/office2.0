package com.office.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.office.domain.Utente;;

public interface UtenteDao extends JpaRepository<Utente, Integer>{

	Utente getUserByUid(int id);

}