package com.office.controller;

import com.office.dao.BeaconDao;
import com.office.dao.MovimentoDao;
import com.office.domain.Beacon;
import com.office.domain.Movimento;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
public class MovimentoController {
    @Autowired
    MovimentoDao movimentoDao;

    @GetMapping(path = "api/movimenti", produces = { "application/json" })
    public List<Movimento> getUsers() {
        List<Movimento> movimenti = (List<Movimento>) movimentoDao.findAll();
        return movimenti;
    }

    @GetMapping(path="api/movimenti/{id}", produces = { "application/json" })
	public List<Movimento> getUserById(@PathVariable("id") Integer id) {
		List<Movimento> movimentiUser = movimentoDao.findAllById(id);
		return movimentiUser;
	}

    @PostMapping(path="api/addMovimento", consumes={"application/json"})
    public void addUser(@RequestBody Movimento a){
        movimentoDao.saveAndFlush(a);
    }
    
}