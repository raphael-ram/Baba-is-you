package projet.Model;

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

    public void loadImages(Path basePath) throws IOException {
        Files.walk(basePath)
            .filter(Files::isRegularFile)
            .filter(file -> file.toString().endsWith(".png"))
            .forEach(file -> {
                String folderName = file.getParent().getFileName().toString();
                images.computeIfAbsent(folderName, k -> new ArrayList<>())
                      .add(loadImage(file));
            });
    }

    private BufferedImage loadImage(Path imagePath) {
        try {
            return ImageIO.read(imagePath.toFile());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
