package sandtechnology.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ImageManager {
    private static final Map<String, CacheImage> cacheImageMap = new ConcurrentHashMap<>();
    private static final Map<String, CacheImage> localStorageImageMap = new ConcurrentHashMap<>();
    public static final CacheImage emptyImage = getImageData("https://static.hdslb.com/error/very_sorry.png");

    //删除缓存文件
    static {
        try {
            Files.walkFileTree(Paths.get("data", "image"), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String relativePath = file.subpath(2, file.getNameCount()).toString();
                    localStorageImageMap.put(relativePath, new CacheImage(file.toAbsolutePath().toFile(), relativePath));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void download(URL url, Path absolutePath) throws Exception {
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0");
        Files.createDirectories(absolutePath.getParent());
        DataContainer.getProcessDataCount().addAndGet(1);
        try (FileOutputStream writer = new FileOutputStream(absolutePath.toFile(), false); BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
            int code;
            byte[] buff = new byte[1024];
            while ((code = inputStream.read(buff)) != -1) {
                writer.write(buff, 0, code);
            }
        }
        DataContainer.getProcessDataSuccessCount().addAndGet(1);
    }

    public static CacheImage getImageData(String imgURL) {
        return getImageData(imgURL, 0);
    }

    public static CacheImage getImageData(String imgURL, int retryCount) {
        try {
            URL url = new URL(imgURL);
            Path path = Paths.get("data", "image", url.getHost(), url.getFile()).toAbsolutePath();
            if (cacheImageMap.containsKey(imgURL)) {
                if (!cacheImageMap.get(imgURL).getFile().exists()) {
                    download(url, path);
                }
                return cacheImageMap.get(imgURL).markAccessed();
            } else {
                CacheImage cacheImage;
                String relativePath = Paths.get(url.getHost(), url.getFile()).toString();
                if (localStorageImageMap.containsKey(relativePath)) {
                    cacheImage = localStorageImageMap.remove(relativePath);
                } else {
                    download(url, path);
                    cacheImage = new CacheImage(path.toAbsolutePath().toFile(), relativePath);
                }
                cacheImageMap.put(imgURL, cacheImage);
                return cacheImage.markAccessed();
            }
        } catch (FileNotFoundException e) {
            MessageHelper.sendingErrorMessage(e, "Getting Image Data,retrying...");
            return emptyImage;
        } catch (Exception e) {
            DataContainer.getProcessDataFailedCount().addAndGet(1);
            if (retryCount > 3) {
                MessageHelper.sendingErrorMessage(e, "Getting Image Data,retrying...");
                return emptyImage;
            }
            ThreadHelper.sleep(1000);
            return getImageData(imgURL, ++retryCount);
        }
    }

    public static void deleteCacheImage() {
        delete(cacheImageMap.entrySet().iterator());
        delete(localStorageImageMap.entrySet().iterator());
    }

    private static void delete(Iterator<Map.Entry<String, CacheImage>> iterator) {
        while (iterator.hasNext()) {
            Map.Entry<String, CacheImage> entry = iterator.next();
            if ((System.currentTimeMillis() - entry.getValue().getLastAccessed()) >= 1000 * 60 * 60 * 24) {
                entry.getValue().getFile().delete();
                iterator.remove();
            }
        }
    }

    public static class CacheImage {
        private final File path;
        private final String CQCode;
        private long lastAccessed = System.currentTimeMillis();

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CacheImage)) return false;
            CacheImage image = (CacheImage) o;
            return path.equals(image.path) &&
                    CQCode.equals(image.CQCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, CQCode);
        }
    }
}
