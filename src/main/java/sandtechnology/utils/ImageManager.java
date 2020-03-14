package sandtechnology.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageManager {
    private static Map<String, CacheImage> cacheImageMap = new ConcurrentHashMap<>();


    private static void download(URL url, Path absolutePath) throws Exception {
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0");
        Files.createDirectories(absolutePath.getParent());
        try (FileOutputStream writer = new FileOutputStream(absolutePath.toFile(), false); BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
            int code;
            byte[] buff = new byte[1024];
            while ((code = inputStream.read(buff)) != -1) {
                writer.write(buff, 0, code);
            }
        }
    }

    public static CacheImage getImageData(String imgURL) {
        try {
            URL url = new URL(imgURL);
            Path path = Paths.get("data", "image", url.getFile()).toAbsolutePath();
            if (cacheImageMap.containsKey(imgURL)) {
                if (!cacheImageMap.get(imgURL).getFile().exists()) {
                    download(url, path);
                }
                return cacheImageMap.get(imgURL).markAccessed();
            } else {
                if (!Files.exists(path)) {
                    download(url, path);
                }
                CacheImage cacheImage = new CacheImage(path.toFile(), url.getFile());
                cacheImageMap.put(imgURL, cacheImage);
                return cacheImage.markAccessed();
            }
        } catch (Exception e) {
            MessageHelper.sendingErrorMessage(e, "GettingImageData,retrying...");
            ThreadHelper.sleep(1000);
            return getImageData(imgURL);
        }
    }

    public static void deleteCacheImage() {
        Iterator<Map.Entry<String, CacheImage>> iterator = cacheImageMap.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().getValue().getFile().delete();
            iterator.remove();
        }
    }

    public static class CacheImage {
        private final File path;
        private final String CQCode;
        private long lastAccessed;

        public CacheImage(File absolutePath, String relativePath) {
            this.path = absolutePath;
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
