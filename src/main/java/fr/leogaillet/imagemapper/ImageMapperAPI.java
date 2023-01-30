package fr.leogaillet.imagemapper;

import java.util.HashMap;

public interface ImageMapperAPI {

    HashMap<String, Integer> getPixelsConfiguration();

    int[][] get2DMapSimplified();

    int getMaxX();

    int getMaxY();

}
