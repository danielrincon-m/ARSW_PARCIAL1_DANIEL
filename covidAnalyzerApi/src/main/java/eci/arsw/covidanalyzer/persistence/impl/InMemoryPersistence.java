package eci.arsw.covidanalyzer.persistence.impl;

import eci.arsw.covidanalyzer.model.Result;
import eci.arsw.covidanalyzer.model.ResultType;
import eci.arsw.covidanalyzer.persistence.CovidAnalyzerPersistence;
import eci.arsw.covidanalyzer.persistence.PersistenceException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component("memPersistence")
public class InMemoryPersistence implements CovidAnalyzerPersistence {
    private final ConcurrentHashMap<String, Result> results = new ConcurrentHashMap<>();

    @Override
    public void addResult(Result result) throws PersistenceException {
        String resultId = result.getId();
        if (!results.containsKey(resultId)) {
            results.put(resultId, result);
        } else {
            throw new PersistenceException("No se pudo agregar el resultado porque ya se encuentra registrado un" +
                    "resultado con el mismo ID.");
        }
    }

    @Override
    public void updateResult(String id, ResultType resultType) throws PersistenceException {
        if (results.containsKey(id)) {
            results.get(id).setResultType(resultType);
        } else {
            throw new PersistenceException("No se pudo actualizar el resultado porque no hay ningún usuario registrado" +
                    "con el ID especificado.");
        }
    }

    @Override
    public List<Result> getResults(ResultType resultType) throws PersistenceException {
        ArrayList<Result> typeResults = new ArrayList<>(results.values());
        typeResults.removeIf((res) -> res.getResultType() != resultType);
        if (typeResults.isEmpty()) {
            throw new PersistenceException("No hay resultados de tipo: " + resultType.name());
        }
        return typeResults;
    }
}
