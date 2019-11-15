package com.office.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.office.domain.Utente;;

public interface UtenteDao extends JpaRepository<Utente, Integer>{

    /*
	List<Utente> findByPoints(int points);

	List<Alien> findByPointsGreaterThan(int points);
	

	// YH: Find the records with specific points and the results are sorted by name
	@Query("from utenti where uid=?1 order by aname")
	Utente findByPointsSorted(int id);
	*/
}