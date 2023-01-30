package fr.leogaillet.imagemapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    public Main() {

        Scanner scanner = new Scanner(System.in);

        boolean goodFile = false;
        File file = null;

        while(!goodFile) {
            System.out.print("Quelle image voulez-vous mapper ? (Chemin absolu) : ");
            String filePath = scanner.nextLine();
            file = new File(filePath);
            System.out.println();

            if(!file.exists()) {
                System.out.println("Le fichier \"" + file.getPath() + "\" n'existe pas !");
            }else {
                goodFile = true;
            }
        }

        System.out.print("Quelle sont les dimensions de votre map ? (ej: 64x64)\n");
        System.out.print("Longueur (width : x) : ");
        int x = scanner.nextInt();
        System.out.print("Largeur (height : y) : ");
        int y = scanner.nextInt();
        Dimension dimension = new Dimension(x, y);
        System.out.println();

        ImageMapperAPI imageMapper = new ImageMapper(
                file,
                dimension.height
        );

        JSONArray mapArray = new JSONArray();
        for(int[] rows : imageMapper.get2DMapSimplified()) {
            JSONArray jsonArray = new JSONArray();
            for(int columns : rows) {
                jsonArray.put(columns);
            }
            mapArray.put(jsonArray);
        }

        JSONObject mapConfiguration = new JSONObject();
        for(Map.Entry<String, Integer> config : imageMapper.getPixelsConfiguration().entrySet()) {
            mapConfiguration.put("#" + config.getKey(), config.getValue());
        }

        JSONObject jsonConfiguration = new JSONObject();
        jsonConfiguration.put("configuration", mapConfiguration);
        jsonConfiguration.put("map", mapArray);

        try {
            File fileConfiguration = new File(file.getParentFile() + File.separator + file.getName() + ".map.json");
            FileWriter fileWriter = new FileWriter(fileConfiguration);
            jsonConfiguration.write(fileWriter, 4, 0);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
