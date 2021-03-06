package eci.arsw.covidanalyzer;

import eci.arsw.covidanalyzer.model.Result;
import eci.arsw.covidanalyzer.model.ResultType;
import eci.arsw.covidanalyzer.persistence.PersistenceException;
import eci.arsw.covidanalyzer.service.ICovidAggregateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
public class CovidAggregateController {

    @Autowired
    @Qualifier("covidServices")
    ICovidAggregateService covidAggregateService;

    @RequestMapping(value = "/covid/result/true-positive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTruePositiveResult(@RequestBody Result result) {
        try {
            covidAggregateService.addResult(result, ResultType.TRUE_POSITIVE);
            return new ResponseEntity<>("Se ha agregado correctamente el registro.", HttpStatus.ACCEPTED);
        } catch (PersistenceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/covid/result/true-negative", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTrueNegativeResult(@RequestBody Result result) {
        try {
            covidAggregateService.addResult(result, ResultType.TRUE_NEGATIVE);
            return new ResponseEntity<>("Se ha agregado correctamente el registro.", HttpStatus.ACCEPTED);
        } catch (PersistenceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/covid/result/false-positive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFalsePositiveResult(@RequestBody Result result) {
        try {
            covidAggregateService.addResult(result, ResultType.FALSE_POSITIVE);
            return new ResponseEntity<>("Se ha agregado correctamente el registro.", HttpStatus.ACCEPTED);
        } catch (PersistenceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/covid/result/false-negative", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFalseNegativeResult(@RequestBody Result result) {
        try {
            covidAggregateService.addResult(result, ResultType.FALSE_NEGATIVE);
            return new ResponseEntity<>("Se ha agregado correctamente el registro.", HttpStatus.ACCEPTED);
        } catch (PersistenceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //TODO: Implemente todos los metodos GET que hacen falta.

    @RequestMapping(value = "/covid/result/true-positive", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTruePositiveResult() {
        try {
            List<Result> results = covidAggregateService.getResults(ResultType.TRUE_POSITIVE);
            return new ResponseEntity<>(covidAggregateService.getJsonResults(results), HttpStatus.OK);
        } catch (PersistenceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/covid/result/true-negative", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTrueNegativeResult() {
        try {
            List<Result> results = covidAggregateService.getResults(ResultType.TRUE_NEGATIVE);
            return new ResponseEntity<>(covidAggregateService.getJsonResults(results), HttpStatus.OK);
        } catch (PersistenceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/covid/result/false-positive", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFalsePositiveResult() {
        try {
            List<Result> results = covidAggregateService.getResults(ResultType.FALSE_POSITIVE);
            return new ResponseEntity<>(covidAggregateService.getJsonResults(results), HttpStatus.OK);
        } catch (PersistenceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/covid/result/false-negative", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFalseNegativeResult() {
        try {
            List<Result> results = covidAggregateService.getResults(ResultType.FALSE_NEGATIVE);
            return new ResponseEntity<>(covidAggregateService.getJsonResults(results), HttpStatus.OK);
        } catch (PersistenceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/covid/result/persona/{id}/{resultType}", method = RequestMethod.PUT)
    public ResponseEntity<?> savePersonaWithMultipleTests(@PathVariable String id, @PathVariable String resultType) {
        try {
            covidAggregateService.upsertPersonWithMultipleTests(id, ResultType.valueOf(resultType.toUpperCase(Locale.ROOT)));
            return new ResponseEntity<>("Se ha actualizado el registro exitosamente.", HttpStatus.ACCEPTED);
        } catch (PersistenceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}