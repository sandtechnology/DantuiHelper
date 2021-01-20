package sandtechnology.utils;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageManager {
    private static final Map<String, CacheImage> cacheImageMap = new ConcurrentHashMap<>();
    private static final Map<String, CacheImage> localStorageImageMap = new ConcurrentHashMap<>();
    public static final CacheImage emptyImage = new CacheImage.ImagePlaceHolder("", CacheImage.ImagePlaceHolder.Reason.Null);
    private static boolean noImageMode = false;

    public static void setNoImageMode(boolean noImageMode) {
        ImageManager.noImageMode = noImageMode;
    }

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
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:75.0) Gecko/20100101 Firefox/75.0");
        Files.createDirectories(absolutePath.getParent());
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
        try (FileOutputStream writer = new FileOutputStream(absolutePath.toFile(), false); BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
            int code;
            byte[] buff = new byte[1024];
            while ((code = inputStream.read(buff)) != -1) {
                writer.write(buff, 0, code);
            }
        }
        DataContainer.getProcessDataSuccessCount().incrementAndGet();
    }

    public static CacheImage getImageData(String imgURL) {
        return getImageData(imgURL, 0);
    }

    public static CacheImage getImageData(String imgURL, int retryCount) {
        if (noImageMode) {
            return new CacheImage.ImagePlaceHolder(imgURL);
        }
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
            DataContainer.getMessageHelper().sendingErrorMessage(e, "Getting Image Data,retrying...");
            return new CacheImage.ImagePlaceHolder(imgURL, CacheImage.ImagePlaceHolder.Reason.Image_Not_Found);
        } catch (Exception e) {
            DataContainer.getProcessDataFailedCount().incrementAndGet();
            if (retryCount > 3) {
                DataContainer.getMessageHelper().sendingErrorMessage(e, "Getting Image Data,retrying...");
                return new CacheImage.ImagePlaceHolder(imgURL, CacheImage.ImagePlaceHolder.Reason.Download_Failed);
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
                //noinspection ResultOfMethodCallIgnored
                entry.getValue().getFile().delete();
                iterator.remove();
            }
        }
    }

}
