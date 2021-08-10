package ar.edu.itba.ss;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

//        String static_file = "particles_static_data.txt";
//        String dynamic_file = "particles_dynamic_data.txt";
        String static_file = "random_particles_static_data.txt";
        String dynamic_file = "random_particles_dynamic_data.txt";

        ParticleSimulator ps = new ParticleSimulator(static_file,dynamic_file);
        long startTime = System.nanoTime();
        ps.simulate();

        long endTime = System.nanoTime();

        long durationMillis = (endTime - startTime)/1000000;
        System.out.println("Time in Milliseconds: " + durationMillis);



    }


}


