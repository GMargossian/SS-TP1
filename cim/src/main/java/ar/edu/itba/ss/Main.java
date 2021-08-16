package ar.edu.itba.ss;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        URL config_url = ResourcesGenerator.class.getClassLoader().getResource("config/cim_config.json");
        if(config_url == null){
            System.out.println("Config file not found, exiting...");
            return;
        }
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));
            CimConfig config = new Gson().fromJson(bufferedReader, CimConfig.class);
            String static_file = config.getStatic_file();
            String dynamic_file = config.getDynamic_file();
            System.out.println(static_file);
            System.out.println(dynamic_file);
            ParticleSimulator ps = new ParticleSimulator(static_file,dynamic_file);
            switch (config.getTo_run()){
                case "simulation":
                    ps.simulate();
                    ps.createResultJSON();
                    break;
                case "benchmark":
                    ps.benchmark(config.getBenchmark_iterations());

                    break;
                default:
                    System.out.println("to_run value not allowed, exiting...");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }


}


