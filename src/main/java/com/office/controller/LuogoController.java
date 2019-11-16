package com.office.controller;

import com.office.dao.LuogoDao;
import com.office.domain.Luogo;

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
public class LuogoController {
    @Autowired
    LuogoDao luogoDao;

    @GetMapping(path = "api/luoghi", produces = { "application/json" })
    public List<Luogo> getUsers() {
        List<Luogo> users = (List<Luogo>) luogoDao.findAll();
        return users;
    }

    @GetMapping(path="api/luogo/{id}", produces = { "application/json" })
	public ResponseEntity<Luogo> getUserById(@PathVariable("id") Integer id) {
		Luogo article = luogoDao.getLuogoById(id);
		return new ResponseEntity<Luogo>(article, HttpStatus.OK);
	}

    @PostMapping(path="api/addLuogo", consumes={"application/json"})
    public void addUser(@RequestBody Luogo a){
        luogoDao.save(a);
    }
}