package ar.edu.itba.ss;

import java.util.List;

public class Particle {

    private double posX, posY;
    private List<Particle> neighbours;

    public Particle(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public List<Particle> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Particle> neighbours) {
        this.neighbours = neighbours;
    }

    public boolean hasNeighbours(){
        return !this.neighbours.isEmpty();
    }

    @Override
    public String toString() {
        return "Particle{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", neighbours=" + neighbours +
                '}';
    }
}

