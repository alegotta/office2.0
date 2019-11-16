package com.office.controller;

import com.office.dao.UtenteDao;
import com.office.domain.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
public class UtenteController {
    @Autowired
    UtenteDao utenteDao;

    @GetMapping(path = "api/users", produces = { "application/json" })
    public List<Utente> getUsers() {
        List<Utente> users = (List<Utente>) utenteDao.findAll();
        return users;
    }

    @GetMapping(path="api/user/{id}", produces = { "application/json" })
	public ResponseEntity<Utente> getUserById(@PathVariable("id") Integer id) {
		Utente article = utenteDao.getUserByUid(id);
		return new ResponseEntity<Utente>(article, HttpStatus.OK);
	}

    @PostMapping(path="api/addUser", consumes={"application/json"})
    public void addUser(@RequestBody Utente a){
        utenteDao.save(a);
    }
}