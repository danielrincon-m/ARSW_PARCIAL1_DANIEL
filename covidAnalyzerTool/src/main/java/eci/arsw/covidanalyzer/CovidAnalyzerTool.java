package eci.arsw.covidanalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Camel Application
 */
public class CovidAnalyzerTool {
    private static final int PROCESSOR_NUMBER = 5;

    private final ArrayList<ResultProcessor> threads = new ArrayList<>();

    private final ResultAnalyzer resultAnalyzer;
    private final AtomicInteger amountOfFilesProcessed;

    private int amountOfFilesTotal;

    public CovidAnalyzerTool() {
        resultAnalyzer = new ResultAnalyzer();
        amountOfFilesProcessed = new AtomicInteger();
    }

    public void processResultData() {
        amountOfFilesProcessed.set(0);
        List<File> resultFiles = getResultFileList();
        amountOfFilesTotal = resultFiles.size();

        for (int i = 0; i < PROCESSOR_NUMBER; i++) {
            threads.add(new ResultProcessor(resultAnalyzer, resultFiles, amountOfFilesProcessed));
        }
        for (int i = 0; i < amountOfFilesTotal; i++) {
            threads.get(i % PROCESSOR_NUMBER).aaIndexToProcess(i);
        }
        threads.forEach(Thread::start);
        try {
            for (ResultProcessor rp : threads) {
                rp.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<File> getResultFileList() {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public Set<Result> getPositivePeople() {
        return resultAnalyzer.listOfPositivePeople();
    }

    public boolean changeThreadsPauseState() {
        if (threads.get(0).isPaused()) {
            threads.forEach(ResultProcessor::resumeThread);
        } else {
            threads.forEach(ResultProcessor::pauseThread);
        }
        return threads.get(0).isPaused();
    }

    public void listenForInput() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.contains("exit"))
                break;
            if (changeThreadsPauseState()) {
                printStatus();
            }
        }
    }

    public void printStatus() {
        String message = "Processed %d out of %d files.\nFound %d positive people:\n%s";
        Set<Result> positivePeople = getPositivePeople();
        String affectedPeople = positivePeople.stream().map(Result::toString).reduce("", (s1, s2) -> s1 + "\n" + s2);
        message = String.format(message, amountOfFilesProcessed.get(), amountOfFilesTotal, positivePeople.size(), affectedPeople);
        System.out.println(message);
    }

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String[] args) throws InterruptedException {
        CovidAnalyzerTool covidAnalyzerTool = new CovidAnalyzerTool();

        new Thread(covidAnalyzerTool::listenForInput).start();
        Thread processingThread = new Thread(covidAnalyzerTool::processResultData);
        processingThread.start();
        processingThread.join();
        covidAnalyzerTool.printStatus();
        System.exit(0);
    }

}

