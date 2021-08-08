package ar.edu.itba.ss;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class ParticleSimulator {

    private Grid grid;
    private Scanner dynamicData;
    private final List<Particle> particles;

    public ParticleSimulator(String staticDataPath, String dynamicDataPath){
        this.particles = new ArrayList<>();
        initStaticData(staticDataPath);
        initDynamicData(dynamicDataPath);

    }

    public void simulate(){
        System.out.println("Beginning simulation. . .");

        if(dynamicData != null){
            long N = this.particles.size();
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
                    if(particleId == N){
                        grid.completeGrid(this.particles);
                        grid.updateNeighbours();
                        grid.printGrid();
                        grid.clearGrid();

                    }
                }else{
                    System.out.println(dynamicData.nextLine()+" - Particles: "+particles);
                }
            }
        }else{
            throw new RuntimeException("Dynamic data scanner is null");
        }
    }

    public void initStaticData(String staticDataPath){
        Integer L = null;
        Integer M = null;
        Integer N = null;
        Integer RC = null;
        Scanner sc = null;
        // pass the path to the file as a parameter
        URL resource = Main.class.getClassLoader().getResource(staticDataPath);
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
                                M = sc.nextInt();
                                break;
                            case 3:
                                RC = sc.nextInt();
                                break;
                        }
                        line++;

                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if(L == null || M == null || RC == null || N == null){
            throw new IllegalArgumentException("Invalid particle static values");
        }
        System.out.println("L = "+L+" M = "+M+" RC = "+RC+" N= "+N);
        this.grid = new Grid(L,M,RC);
        initParticles(sc);
    }

    public void initDynamicData(String dynamicDataPath){

        URL resource = Main.class.getClassLoader().getResource(dynamicDataPath);
        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        } else {

            try {
                File file = new File(resource.toURI());

                try {
                    dynamicData = new Scanner(file).useLocale(Locale.US);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void initParticles(Scanner sc){

        // pass the path to the file as a parameter
        if(sc != null) {
            int line = 0;
            while (sc.hasNextDouble()) {

                particles.add(new Particle(line++,sc.nextDouble()));

            }
            System.out.println("Loaded "+line+" particles.");
            System.out.println("Particles: "+particles);
        }else{
            throw new IllegalArgumentException("Scanner can't be null");
        }
    }
}
