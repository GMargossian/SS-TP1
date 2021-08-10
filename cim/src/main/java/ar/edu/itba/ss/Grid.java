package ar.edu.itba.ss;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Grid {

    final private int L;
    final private int M;
    final private double RC;
    final private double cellLong;
    final private boolean hasWalls;
    private final Cell[][] grid;
    private final List<int[]> directions = new ArrayList<>(){
        {
            //add(new int[]{0, 0});
            add(new int[]{-1, 0});
            add(new int[]{-1, 1});
            add(new int[]{0, 1});
            add(new int[]{1, 1});
        }
    };

    Grid(int L, int M,double RC,boolean hasWalls){
        this.L = L;
        this.M = M;
        this.RC = RC;
        this.hasWalls = hasWalls;
        this.cellLong = (double)L / M; // Tiene que ser entero?
        this.grid = new Cell[M][M];
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                this.grid[i][j] = new Cell();
            }
        }
    }

    public void completeGrid(List<Particle> particles){
        particles.stream().parallel().forEach(particle ->{
            int gridI =(int) (Math.floor(particle.getPosY()/cellLong));
            int gridJ = (int) (Math.floor(particle.getPosX()/cellLong));
            grid[gridI][gridJ].getParticles().add(particle);
        } );

        //particles.forEach(p -> grid[(int) (Math.floor(p.posX/M)-1)][(int) (Math.floor(p.posY/M)-1)].getParticles().add(p));
    }

    public Cell getCellFromGrid(int i, int j){
        return grid[i][j];
    }


    public void clearGrid(){
//        Arrays.stream(grid).parallel().forEach(cells -> {
//            for(int i = 0 ; i< M;i++){
//                cells[i].getParticles().clear();
//            }
//        });
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                grid[i][j].getParticles().clear();
            }
        }
    }

    public void updateNeighbours(){
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                Cell curr = this.grid[i][j];
                if(curr.hasParticles()){
                    Map<Cell,List<int[]>> neighbourCells = getCellNeighbours(this.directions,i,j);
                    //Testing parallel stream
                    curr.getParticles().parallelStream().forEach(particle-> addNeighbours(particle,curr.getParticles(),neighbourCells,this.RC));
                }
            }
        }
    }

    public void printGrid(){
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
//                System.out.println("Cell["+i+"]["+j+"]:");
                Cell curr = this.grid[i][j];
                if(curr.hasParticles()){
                    Set<Particle> particles = curr.getParticles();
                    for(Particle particle: particles){
                        System.out.println(particle);
                    }
                }
                System.out.println("###############################################################################");
            }
        }
    }

    private boolean isNeighbour(Particle p1, Particle p2, List<int[]> overflows,double RC){
        for(int[] overflow: overflows){
            double dist = Math.sqrt(Math.pow((p1.getPosX() - (p2.getPosX() + overflow[0])),2) + Math.pow((p1.getPosY() - (p2.getPosY() + overflow[1])),2)) - p1.getRadius() - p2.getRadius();
            if(dist <= RC){
                return true;
            }
        }
        return false;
    }

    public void addNeighbours(Particle particle,Set<Particle> currentCellParticles,Map<Cell,List<int[]>> neighbourCells,double RC){


        Set<Particle> auxSet = new HashSet<>(currentCellParticles);
        auxSet.remove(particle);
        Iterator<Particle> currentCellIterator = new HashSet<>(auxSet).iterator();

        while(currentCellIterator.hasNext()){
            Particle neighbour = currentCellIterator.next();
            addNeighbour(particle,neighbour,new ArrayList<>(){{add(new int[]{0,0});}},RC);
            currentCellIterator.remove();
        }

        for(Map.Entry<Cell,List<int[]>> cell: neighbourCells.entrySet()){
            List<int[]> overflow = cell.getValue();
            //Testing parallel stream
            cell.getKey().getParticles().parallelStream().forEach(neighbour-> addNeighbour(particle,neighbour,overflow,RC));

        }
    }


    private void addNeighbour(Particle particle, Particle neighbour,List<int[]> overflows,double RC){
        if(!particle.equals(neighbour)){
            if(isNeighbour(particle,neighbour,overflows,RC)){
                particle.getNeighbours().add(neighbour);
                neighbour.getNeighbours().add(particle);
            }
        }

    }

    public Map<Cell,List<int[]>> getCellNeighbours(List<int[]> directions,int i, int j){
        // Cell --> {0,-L}

        Map<Cell, List<int[]>> cells = new HashMap<>();

        for (int[] dir : directions){
            int overflowX = 0;
            int overflowY = 0;
            int dj = j+dir[1];
            int di = i+dir[0];

            if(this.hasWalls && ((dj < 0 || dj >= this.M) || (di < 0 || di >= this.M))) {
                continue;
            }

            if(dj < 0){
                overflowX = -this.L; // No pasa en nuestro caso
            }else if(dj >= this.M){
                overflowX = this.L;
            }


            if(di < 0){
                overflowY = -this.L;
            }else if(di >= this.M){
                overflowY = this.L;
            }

            int realJ = (dj + M) % this.M;
            int realI = (di + M) % this.M;
//          System.out.println("Neigbour cell["+realI+"]["+realJ+"] OVERFLOW = {"+overflowX+", "+overflowY+"}");
            Cell currentCell = this.grid[realI][realJ];
            if(cells.containsKey(currentCell)) {
                cells.get(currentCell).add(new int[]{overflowX,overflowY});
            }
            else{
                List<int[]> overflows = new ArrayList<>();
                overflows.add(new int[]{overflowX,overflowY});
                cells.put(currentCell,overflows);
            }
        }

        return cells;

    }


    public void dropDataToJSONFile(String jsonpath){
        Gson gson = new GsonBuilder().registerTypeAdapter(Particle.class, new ParticleSerializer()).create();
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"L\":").append(this.L).append(",\n");
        sb.append("\"M\":").append(this.M).append(",\n");
        sb.append("\"RC\":").append(this.RC).append(",\n");
        sb.append("\"hasWalls\":").append(this.hasWalls).append(",\n");
        sb.append("\"particles\": [\n");

        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.M; j++) {
                for(Particle p : this.grid[i][j].getParticles()){
                    sb.append(gson.toJson(p));
                    sb.append(",\n");
                }
            }
        }
        sb.deleteCharAt(sb.length()-2);
        sb.append("]}");

        try {
            FileWriter fw = new FileWriter(jsonpath);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}

