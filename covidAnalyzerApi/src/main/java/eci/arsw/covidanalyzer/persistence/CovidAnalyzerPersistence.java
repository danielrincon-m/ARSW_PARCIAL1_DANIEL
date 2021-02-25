package eci.arsw.covidanalyzer.persistence;

import eci.arsw.covidanalyzer.model.Result;
import eci.arsw.covidanalyzer.model.ResultType;

import java.util.List;

public interface CovidAnalyzerPersistence {
    void addResult(Result result) throws PersistenceException;

    void updateResult(String id, ResultType resultType) throws PersistenceException;

    List<Result> getResults(ResultType resultType) throws PersistenceException;
}
