package eci.arsw.covidanalyzer.service;

import eci.arsw.covidanalyzer.model.Result;
import eci.arsw.covidanalyzer.model.ResultType;
import eci.arsw.covidanalyzer.persistence.PersistenceException;

import java.util.List;

public interface ICovidAggregateService {

    /**
     * Add a new result into the specified result type storage.
     *
     * @param result
     * @param type
     * @return
     */
    void addResult(Result result, ResultType type) throws PersistenceException;

    /**
     * Get all the results for the specified result type.
     *
     * @param type
     * @return
     */
    List<Result> getResults(ResultType type) throws PersistenceException;

    /**
     * 
     * @param id
     * @param type
     */
    void upsertPersonWithMultipleTests(String id, ResultType type) throws PersistenceException;

    String getJsonResults(List<Result> results) throws PersistenceException;
}
