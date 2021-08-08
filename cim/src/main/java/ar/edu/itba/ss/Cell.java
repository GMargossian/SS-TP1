package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Cell {
    private Set<Particle> particles;


    Cell(){
        this.particles = new HashSet<>();
    }

    Set<Particle> getParticles(){
        return particles;
    }

    public boolean hasParticles(){
        return !this.particles.isEmpty();
    }

    @Override
    public String toString(){
        return particles.toString();
    }
}
