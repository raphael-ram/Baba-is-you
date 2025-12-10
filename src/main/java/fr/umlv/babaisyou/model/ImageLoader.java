package fr.umlv.babaisyou.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class ImageLoader {
    private final Map<String, List<BufferedImage>> images = new HashMap<>();
    private final Map<String, Integer> animationIndices = new HashMap<>();

    public void loadImages() throws IOException {
        try (var inputStream = ImageLoader.class.getResourceAsStream("/images_list.txt")) {
            if (inputStream == null) {
                throw new IOException("images_list.txt not found");
            }
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream))) {
                reader.lines().forEach(this::loadImage);
            }
        }
    }

    private void loadImage(String path) {
        try {
            var url = ImageLoader.class.getResource("/" + path);
            if (url != null) {
                BufferedImage img = ImageIO.read(url);
                if (img != null) {
                    // Extract folder name from path (e.g., "images/baba/baba_0.png" -> "baba")
                    String[] parts = path.split("/");
                    if (parts.length >= 2) {
                        String folderName = parts[parts.length - 2];
                        images.computeIfAbsent(folderName, k -> new ArrayList<>()).add(img);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<BufferedImage> getImages(String directoryName, Cell cell) {
        return switch (cell) {
            case Operator op -> images.getOrDefault(directoryName, new ArrayList<>());
            case Word word -> images.getOrDefault(directoryName + "_txt", new ArrayList<>());
            case Material material -> images.getOrDefault(directoryName, new ArrayList<>());
            default -> images.getOrDefault(directoryName, new ArrayList<>());
        };
    }

    public BufferedImage getNextImage(String directoryName, Cell cell) {
        List<BufferedImage> imageList = getImages(directoryName, cell);
        if (imageList.isEmpty()) {
            return null;
        }

        int index = animationIndices.getOrDefault(directoryName, 0);
        BufferedImage nextImage = imageList.get(index);

        index = (index + 1) % imageList.size();
        animationIndices.put(directoryName, index);

        return nextImage;
    }

    public void resetAnimation(String directoryName) {
        animationIndices.put(directoryName, 0);
    }
}
