package com.office.controller;

import com.office.dao.MovimentoDao;
import com.office.domain.DataBeacon;
import com.office.domain.Movimento;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@RestController
public class MovimentoController {
    @Autowired
    MovimentoDao movimentoDao;

    @GetMapping(path = "api/movimenti", produces = { "application/json" })
    public List<Movimento> getMovimenti() {
        List<Movimento> movimenti = (List<Movimento>) movimentoDao.findAll();
        return movimenti;
    }

    @GetMapping(path = "api/lastMovimento", produces = { "application/json" })
    public Movimento getLastMovimento() {
        Movimento movimento = movimentoDao.findLatest();
        return movimento;
    }


    @GetMapping(path = "api/movimenti/{id}", produces = { "application/json" })
    public List<Movimento> getUserById(@PathVariable("id") Integer id) {
        List<Movimento> movimentiUser = movimentoDao.findAllById(id);
        return movimentiUser;
    }

    @PostMapping(path = "api/addMovimento", consumes = { "application/json" })
    public void addMovimento(@RequestBody String a, @RequestParam("uid") Integer id,
            @RequestParam("timestamp") Long timestamp) throws JsonParseException, JsonMappingException, IOException {

        DataBeacon[] p = new ObjectMapper().readValue(a, DataBeacon[].class);

        // get the position x and y
        double[][] positions = new double[][] { { 0, 0 }, { 3, 0 }, { 1.5, 3 } };
        double[] distances = new double[] { p[0].getDistance(), p[1].getDistance(), p[2].getDistance() };

        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(
                new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        Optimum optimum = solver.solve();

        // the answer
        double[] centroid = optimum.getPoint().toArray();

        // error and geometry information; may throw SingularMatrixException depending
        // the threshold argument provided
        RealVector standardDeviation = optimum.getSigma(0);
        RealMatrix covarianceMatrix = optimum.getCovariances(0);

        System.out.println(centroid.length);
        if (centroid.length >= 2) {
            // create ObjectMapper instance
            Movimento obj = new Movimento();
            obj.setId(id);
            obj.setTimestamp(timestamp);
            obj.setPosX(centroid[0]);
            obj.setPosY(centroid[1]);
            movimentoDao.save(obj);
        }

        for (int i = 0; i < centroid.length; i++)
            System.out.println(centroid[i]);

        
    }

}