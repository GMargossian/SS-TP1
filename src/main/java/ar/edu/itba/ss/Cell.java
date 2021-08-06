package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private List<Particle> particles;

    Cell(){
        this.particles = new ArrayList<>();
    }

    List<Particle> getParticles(){
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
