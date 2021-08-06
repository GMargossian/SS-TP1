package ar.edu.itba.ss;


import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    final private int L;
    final private int M;
    final private double cellLong;
    final private long N;
    private Cell[][] grid;


    Grid(int L, int M,long N){
        this.L = L;
        this.M = M;
        this.N = N;
        this.cellLong = (double)L / M; // Tiene que ser entero?
        this.grid = new Cell[M][M];
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                this.grid[i][j] = new Cell();
            }
        }
    }

    public boolean insertParticles(List<Particle> particles){
        for(Particle particle : particles){
            int gridX =(int) (Math.floor(particle.getPosX()/cellLong));
            int gridY = (int) (Math.floor(particle.getPosY()/cellLong));
            System.out.println("Particle: "+particle);

            System.out.println("Grid Pos: "+ gridX + " " + gridY);

            grid[gridX][gridY].getParticles().add(particle);
            System.out.println("Updated Cell: " + grid[gridX][gridY]);
        }
        //particles.forEach(p -> grid[(int) (Math.floor(p.posX/M)-1)][(int) (Math.floor(p.posY/M)-1)].getParticles().add(p));


        return false;
    }


}
