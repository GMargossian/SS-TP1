package ar.edu.itba.ss;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Particle {

    private int id;
    private double posX, posY;
    private Set<Particle> neighbours;
    private double radius;

    public Particle(int id, double posX, double posY, double radius) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        this.neighbours = new HashSet<>();
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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Set<Particle> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Set<Particle> neighbours) {
        this.neighbours = neighbours;
    }

    public boolean hasNeighbours(){
        return !this.neighbours.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Particle:" + id + "{" +
                "posX=" + posX +
                ", posY=" + posY+", neighbours= [");
        for(Particle particle: neighbours){
            sb.append("Particle"+ particle.id + "{ posX= ").append(particle.getPosX()).append(", posY= ").append(particle.posY).append("}, ");
        }
        return sb.append("]}").toString();
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Particle)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Particle other = (Particle) o;

        // Compare the data members and return accordingly
        return this.posX == other.getPosX() && this.posY == other.getPosY() && this.radius  == other.getRadius();
    }
}

