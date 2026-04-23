package fr.umlv.babaisyou.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageLoader {
    private final ConcurrentHashMap<String, List<BufferedImage>> frames = new ConcurrentHashMap<>();

    // Global animation state: all sprites advance together (like the real game)
    private long lastFrameAdvance = System.currentTimeMillis();
    private int globalFrameIndex = 0;
    private static final int FRAME_DURATION_MS = 150; // ~7 FPS animation

    /**
     * Loads all GIFs in parallel using Virtual Threads (Java 21+).
     * I/O-bound work: each GIF open+decode runs in its own virtual thread,
     * so disk/classpath latency is overlapped across all 35 assets.
     */
    public void loadImages() throws IOException {
        List<String> imagePaths;
        try (var inputStream = ImageLoader.class.getResourceAsStream("/images_list.txt")) {
            if (inputStream == null) {
                throw new IOException("images_list.txt not found");
            }
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream))) {
                imagePaths = reader.lines().filter(l -> !l.isBlank()).toList();
            }
        }

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = imagePaths.stream()
                .map(path -> executor.submit(() -> loadImage(path)))
                .toList();
            for (var future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    System.err.println("Error loading image: " + e.getMessage());
                }
            }
        }
    }

    private void loadImage(String path) {
        // Key = filename without extension (e.g. "babaEntity", "youWord", "isWord")
        String fileName = path.substring(path.lastIndexOf('/') + 1);
        String key = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;

        List<BufferedImage> gifFrames = readGifFrames(path);
        if (!gifFrames.isEmpty()) {
            frames.put(key, gifFrames);
        } else {
            System.err.println("No frames loaded for: " + path);
        }
    }

    /**
     * Reads all frames of an animated GIF using ImageReader.
     * Falls back to ImageIO.read() for single-frame images.
     */
    private List<BufferedImage> readGifFrames(String path) {
        List<BufferedImage> result = new ArrayList<>();
        var url = ImageLoader.class.getResource("/" + path);
        if (url == null) return result;

        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");
        if (!readers.hasNext()) return result;

        ImageReader reader = readers.next();
        try (ImageInputStream iis = ImageIO.createImageInputStream(url.openStream())) {
            reader.setInput(iis);
            int count = reader.getNumImages(true);
            for (int i = 0; i < count; i++) {
                result.add(reader.read(i));
            }
        } catch (IOException e) {
            System.err.println("Failed to load GIF frames: " + path + " — " + e.getMessage());
        } finally {
            reader.dispose();
        }
        return result;
    }

    /**
     * Returns the current animation frame for the given cell.
     * Advances the global frame index based on elapsed time.
     *
     * Mapping:
     *   Material  → "{name}Entity"   (e.g. "baba"  → "babaEntity")
     *   Word      → "{name}Word"     (e.g. "baba"  → "babaWord")
     *   Action    → "{name}Word"     (e.g. "you"   → "youWord")
     *   Operator  → "isWord"
     *   Element   → "emptyEntity"
     */
    public BufferedImage getNextImage(String name, Cell cell) {
        // Advance global frame counter based on time (not per-cell-call)
        long now = System.currentTimeMillis();
        if (now - lastFrameAdvance >= FRAME_DURATION_MS) {
            globalFrameIndex++;
            lastFrameAdvance = now;
        }

        String key = switch (cell) {
            case Material m -> name + "Entity";
            case Word w     -> name + "Word";
            case Action a   -> name + "Word";
            case Operator o -> "isWord";
            case Element e  -> "emptyEntity";
            default         -> name + "Entity";
        };

        List<BufferedImage> frameList = frames.get(key);
        if (frameList == null || frameList.isEmpty()) return null;
        return frameList.get(globalFrameIndex % frameList.size());
    }

    // Kept for compatibility
    public void resetAnimation(String directoryName) {
        // No-op: animation is time-based and global
    }
}
