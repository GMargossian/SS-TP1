package ar.edu.itba.ss;

import com.google.gson.Gson;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ResourcesGenerator {

    private static final int MAX_ITER = 50000;

    public static void main(String[] args) {
        // pass the path to the file as a parameter
        URL config_url = ResourcesGenerator.class.getClassLoader().getResource("config/resources_generator_config.json");
        if(config_url == null){
            System.out.println("Config file not found, exiting...");
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));
            randomParticlesGeneratorConfig config = new Gson().fromJson(bufferedReader, randomParticlesGeneratorConfig.class);
            final List<int[]> directions = new ArrayList<>(){
                {
                    // add(new int[]{0, 0});
                    add(new int[]{-1, 0});
                    add(new int[]{-1, 1});
                    add(new int[]{0, 1});
                    add(new int[]{1, 1});
                    add(new int[]{1, 0});
                    add(new int[]{0, -1});
                    add(new int[]{-1, -1});
                    add(new int[]{1, -1});


                }
            };
            int N = config.getN();
            int L = config.getL();

            int RC = 0;
            boolean hasWalls = false;
            Random r = new Random();
            double radiusLimit = 0.7;
            double minRadius = 0.1;

            int M = (int) Math.floor((L/(RC + 2*(radiusLimit+minRadius))));
            double cellLong  = (double)L/M;
            Grid grid = new Grid(L,M,RC,false);
            int i = 0;
            int iter = 0;
            StringBuilder static_data_sb = new StringBuilder();
            StringBuilder dynamic_data_sb = new StringBuilder("t0\n");

            while(i < N && iter < MAX_ITER){
                System.out.println("ITER: "+iter + ", i: "+i);
                iter++;
                double posx,posy;
                double radius = config.getParticle_radius() <= 0 ? r.nextDouble()*radiusLimit + minRadius : config.getParticle_radius();
                posx = L * r.nextDouble();
                posy = L * r.nextDouble();

                Particle particle = new Particle(i,posx,posy,radius);

                int gridI =(int) (Math.floor(posy/cellLong));
                int gridJ = (int) (Math.floor(posx/cellLong));
                Cell cell = grid.getCellFromGrid(gridI,gridJ);
                cell.getParticles().add(particle);
                Map<Cell,List<int[]>> neighbourCells = grid.getCellNeighbours(directions,gridI,gridJ);
                //Testing parallel stream
                grid.addNeighbours(particle,cell.getParticles(),neighbourCells,RC);
                if(particle.hasNeighbours()){
                    // System.out.println("PARTICLE "+particle.getId()+" TIENE VECINOS: "+particle.getNeighbours());
                    cell.getParticles().remove(particle);
                    particle.getNeighbours().stream().filter(Particle::hasNeighbours).forEach(p->p.getNeighbours().remove(particle));
                    // System.out.println("BORRE PARTICLE DE VECINOS: "+particle.getNeighbours());
                }
                else{
                    i++;
                    iter = 0;

                    dynamic_data_sb.append(posx).append(" ").append(posy).append(" 0 0\n");
                    static_data_sb.append(radius).append("\n");
                }
            }
            String header = i + "\n" + L + "\n" + config.getRC() + "\n" + (config.getHasWalls() ? "1" : "0") + "\n";
            static_data_sb.insert(0,header);
            dynamic_data_sb.append("t1\n");
            grid.printGrid();
            writeToResources("random_particles_dynamic_data.txt",dynamic_data_sb.toString());
            writeToResources("random_particles_static_data.txt",static_data_sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static void writeToResources(String resource, String content){
        String path = "src/main/resources/" + resource;

        try {
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


