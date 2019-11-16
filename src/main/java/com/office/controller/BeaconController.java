package com.office.controller;

import com.office.dao.BeaconDao;
import com.office.domain.Beacon;

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
public class BeaconController {
    @Autowired
    BeaconDao beaconDao;

    @GetMapping(path = "api/beacons", produces = { "application/json" })
    public List<Beacon> getUsers() {
        List<Beacon> users = (List<Beacon>) beaconDao.findAll();
        return users;
    }

    @GetMapping(path="api/beacon/{id}", produces = { "application/json" })
	public ResponseEntity<Beacon> getUserById(@PathVariable("id") Integer id) {
		Beacon article = beaconDao.getBeaconById(id);
		return new ResponseEntity<Beacon>(article, HttpStatus.OK);
	}

    @PostMapping(path="api/addBeacon", consumes={"application/json"})
    public void addUser(@RequestBody Beacon a){
        beaconDao.save(a);
    }
}