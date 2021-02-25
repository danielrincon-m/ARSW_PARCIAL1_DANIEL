package eci.arsw.covidanalyzer.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eci.arsw.covidanalyzer.model.Result;
import eci.arsw.covidanalyzer.model.ResultType;
import eci.arsw.covidanalyzer.persistence.CovidAnalyzerPersistence;
import eci.arsw.covidanalyzer.persistence.PersistenceException;
import eci.arsw.covidanalyzer.service.ICovidAggregateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("covidServices")
public class CovidAggregateServiceStub implements ICovidAggregateService {

    @Autowired
    @Qualifier("memPersistence")
    private CovidAnalyzerPersistence persistence;

    @Override
    public void addResult(Result result, ResultType type) throws PersistenceException {
        result.setResultType(type);
        persistence.addResult(result);
    }

    @Override
    public List<Result> getResults(ResultType type) throws PersistenceException {
        return persistence.getResults(type);
    }

    @Override
    public void upsertPersonWithMultipleTests(String id, ResultType type) throws PersistenceException {
        persistence.updateResult(id, type);
    }

    @Override
    public String getJsonResults(List<Result> results) throws PersistenceException {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(results);
    }
}
