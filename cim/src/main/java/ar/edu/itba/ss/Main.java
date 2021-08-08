package ar.edu.itba.ss;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int L = 12;
        int M = 4;
        int N = 9;
        int RC = 3;
        // pass the path to the file as a parameter
//        URL resource = Main.class.getClassLoader().getResource(path);
//        if (resource == null) {
//            throw new IllegalArgumentException("File not found!");
//        } else {
//
//            try {
//                File file = new File(resource.toURI());
//                Scanner sc = null;
//                try {
//                    sc = new Scanner(file);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                if(sc != null){
//                    int line = 0;
//                    while (line <4 && sc.hasNextLine()){
//                        System.out.println("Reading line: "+line);
//                        switch(line){
//                            case 0:
//                                N = sc.nextInt();
//                                break;
//                            case 1:
//                                L = sc.nextInt();
//                                break;
//                            case 2:
//                                M = sc.nextInt();
//                                break;
//                            case 3:
//                                RC = sc.nextInt();
//                                break;
//                        }
//                        line++;
//
//                    }
//                }
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }

        Grid grid = new Grid(L,M,RC,N);

       // Set<Particle> particlesTest = initializeParticles(sc);

        Set<Particle> particlesTest = new HashSet<>();
        particlesTest.add(new Particle(1,1.5,1.5,0.4));
        particlesTest.add(new Particle(2,3.2,2.2,0.6));
        particlesTest.add(new Particle(3,5.9,1.5,0.2));
        particlesTest.add(new Particle(4,2,11.7,0.3));
        particlesTest.add(new Particle(5,7.9,9,0.1));
        particlesTest.add(new Particle(6,2.7,11.7,0.3));
        particlesTest.add(new Particle(7,4,11.6,0.1));
        particlesTest.add(new Particle(8,11.9,11.9,0.4));


        grid.completeGrid(particlesTest);
        grid.updateNeighbours();
        grid.printGrid();

        grid.dropDataToJSONFile("data.json");

    }


    public static Set<Particle> initializeParticles(Scanner sc){
        Set<Particle> particles = new HashSet<>();
        // pass the path to the file as a parameter

            if(sc != null) {
                int line = 0;
                while (sc.hasNextLine()) {
                    System.out.println("Reading line: " + line);
                    if (line < 4) {
                        switch (line) {
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                        }
                    } else {

                    }
                    line++;
                    System.out.println(sc.nextLine());
                }
            }


        return particles;
    }

}


