package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int L = 12;
        int M = 4;
        int N = 5;

        Grid grid = new Grid(L,M,N);

        List<Particle> particlesTest = new ArrayList<>();
        particlesTest.add(new Particle(1,1.5));
        particlesTest.add(new Particle(2.2,0.5));
        particlesTest.add(new Particle(3.5,2));
        particlesTest.add(new Particle(4,2));
        particlesTest.add(new Particle(11,11));


        grid.insertParticles(particlesTest);

    }
}
