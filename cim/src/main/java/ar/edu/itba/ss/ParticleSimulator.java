package ar.edu.itba.ss;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

public class ParticleSimulator {

    private Grid grid;
    private Scanner dynamicData;
    private final List<Particle> particles;
    private Integer L;
    private Integer N;
    private Double RC;
    private int MAX_M;
    private Boolean hasWalls;
    private static final String benchmarkJSONPath = "benchmark_results.json";
    private static final String RESULTS_DIRECTORY = "results/";

    public ParticleSimulator(String staticDataPath, String dynamicDataPath){
        this.particles = new ArrayList<>();
        initStaticData(staticDataPath);
        initDynamicData(dynamicDataPath);

    }
    
    public void benchmark(int iterations){
        List<double[]> bestMRes = bestMBenchmark(iterations);
        List<double[]> compRes = compareBruteForceBenchmark(iterations);
        bestMRes.forEach(arr ->{
            for(int i = 0; i < arr.length;i++){
                System.out.println("For M=" + arr[0] + ", Time in Milliseconds: " + arr[1]);
            }
        });
        compRes.forEach(arr ->{
            for(int i = 0; i < arr.length;i++){
                System.out.println("For N= " + arr[0] + ", CIM took= " + arr[1] + " and BF took= " + arr[2]);
            }
        });
        writeToBenchmarkJSON(bestMRes,compRes);
    }

    private double getComparisonDuration(List<Particle> sub_particles, boolean useBF){
        grid = new Grid(L,MAX_M,RC,hasWalls,useBF);
        long startTime = System.nanoTime();
        benchmarkSimulate(sub_particles);
        long endTime = System.nanoTime();
        return ((double)(endTime - startTime))/1000000;
    }

    // Runs CIM vs Brute Force Benchmark
    private List<double[]> compareBruteForceBenchmark(int iterations) {
        List<double[]> durations = new ArrayList<>();
        for(int i = 100; i <= particles.size();i+=100){
            double acumBF = 0;
            double acumCIM = 0;
            List<Particle> aux = particles.stream().limit(i).collect(Collectors.toList());
            for(int j = 0; j < iterations; j++){
                acumBF += getComparisonDuration(aux,true);
                acumCIM += getComparisonDuration(aux,false);

            }
            System.out.println("For N= " + i + ", CIM took= " + acumCIM/iterations + " and BF took= " + acumBF/iterations);
            durations.add(new double[]{i, acumCIM/iterations, acumBF/iterations});
        }
        return durations;
    }

    // Runs best M Benchmark
    private List<double[]> bestMBenchmark(int iterations) {
        List<double[]> durations = new ArrayList<>();
        for(int i = 1; i <= MAX_M;i++){
            double acum = 0;
            for(int j = 0; j < iterations; j++){
                grid = new Grid(L,i,RC,hasWalls);
                long startTime = System.nanoTime();
                simulate();
                long endTime = System.nanoTime();
                long durationMillis = (endTime - startTime)/1000000;
                acum += durationMillis;

                System.out.println("For M=" + i + ", Time in Milliseconds: " + durationMillis);
            }
            durations.add(new double[]{i,acum/iterations});
        }
        return durations;
    }

    private void benchmarkSimulate(List<Particle> sub_particles){
        grid.completeGrid(sub_particles);
        grid.updateNeighbours();
    }

    public void simulate(){
            grid = new Grid(L,MAX_M,RC,hasWalls);
            grid.completeGrid(this.particles);
            grid.updateNeighbours();
    }

    public void createResultJSON(){
        grid.dropDataToJSONFile("data.json");
        grid.clearGrid();
    }

    public void loadDynamicData(){


        if(dynamicData != null){

            int particleId = 0;

            while(dynamicData.hasNextLine()){

                if(dynamicData.hasNextDouble()){
                    String[] values = dynamicData.nextLine().split(" ");

                    Particle particle = particles.get(particleId);
                    particle.setPosX(Double.parseDouble(values[0]));
                    particle.setPosY(Double.parseDouble(values[1]));
                    particle.setVelX(Double.parseDouble(values[2]));
                    particle.setVelY(Double.parseDouble(values[3]));
                    particleId++;

                }
                else{
                    dynamicData.nextLine();
//                    System.out.println(+" - Particles: "+particles);
                }
            }
        }else{
            throw new RuntimeException("Dynamic data scanner is null");
        }
    }

    public void initStaticData(String staticDataPath){
        Scanner sc = null;
        // pass the path to the file as a parameter
        URL resource = ParticleSimulator.class.getClassLoader().getResource(staticDataPath);
        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        } else {

            try {
                File file = new File(resource.toURI());

                try {
                    sc = new Scanner(file).useLocale(Locale.US);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if(sc != null){
                    int line = 0;
                    while (line <4 && sc.hasNextLine()){

                        switch(line){
                            case 0:
                                N = sc.nextInt();
                                break;
                            case 1:
                                L = sc.nextInt();
                                break;
                            case 2:
                                RC = sc.nextDouble();
                                break;
                            case 3:
                                hasWalls = sc.nextInt() == 1 ? Boolean.TRUE : Boolean.FALSE;
                                break;
                        }
                        line++;

                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if(L == null || RC == null || N == null || hasWalls == null){
            throw new IllegalArgumentException("Invalid particle static values");
        }

        double maxRadius = initParticles(sc);
        MAX_M = (int) Math.floor((L/(RC + 2*maxRadius)));

        System.out.println("L = "+L+" M = "+MAX_M+" RC = "+RC+" N= "+N + " hasWalls= " + hasWalls);
//        this.grid = new Grid(L,M,RC,hasWalls);

    }

    public void initDynamicData(String dynamicDataPath){

        URL resource = ParticleSimulator.class.getClassLoader().getResource(dynamicDataPath);
        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        } else {

            try {
                File file = new File(resource.toURI());

                try {
                    dynamicData = new Scanner(file).useLocale(Locale.US);
                    loadDynamicData();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public double initParticles(Scanner sc){

        // pass the path to the file as a parameter
        double maxRadius = 0;
        if(sc != null) {
            int line = 0;
            while (sc.hasNextDouble()) {
                double radius = sc.nextDouble();
                if(maxRadius < radius){
                    maxRadius = radius;
                }
                particles.add(new Particle(line++,radius));

            }
            System.out.println("Loaded "+line+" particles.");
           // System.out.println("Particles: "+particles);
        }else{
            throw new IllegalArgumentException("Scanner can't be null");
        }

        return maxRadius;
    }

    public void writeToBenchmarkJSON(List<double[]> bestMRes, List<double[]> compRes){
        File directory = new File(RESULTS_DIRECTORY);
        if (!directory.exists()){
            if(!directory.mkdir()){
                System.out.println("Couldn't create directory results, exiting...");
                System.exit(-1);
            }
        }
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder("{\n");
        sb.append("\t\"best_m_results\":").append(gson.toJson(bestMRes)).append(",\n");
        sb.append("\t\"comparison_results\":").append(gson.toJson(compRes)).append(",\n");
        sb.deleteCharAt(sb.length()-2);
        sb.append("}");

        try {
            FileWriter fw = new FileWriter(RESULTS_DIRECTORY+benchmarkJSONPath);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
