package com.office.controller;

import com.office.dao.UtenteDao;
import com.office.domain.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@RestController
public class UtenteController{
    @Autowired
    UtenteDao utenteDao;

    @RequestMapping(path="api/users", produces={"application/json"})
    public List<Utente> getUsers(){
        List<Utente> users = (List<Utente>) utenteDao.findAll();
        return users;
    }

    @PostMapping(path="api/addUser", consumes={"application/json"})
    public void addUser(@RequestBody Utente a){
        utenteDao.save(a);
    }
}