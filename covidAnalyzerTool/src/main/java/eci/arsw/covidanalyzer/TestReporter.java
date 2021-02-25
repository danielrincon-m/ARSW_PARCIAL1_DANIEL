package eci.arsw.covidanalyzer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TestReporter {

    public static final int TRUE_POSITIVE = 0;
    public static final int FALSE_POSITIVE = 1;
    public static final int TRUE_NEGATIVE = 2;
    public static final int FALSE_NEGATIVE = 3;

    private static final String SERVER_URL = "http://localhost:8080/covid/result";

    private static ObjectMapper mapper = new ObjectMapper();

    public static void report(Result result, int type) {

        String complement = "/";
        switch (type) {
            case TRUE_POSITIVE:
                complement += "true-positive";
                break;
            case FALSE_POSITIVE:
                complement += "false-positive";
                break;
            case TRUE_NEGATIVE:
                complement += "true-negative";
                break;
            case FALSE_NEGATIVE:
                complement += "false-negative";
                break;
        }

        try {
            String jsonString = mapper.writeValueAsString(result);
//            System.out.println(jsonString);
            Unirest.post(SERVER_URL + complement)
                    .header("content-type", "application/json")
                    .body(jsonString)
                    .asString();
        } catch (JsonProcessingException | UnirestException e) {
//            System.out.println(":(");
            Logger.getLogger(TestReporter.class.getName()).log(Level.SEVERE, "Unable to report covid test result", e);
        }
    }

}
