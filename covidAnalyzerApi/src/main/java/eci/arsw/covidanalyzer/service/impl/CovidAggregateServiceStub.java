package eci.arsw.covidanalyzer.service.impl;

import eci.arsw.covidanalyzer.model.Result;
import eci.arsw.covidanalyzer.model.ResultType;
import eci.arsw.covidanalyzer.service.ICovidAggregateService;

import java.util.UUID;

public class CovidAggregateServiceStub implements ICovidAggregateService {
    @Override
    public boolean aggregateResult(Result result, ResultType type) {
        return false;
    }

    @Override
    public boolean getResult(ResultType type) {
        return false;
    }

    @Override
    public void upsertPersonWithMultipleTests(UUID id, ResultType type) {

    }
}
