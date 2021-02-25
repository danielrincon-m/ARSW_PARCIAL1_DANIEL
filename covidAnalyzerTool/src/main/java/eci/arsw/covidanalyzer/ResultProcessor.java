package eci.arsw.covidanalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ResultProcessor extends Thread {
    private final ArrayList<Integer> indicesToProcess = new ArrayList<>();

    private final ResultAnalyzer resultAnalyzer;
    private final TestReader testReader;
    private AtomicInteger amountOfFilesProcessed;
    private final List<File> resultFiles;

    private boolean paused = false;

    public ResultProcessor(ResultAnalyzer resultAnalyzer, List<File> resultFiles, AtomicInteger amountOfFilesProcessed) {
        this.resultAnalyzer = resultAnalyzer;
        this.resultFiles = resultFiles;
        this.amountOfFilesProcessed = amountOfFilesProcessed;
        testReader = new TestReader();
    }

    @Override
    public void run() {
        processData();
    }

    public void processData() {
        for (int i : indicesToProcess) {
            System.out.println(i + " ->");
            List<Result> results = testReader.readResultsFromFile(resultFiles.get(i));
            for (Result result : results) {
                resultAnalyzer.addResult(result);
            }
            amountOfFilesProcessed.incrementAndGet();
            System.out.println("-> " + i);
            if (paused) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void pauseThread() {
        System.out.println("Pausing...");
        paused = true;
    }

    public void resumeThread() {
        System.out.println("Resuming...");
        paused = false;
        synchronized (this) {
            notifyAll();
        }
    }

    public void aaIndexToProcess(int index) {
        indicesToProcess.add(index);
    }
}
