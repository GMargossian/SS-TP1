package ar.edu.itba.ss;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        long startTime = System.nanoTime();

        ParticleSimulator ps = new ParticleSimulator("particles_static_data.txt","particles_dynamic_data.txt");
        ps.simulate();

        long endTime = System.nanoTime();

        long durationMillis = (endTime - startTime)/1000000;
        System.out.println("Time in Milliseconds: " + durationMillis);
    }


}


