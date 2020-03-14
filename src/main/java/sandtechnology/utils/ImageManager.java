package sandtechnology.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageManager {
    private static Map<String, CacheImage> cacheImageMap = new ConcurrentHashMap<>();

    public static CacheImage getImageData(String imgURL) {

        if (cacheImageMap.containsKey(imgURL)) {
            return cacheImageMap.get(imgURL).markAccessed();
        }

        try {
            URLConnection connection = new URL(imgURL).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0");
            Path absolutePath = Paths.get("data", "image", connection.getURL().getFile()).toAbsolutePath();
            Files.createDirectories(absolutePath.getParent());
            try (FileOutputStream writer = new FileOutputStream(absolutePath.toFile(), false); BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
                int code;
                byte[] buff = new byte[1024];
                while ((code = inputStream.read(buff)) != -1) {
                    writer.write(buff, 0, code);
                }
            }
            CacheImage cacheImage = new CacheImage(absolutePath.toFile(), connection.getURL().getFile());
            cacheImageMap.put(imgURL, cacheImage);
            return cacheImage.markAccessed();
        } catch (Exception e) {
            MessageHelper.sendingErrorMessage(e, "GettingImageData,retrying...");
            ThreadHelper.sleep(1000);
            return getImageData(imgURL);
        }
    }

    public static class CacheImage {
        private final File path;
        private final String CQCode;
        private long lastAccessed;

        public CacheImage(File absolutePath, String relativePath) {
            this.path = absolutePath;
            path.deleteOnExit();
            CQCode = "[CQ:image,file=" + relativePath.substring(1).replace('/', '\\') + "]";
        }


        public String toCQCode() {
            return CQCode;
        }

        CacheImage markAccessed() {
            this.lastAccessed = System.currentTimeMillis();
            return this;
        }

        public long getLastAccessed() {
            return lastAccessed;
        }

        public File getFile() {
            return path;
        }
    }
}
