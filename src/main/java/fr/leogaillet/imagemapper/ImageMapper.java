package fr.leogaillet.imagemapper;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ImageMapper implements ImageMapperAPI {

    private final int max_x, max_y;
    private final int[][] map;
    private final HashMap<String, Integer> configuration = new HashMap<>();


    public ImageMapper(final File file, final int height) {

        // Load Image
        BufferedImage imageFile;
        try {
            imageFile = Scalr.resize(ImageIO.read(file), Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, height);
            this.max_y = imageFile.getHeight();
            this.max_x = imageFile.getWidth();
            this.map = new int[max_y][max_x];
            System.out.println("Image redimensionnée !");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.print("Image en cours de lecture...");

        int i = 0;
        // Extract pixels data from loaded image
        for(int y = 0; y < max_y; y++) {
            for(int x = 0; x < max_x; x++) {

                int rgb = imageFile.getRGB(x, y);

                // https://www.educba.com/bit-manipulation-in-java/
                // 11111111 11111111 11111111 {24}
                int red = rgb >> 16 & 0xFF;
                int green = rgb >> 8 & 0xFF;
                int blue = rgb & 0xFF;

                String data = Integer.toHexString(red << 16 | green << 8 | blue);
                if(!configuration.containsKey(data)) {
                    configuration.put(data, i);
                    i++;
                }

                map[y][x] = configuration.get(data);

                // System.out.println("x: "+ ((y*max_x)+x) + ", r: " + red + ", g: " + green + ", b: " + blue);
                // https://stackoverflow.com/questions/11194513/convert-hex-string-to-int
            }
        }

        System.out.print("\rLecture de l'image terminée !");

    }

    @Override
    public HashMap<String, Integer> getPixelsConfiguration() {
        return configuration;
    }

    @Override
    public int[][] get2DMapSimplified() {
        return map;
    }

    @Override
    public int getMaxX() {
        return max_x;
    }

    @Override
    public int getMaxY() {
        return max_y;
    }
}
