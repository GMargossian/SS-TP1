package ar.edu.itba.ss;

public class randomParticlesGeneratorConfig {
    private int N;
    private int L;
    private int RC;
    private boolean hasWalls;
    private double particle_radius;

    public randomParticlesGeneratorConfig(int N,int L, int RC, boolean hasWalls, double particle_radius){
        this.N = N;
        this.L = L;
        this.RC = RC;
        this.hasWalls = hasWalls;
        this.particle_radius = particle_radius;
    }

    public int getL() {
        return L;
    }

    public void setL(int l) {
        L = l;
    }

    public int getRC() {
        return RC;
    }

    public void setRC(int RC) {
        this.RC = RC;
    }

    public boolean getHasWalls() {
        return hasWalls;
    }

    public void setHasWalls(boolean hasWalls) {
        this.hasWalls = hasWalls;
    }

    public double getParticle_radius() {
        return particle_radius;
    }

    public void setParticle_radius(int particle_radius) {
        this.particle_radius = particle_radius;
    }

    public int getN() {
        return N;
    }
}
